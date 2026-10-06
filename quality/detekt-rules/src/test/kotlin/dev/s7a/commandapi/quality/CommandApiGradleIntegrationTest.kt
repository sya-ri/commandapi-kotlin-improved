package dev.s7a.commandapi.quality

import io.kotest.core.spec.style.FunSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.string.shouldContain
import org.gradle.testkit.runner.GradleRunner
import org.gradle.testkit.runner.TaskOutcome
import kotlin.io.path.createDirectories
import kotlin.io.path.createTempDirectory
import kotlin.io.path.writeText

class CommandApiGradleIntegrationTest :
    FunSpec({
        test("check uses the dedicated provider and listener with actual CommandAPI binaries") {
            val directory = createTempDirectory("commandapi-rules-fixture-")
            try {
                val kotlinVersion = System.getProperty("rules.kotlin.version")
                val detektVersion = System.getProperty("rules.detekt.version")
                val commandapiVersion = System.getProperty("rules.commandapi.version")
                val improvedVersion = System.getProperty("rules.improved.version")
                val paperVersion = System.getProperty("rules.paper.version")
                directory.resolve("settings.gradle.kts").writeText("rootProject.name = \"commandapi-fixture\"")
                directory.resolve("gradle.properties").writeText(
                    "org.gradle.jvmargs=-Xmx2g -XX:MaxMetaspaceSize=768m\nkotlin.compiler.execution.strategy=in-process\n",
                )
                directory.resolve("build.gradle.kts").writeText(
                    """
                    plugins {
                        kotlin("jvm") version "$kotlinVersion"
                        id("dev.detekt") version "$detektVersion"
                    }

                    repositories {
                        mavenCentral()
                        maven("https://repo.papermc.io/repository/maven-public/")
                    }

                    dependencies {
                        compileOnly("dev.s7a:commandapi-paper-kotlin-improved:$improvedVersion")
                        compileOnly("dev.jorel:commandapi-paper-core:$commandapiVersion")
                        compileOnly("io.papermc.paper:paper-api:$paperVersion")
                        detektPlugins(files(providers.gradleProperty("rulesJar").get()))
                    }

                    detekt {
                        config.setFrom(file("detekt.yml"))
                    }

                    tasks.named("check") {
                        dependsOn("detektMain")
                    }
                    """.trimIndent(),
                )
                directory.resolve("detekt.yml").writeText(
                    """
                    config:
                      validation: true
                      excludes: ['commandApi>.*']
                    commandApi:
                      active: true
                      NoMixedLiteralAndArgumentSiblings:
                        active: true
                    """.trimIndent(),
                )
                val source = directory.resolve("src/main/kotlin").createDirectories().resolve("Commands.kt")

                fun sourceCode(body: String) =
                    """
                    import dev.s7a.commandapi.commandTree
                    import dev.s7a.commandapi.literalArgument
                    import dev.s7a.commandapi.stringArgument

                    fun register() = commandTree("example") {
                        $body
                    }
                    """.trimIndent()

                fun runner() =
                    GradleRunner
                        .create()
                        .withProjectDir(directory.toFile())
                        .withArguments("check", "-PrulesJar=${System.getProperty("rules.jar")}", "--console=plain")

                source.writeText(sourceCode("literalArgument(\"create\") {}\nstringArgument(\"name\") {}"))
                val failed = runner().buildAndFail()
                failed.task(":detektMain")?.outcome shouldBe TaskOutcome.FAILED
                failed.output shouldContain "NoMixedLiteralAndArgumentSiblings"
                failed.output shouldContain "Literal commands and argument nodes share a completion position."

                source.writeText(sourceCode("literalArgument(\"create\") { stringArgument(\"name\") {} }"))
                val accepted = runner().build()
                accepted.task(":detektMain")?.outcome shouldBe TaskOutcome.SUCCESS
            } finally {
                directory.toFile().deleteRecursively()
            }
        }
    })
