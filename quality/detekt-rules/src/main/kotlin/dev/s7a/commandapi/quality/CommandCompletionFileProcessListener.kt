package dev.s7a.commandapi.quality

import com.intellij.psi.PsiElement
import dev.detekt.api.Config
import dev.detekt.api.Detektion
import dev.detekt.api.Entity
import dev.detekt.api.FileProcessListener
import dev.detekt.api.Issue
import dev.detekt.api.Location
import dev.detekt.api.RuleInstance
import dev.detekt.api.SetupContext
import org.jetbrains.kotlin.psi.KtAnnotated
import org.jetbrains.kotlin.psi.KtAnnotationEntry
import org.jetbrains.kotlin.psi.KtElement
import org.jetbrains.kotlin.psi.KtFile
import java.nio.file.Path
import java.util.concurrent.ConcurrentLinkedQueue

/**
 * Joins resolved command definitions across detekt's per-file rule instances.
 *
 * Candidate collection is thread-safe because detekt may call [onProcessComplete] in parallel.
 */
class CommandCompletionFileProcessListener : FileProcessListener {
    override val id: String = "CommandCompletionFileProcessListener"

    private var basePath: Path = Path.of("").toAbsolutePath()
    private var aliases = emptySet<String>()
    private val candidates = ConcurrentLinkedQueue<CommandCompletionScope>()

    override fun init(context: SetupContext) {
        basePath = context.basePath
        val ruleConfig =
            context.config
                .subConfig("commandApi")
                .subConfig(NoMixedLiteralAndArgumentSiblings.RULE_ID)
        aliases = ruleConfig.valueOrDefault(Config.ALIASES_KEY, emptyList<String>()).toSet()
    }

    override fun onStart(files: List<KtFile>) {
        candidates.clear()
        files.forEach { it.putUserData(NoMixedLiteralAndArgumentSiblings.scopesKey, null) }
    }

    override fun onProcessComplete(
        file: KtFile,
        issues: List<Issue>,
    ) {
        candidates.addAll(file.getUserData(NoMixedLiteralAndArgumentSiblings.scopesKey).orEmpty())
    }

    override fun onFinish(
        files: List<KtFile>,
        result: Detektion,
    ): Detektion {
        val ruleInstance =
            result.rules.firstOrNull { rule ->
                rule.id == NoMixedLiteralAndArgumentSiblings.RULE_ID &&
                    rule.ruleSetId.value == "commandApi" &&
                    rule.active
            } ?: return result
        val children = candidates.filter { it.parentClass != null }.groupBy { it.parentClass }
        val issues =
            candidates
                .filter { scope ->
                    scope.hasArgument &&
                        (scope.literals + children[scope.commandClass].orEmpty().map { it.name })
                            .any { it?.startsWith("--") != true }
                }.filterNot { it.isSuppressed() }
                .sortedWith(compareBy({ it.element.containingKtFile.virtualFilePath }, { it.element.textOffset }))
                .map { it.toIssue(ruleInstance) }
        if (issues.isEmpty()) {
            return result
        }

        return result.copyWithIssues(result.issues + issues)
    }

    private fun CommandCompletionScope.toIssue(ruleInstance: RuleInstance): Issue {
        val entity = Entity.from(element)
        return Issue(
            ruleInstance = ruleInstance,
            entity = entity.toIssueEntity(),
            references = emptyList(),
            message =
                "Literal commands and argument nodes share a completion position. " +
                    "Place the operation literal first; -- flags are exempt.",
            severity = ruleInstance.severity,
            suppressReasons = emptyList(),
        )
    }

    private fun CommandCompletionScope.isSuppressed(): Boolean {
        val acceptedNames =
            buildSet {
                add(NoMixedLiteralAndArgumentSiblings.RULE_ID)
                add("commandApi")
                add("${"commandApi"}.${NoMixedLiteralAndArgumentSiblings.RULE_ID}")
                add("${"commandApi"}:${NoMixedLiteralAndArgumentSiblings.RULE_ID}")
                addAll(globalSuppressions)
                addAll(aliases)
            }
        return element
            .annotationsWithParents()
            .filter { annotation -> annotation.isSuppressionAnnotation() }
            .flatMap { annotation -> annotation.valueArguments.asSequence() }
            .mapNotNull { argument -> argument.getArgumentExpression()?.text }
            .map { value -> value.replace(detektPrefix, "").replace("\"", "") }
            .any(acceptedNames::contains)
    }

    private fun KtElement.annotationsWithParents(): Sequence<KtAnnotationEntry> =
        generateSequence<PsiElement>(this) { element -> element.parent }
            .filterIsInstance<KtAnnotated>()
            .flatMap { annotated -> annotated.annotationEntries.asSequence() }

    private fun KtAnnotationEntry.isSuppressionAnnotation(): Boolean =
        typeReference?.text?.substringAfterLast('.') in suppressionAnnotations

    private fun Entity.toIssueEntity(): Issue.Entity =
        Issue.Entity(
            signature = signature,
            location = location.toIssueLocation(),
        )

    private fun Location.toIssueLocation(): Issue.Location =
        Issue.Location(
            source = source,
            endSource = endSource,
            text = text,
            path =
                if (path.isAbsolute && basePath.isAbsolute && path.root == basePath.root) {
                    basePath.relativize(path)
                } else {
                    path
                },
        )

    private fun Detektion.copyWithIssues(issues: List<Issue>): Detektion =
        Detektion(
            issues = issues,
            rules = rules,
            metrics = metrics,
            notifications = notifications,
            userData = userData,
        )

    private companion object {
        val detektPrefix = Regex("detekt[.:]", RegexOption.IGNORE_CASE)
        val globalSuppressions = setOf("ALL", "all", "All")
        val suppressionAnnotations = setOf("Suppress", "SuppressWarnings")
    }
}
