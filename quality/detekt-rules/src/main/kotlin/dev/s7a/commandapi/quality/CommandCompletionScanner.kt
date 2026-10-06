package dev.s7a.commandapi.quality

import org.jetbrains.kotlin.analysis.api.analyze
import org.jetbrains.kotlin.analysis.api.resolution.KaFunctionCall
import org.jetbrains.kotlin.analysis.api.resolution.successfulCallOrNull
import org.jetbrains.kotlin.analysis.api.types.KaClassType
import org.jetbrains.kotlin.psi.KtBlockExpression
import org.jetbrains.kotlin.psi.KtCallExpression
import org.jetbrains.kotlin.psi.KtDotQualifiedExpression
import org.jetbrains.kotlin.psi.KtExpression
import org.jetbrains.kotlin.psi.KtLambdaExpression
import org.jetbrains.kotlin.psi.KtNameReferenceExpression
import org.jetbrains.kotlin.psi.KtNamedFunction
import org.jetbrains.kotlin.psi.KtStringTemplateExpression

/**
 * Follows direct DSL calls and source receiver helpers, never executor callbacks or arbitrary code.
 */
internal class CommandCompletionScanner(
    private val scopes: MutableList<CommandCompletionScope>,
) {
    /**
     * Reads a literal node name without accepting interpolation.
     */
    fun literal(expression: KtExpression?): String? =
        (expression as? KtStringTemplateExpression)?.takeUnless { it.hasInterpolation() }?.entries?.joinToString("") { it.text }

    /**
     * Collects sibling nodes from a DSL body and follows source receiver helpers.
     */
    fun scan(
        expression: KtExpression?,
        scope: CommandCompletionScope,
        bindings: Map<String, KtExpression> = emptyMap(),
        visiting: Set<KtNamedFunction> = emptySet(),
    ) {
        when (expression) {
            is KtBlockExpression -> {
                expression.statements.forEach { scan(it, scope, bindings, visiting) }
            }

            is KtDotQualifiedExpression -> {
                // Only explicit `this` addresses this node. A different receiver is a different tree.
                if (expression.receiverExpression.text == "this") scan(expression.selectorExpression, scope, bindings, visiting)
            }

            is KtCallExpression -> {
                scanCall(expression, scope, bindings, visiting)
            }
        }
    }

    private fun scanCall(
        expression: KtCallExpression,
        scope: CommandCompletionScope,
        bindings: Map<String, KtExpression>,
        visiting: Set<KtNamedFunction>,
    ) {
        analyze(expression) {
            val call = expression.resolveToCall()?.successfulCallOrNull<KaFunctionCall<*>>() ?: return@analyze
            val symbol = call.partiallyAppliedSymbol.signature.symbol
            val callableId = symbol.callableId ?: return@analyze
            val receiver = (symbol.receiverParameter?.returnType as? KaClassType)?.classId?.asSingleFqName()?.asString()
            if (receiver !in receiverTypes) return@analyze
            val arguments =
                call.argumentMapping.entries.associate { (value, parameter) ->
                    parameter.name.asString() to substitute(value, bindings)
                }
            if (callableId.packageName.asString() == "dev.s7a.commandapi") {
                val name = callableId.callableName.asString()
                if (name.endsWith("Executor") || name == "flagArguments") return@analyze
                if (name.contains("Argument").not() && name !in setOf("argument", "optionalArgument")) return@analyze
                when (name) {
                    "literalArgument" -> {
                        scope.literals +=
                            literal(arguments["literal"] ?: arguments["nodeName"] ?: arguments.values.firstOrNull())
                    }

                    "multiLiteralArgument" -> {
                        val values =
                            expression.valueArguments
                                .drop(1)
                                .mapNotNull { it.getArgumentExpression() }
                                .filterNot { it is KtLambdaExpression }
                                .map { literal(substitute(it, bindings)) }
                        scope.literals += values.ifEmpty { listOf(null) }
                    }

                    "argument", "optionalArgument" -> {
                        val argument = arguments["argument"] ?: arguments.values.firstOrNull()
                        val type = (argument?.expressionType as? KaClassType)?.classId?.shortClassName?.asString()
                        if (type in setOf("LiteralArgument", "MultiLiteralArgument")) {
                            val constructor = argument as? KtCallExpression
                            scope.literals += literal(constructor?.valueArguments?.firstOrNull()?.getArgumentExpression())
                        } else {
                            scope.hasArgument = true
                        }
                    }

                    else -> {
                        scope.hasArgument = true
                    }
                }
                val block = arguments["block"] as? KtLambdaExpression
                if (block != null) {
                    val child = CommandCompletionScope(block)
                    scopes += child
                    scan(block.bodyExpression, child, bindings, visiting)
                }
            } else {
                val helper = symbol.psi as? KtNamedFunction ?: return@analyze
                if (helper in visiting) return@analyze
                scan(helper.bodyExpression, scope, arguments, visiting + helper)
            }
        }
    }

    private fun substitute(
        expression: KtExpression,
        bindings: Map<String, KtExpression>,
    ): KtExpression = if (expression is KtNameReferenceExpression) bindings[expression.getReferencedName()] ?: expression else expression

    private companion object {
        val receiverTypes = setOf("dev.jorel.commandapi.CommandTree", "dev.jorel.commandapi.arguments.Argument")
    }
}
