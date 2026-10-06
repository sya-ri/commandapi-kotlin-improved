package dev.s7a.commandapi.quality

import dev.detekt.api.Config
import dev.detekt.api.Detektion
import dev.detekt.api.Issue
import dev.detekt.api.RuleInstance
import dev.detekt.api.RuleSetId
import dev.detekt.api.Severity
import dev.detekt.test.utils.KotlinAnalysisApiEngine
import dev.detekt.test.utils.createEnvironment
import org.jetbrains.kotlin.config.LanguageVersionSettingsImpl

internal object CommandCompletionRuleFixture {
    val dependencies =
        listOf(
            """
            package dev.jorel.commandapi.arguments
            open class Argument<T>
            class LiteralArgument(val name: String) : Argument<String>()
            class StringArgument(val name: String) : Argument<String>()
            """.trimIndent(),
            "package dev.jorel.commandapi; class CommandTree",
            """
            package fixture.presentation.command
            import kotlin.reflect.KClass
            import dev.jorel.commandapi.CommandTree
            import dev.jorel.commandapi.arguments.Argument
            abstract class Command {
                abstract class Root<T>(name: String, block: CommandTree.() -> Unit = {})
                abstract class Child<P : Any, T>(parent: KClass<P>, name: String, block: Argument<*>.() -> Unit = {})
            }
            """.trimIndent(),
            """
            package dev.s7a.commandapi
            import dev.jorel.commandapi.CommandTree
            import dev.jorel.commandapi.arguments.Argument
            fun commandTree(name: String, block: CommandTree.() -> Unit) {}
            fun CommandTree.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}) {}
            fun Argument<*>.literalArgument(literal: String, block: Argument<*>.() -> Unit = {}) {}
            fun CommandTree.stringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}) {}
            fun Argument<*>.stringArgument(nodeName: String, block: Argument<*>.() -> Unit = {}) {}
            fun Argument<*>.integerArgument(nodeName: String, block: Argument<*>.() -> Unit = {}) {}
            fun Argument<*>.entitySelectorArgumentOnePlayer(nodeName: String, block: Argument<*>.() -> Unit = {}) {}
            fun Argument<*>.argument(argument: Argument<*>, block: Argument<*>.() -> Unit = {}) {}
            fun Argument<*>.anyExecutor(block: () -> Unit) {}
            """.trimIndent(),
        )

    fun lint(
        code: String,
        config: Config = Config.empty,
        vararg extra: String,
    ): List<Issue> {
        KotlinAnalysisApiEngine().use { engine ->
            val file =
                engine.compile(
                    code.trimIndent(),
                    dependencyCodes = dependencies + extra,
                    jvmClasspathRoots = createEnvironment().jvmClasspathRoots,
                    allowCompilationErrors = false,
                )
            val listener = CommandCompletionFileProcessListener()
            listener.onStart(listOf(file))
            val rule =
                NoMixedLiteralAndArgumentSiblings(
                    if (config ===
                        Config.empty
                    ) {
                        TestConfig(mapOf("commandBase" to "fixture.presentation.command.Command"))
                    } else {
                        config
                    },
                )
            rule.visitFile(file, LanguageVersionSettingsImpl.DEFAULT)
            listener.onProcessComplete(file, emptyList())
            return listener
                .onFinish(
                    listOf(file),
                    Detektion(
                        issues = emptyList(),
                        rules =
                            listOf(
                                RuleInstance(
                                    id = NoMixedLiteralAndArgumentSiblings.RULE_ID,
                                    ruleSetId = RuleSetId("commandApi"),
                                    url = null,
                                    description = rule.description,
                                    severity = Severity.Error,
                                    active = true,
                                ),
                            ),
                    ),
                ).issues
        }
    }

    fun lintFiles(vararg codes: String): List<Issue> {
        KotlinAnalysisApiEngine().use { engine ->
            val files =
                codes.mapIndexed { index, code ->
                    engine.compile(
                        code.trimIndent(),
                        dependencyCodes = dependencies + codes.filterIndexed { other, _ -> other != index },
                        jvmClasspathRoots = createEnvironment().jvmClasspathRoots,
                        allowCompilationErrors = false,
                    )
                }
            val listener = CommandCompletionFileProcessListener()
            listener.onStart(files)
            val rule = NoMixedLiteralAndArgumentSiblings(TestConfig(mapOf("commandBase" to "fixture.presentation.command.Command")))
            files.forEach { file ->
                rule.visitFile(file, LanguageVersionSettingsImpl.DEFAULT)
                listener.onProcessComplete(file, emptyList())
            }
            return listener
                .onFinish(
                    files,
                    Detektion(
                        issues = emptyList(),
                        rules =
                            listOf(
                                RuleInstance(
                                    id = NoMixedLiteralAndArgumentSiblings.RULE_ID,
                                    ruleSetId = RuleSetId("commandApi"),
                                    url = null,
                                    description = rule.description,
                                    severity = Severity.Error,
                                    active = true,
                                ),
                            ),
                    ),
                ).issues
        }
    }
}
