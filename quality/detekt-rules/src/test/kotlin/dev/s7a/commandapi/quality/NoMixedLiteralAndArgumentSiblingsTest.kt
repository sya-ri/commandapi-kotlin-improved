package dev.s7a.commandapi.quality

import dev.detekt.api.Config
import kotlin.test.Test
import kotlin.test.assertEquals

class NoMixedLiteralAndArgumentSiblingsTest {
    @Test
    fun `rejects a literal beside standard arguments once per node`() {
        assertEquals(
            1,
            lint(
                """
            literalArgument("create") {}
            stringArgument("id") {}
            integerArgument("count") {}
            entitySelectorArgumentOnePlayer("player") {}
        """,
            ).size,
        )
    }

    @Test
    fun `allows flags and executors beside dynamic arguments`() {
        assertEquals(
            0,
            lint(
                """
            literalArgument("--all") {}
            literalArgument("--offline") {}
            stringArgument("id") {}
            anyExecutor {}
        """,
            ).size,
        )
    }

    @Test
    fun `allows separate levels and homogeneous siblings`() {
        assertEquals(
            0,
            lint(
                """
            literalArgument("info") { stringArgument("id") {} }
            literalArgument("edit") {
                stringArgument("id") { literalArgument("remove") {} }
                integerArgument("number") {}
            }
        """,
            ).size,
        )
    }

    @Test
    fun `reports nested mixing and explicit this calls`() {
        assertEquals(
            1,
            lint(
                """
            literalArgument("edit") {
                this.literalArgument("create") {}
                stringArgument("id") {}
            }
        """,
            ).size,
        )
    }

    @Test
    fun `follows aliased DSL and source argument helper lambda`() {
        val code = """
            package test
            import fixture.presentation.command.Command
            import dev.s7a.commandapi.literalArgument as branch
            import dev.s7a.commandapi.stringArgument
            import helpers.idArgument
            class Parent
            class TestCommand : Command.Child<Parent, TestCommand>(Parent::class, "test", {
                idArgument("id") {
                    branch("create") {}
                    stringArgument("other") {}
                }
            })
        """
        assertEquals(
            1,
            CommandCompletionRuleFixture
                .lint(
                    code,
                    Config.empty,
                    """
                    package helpers
                    import dev.jorel.commandapi.arguments.Argument
                    import dev.jorel.commandapi.arguments.StringArgument
                    import dev.s7a.commandapi.argument
                    fun Argument<*>.idArgument(name: String, block: Argument<*>.() -> Unit) = argument(StringArgument(name), block)
                    """.trimIndent(),
                ).size,
        )
    }

    @Test
    fun `helper with literal branches contributes to the caller node`() {
        val code = """
            package test
            import fixture.presentation.command.Command
            import dev.s7a.commandapi.stringArgument
            import helpers.roles
            class Parent
            class TestCommand : Command.Child<Parent, TestCommand>(Parent::class, "test", {
                roles()
                stringArgument("id") {}
            })
        """
        assertEquals(
            1,
            CommandCompletionRuleFixture
                .lint(
                    code,
                    Config.empty,
                    """
                    package helpers
                    import dev.jorel.commandapi.arguments.Argument
                    import dev.s7a.commandapi.literalArgument
                    fun Argument<*>.roles() { literalArgument("shop") {}; literalArgument("tp") {} }
                    """.trimIndent(),
                ).size,
        )
    }

    @Test
    fun `joins child commands to their parent`() {
        assertEquals(
            1,
            CommandCompletionRuleFixture
                .lint(
                    """
            package test
            import fixture.presentation.command.Command
            import dev.s7a.commandapi.stringArgument
            class Parent : Command.Root<Parent>("test", { stringArgument("id") {} })
            class Child : Command.Child<Parent, Child>(Parent::class, "info")
        """,
                ).size,
        )
        assertEquals(
            0,
            CommandCompletionRuleFixture
                .lint(
                    """
            package test
            import fixture.presentation.command.Command
            import dev.s7a.commandapi.stringArgument
            class Parent : Command.Root<Parent>("test", { stringArgument("id") {} })
            class Child : Command.Child<Parent, Child>(Parent::class, "--all")
        """,
                ).size,
        )
    }

    @Test
    fun `joins a child literal declared in another source file`() {
        assertEquals(
            1,
            CommandCompletionRuleFixture
                .lintFiles(
                    """
            package test
            import fixture.presentation.command.Command
            import dev.s7a.commandapi.stringArgument
            class Parent : Command.Root<Parent>("test", { stringArgument("id") {} })
            """,
                    """
            package test
            import fixture.presentation.command.Command
            class Child : Command.Child<Parent, Child>(Parent::class, "info")
            """,
                ).size,
        )
    }

    @Test
    fun `ignores unrelated functions and different explicit receivers`() {
        assertEquals(
            0,
            lint(
                """
            fun literalArgument(name: String) {}
            literalArgument("create")
            val other = dev.jorel.commandapi.arguments.Argument<String>()
            other.literalArgument("edit") {}
            stringArgument("id") {}
        """,
            ).size,
        )
    }

    @Test
    fun `supports direct commandTree and suppression`() {
        assertEquals(
            1,
            CommandCompletionRuleFixture
                .lint(
                    """
            import dev.s7a.commandapi.*
            fun define() = commandTree("test") { literalArgument("info") {}; stringArgument("id") {} }
        """,
                ).size,
        )
        assertEquals(
            0,
            CommandCompletionRuleFixture
                .lint(
                    """
            import dev.s7a.commandapi.*
            @Suppress("NoMixedLiteralAndArgumentSiblings")
            fun define() = commandTree("test") { literalArgument("info") {}; stringArgument("id") {} }
        """,
                ).size,
        )
    }

    private fun lint(body: String) =
        CommandCompletionRuleFixture.lint(
            """
        package test
        import fixture.presentation.command.Command
        import dev.s7a.commandapi.*
        class Parent
        class TestCommand : Command.Child<Parent, TestCommand>(Parent::class, "test", { $body })
    """,
        )
}
