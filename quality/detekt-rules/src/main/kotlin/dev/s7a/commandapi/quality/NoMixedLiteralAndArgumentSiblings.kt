package dev.s7a.commandapi.quality

import com.intellij.openapi.util.Key
import dev.detekt.api.Config
import dev.detekt.api.RequiresAnalysisApi
import dev.detekt.api.Rule
import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.resolution.KaFunctionCall
import org.jetbrains.kotlin.analysis.api.resolution.successfulCallOrNull
import org.jetbrains.kotlin.analysis.api.types.KaClassType
import org.jetbrains.kotlin.analysis.api.types.KaTypeArgumentWithVariance
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtClass
import org.jetbrains.kotlin.psi.KtFile
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtSuperTypeCallEntry

/**
 * Collects resolved DSL trees; the listener joins Command.Child definitions across files.
 */
class NoMixedLiteralAndArgumentSiblings(
    config: Config,
) : Rule(
        config,
        "Do not mix literal commands and argument nodes at one completion position, except -- flags.",
    ),
    RequiresAnalysisApi {
    private val scopes = mutableListOf<CommandCompletionScope>()
    private val scanner = CommandCompletionScanner(scopes)
    private val commandBase = config.valueOrDefault("commandBase", "")

    override fun visit(root: KtFile) {
        scopes.clear()
        super.visit(root)
        root.putUserData(scopesKey, scopes.toList())
    }

    override fun visitClass(klass: KtClass) {
        super.visitClass(klass)
        if (commandBase.isBlank()) return
        val classId = klass.fqName?.asString() ?: return
        klass.superTypeListEntries.filterIsInstance<KtSuperTypeCallEntry>().forEach { entry ->
            val reference = entry.typeReference ?: return@forEach
            val identities =
                analyze(reference) {
                    val type = reference.type as? KaClassType ?: return@analyze null
                    val base = type.classId.asSingleFqName().asString()
                    val parent =
                        ((type.typeArguments.firstOrNull() as? KaTypeArgumentWithVariance)?.type as? KaClassType)
                            ?.classId
                            ?.asSingleFqName()
                            ?.asString()
                    base to parent
                } ?: return@forEach
            val isChild = identities.first == "$commandBase.Child"
            if (isChild.not() && identities.first != "$commandBase.Root") return@forEach
            val arguments = entry.valueArguments.mapNotNull { it.getArgumentExpression() }
            val scope =
                CommandCompletionScope(
                    element = klass,
                    commandClass = classId,
                    parentClass = if (isChild) identities.second else null,
                    name = scanner.literal(arguments.getOrNull(if (isChild) 1 else 0)),
                )
            scopes += scope
            arguments.filterIsInstance<KtLambdaExpression>().forEach { scanner.scan(it.bodyExpression, scope) }
        }
    }

    override fun visitCallExpression(expression: KtCallExpression) {
        super.visitCallExpression(expression)
        if (expression.calleeExpression?.text?.contains("commandTree", ignoreCase = true) != true &&
            expression.containingKtFile.importDirectives.none { it.aliasName == expression.calleeExpression?.text }
        ) {
            return
        }
        val callable =
            analyze(expression) {
                expression
                    .resolveToCall()
                    ?.successfulCallOrNull<KaFunctionCall<*>>()
                    ?.partiallyAppliedSymbol
                    ?.signature
                    ?.callableId
                    ?.asSingleFqName()
                    ?.asString()
            }
        if (callable != "dev.s7a.commandapi.commandTree") return
        val scope = CommandCompletionScope(expression)
        scopes += scope
        expression.valueArguments
            .mapNotNull { it.getArgumentExpression() }
            .filterIsInstance<KtLambdaExpression>()
            .forEach { scanner.scan(it.bodyExpression, scope) }
    }

    internal companion object {
        /**
         * Stable rule ID used by configuration and listener diagnostics.
         */
        const val RULE_ID = "NoMixedLiteralAndArgumentSiblings"
        val scopesKey = Key.create<List<CommandCompletionScope>>("$RULE_ID.scopes")
    }
}
