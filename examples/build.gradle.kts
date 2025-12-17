import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask.JarUrl
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.plugin.yml.bukkit) apply false
    alias(libs.plugins.minecraft.server) apply false
}

subprojects {
    afterEvaluate {
        apply(
            plugin =
                libs.plugins.shadow
                    .get()
                    .pluginId,
        )
        apply(
            plugin =
                libs.plugins.plugin.yml.bukkit
                    .get()
                    .pluginId,
        )
        apply(
            plugin =
                libs.plugins.minecraft.server
                    .get()
                    .pluginId,
        )

        dependencies {
            compileOnly(libs.paper.api)
        }

        configure<BukkitPluginDescription> {
            main = "dev.s7a.commandapi.example.ExamplePlugin"
            version = rootProject.version.toString()
            apiVersion = "1.20"
            author = "sya_ri"
        }

        tasks.withType<ShadowJar> {
            archiveClassifier.set("")
        }
    }

    tasks.named("build") {
        dependsOn("shadowJar")
    }

    listOf(
        "20_1" to "1.20.1",
        "20_2" to "1.20.2",
        "20_4" to "1.20.4",
        "20_6" to "1.20.6",
        "21" to "1.21",
        "21_1" to "1.21.1",
        "21_3" to "1.21.3",
        "21_4" to "1.21.4",
        "21_5" to "1.21.5",
        "21_6" to "1.21.6",
        "21_7" to "1.21.7",
        "21_8" to "1.21.8",
        "21_10" to "1.21.10",
        "21_11" to "1.21.11",
    ).forEach { (name, version) ->
        tasks.register<LaunchMinecraftServerTask>("buildAndLaunchServer$name") {
            dependsOn("build")
            doFirst {
                copy {
                    from(
                        layout.buildDirectory.asFile
                            .get()
                            .resolve("libs/${project.name}.jar"),
                    )
                    into(
                        layout.buildDirectory.asFile
                            .get()
                            .resolve("MinecraftServer$name/plugins"),
                    )
                }
            }

            jarUrl.set(JarUrl.Paper(version))
            jarName.set("server.jar")
            serverDirectory.set(
                layout.buildDirectory.asFile
                    .get()
                    .resolve("MinecraftServer$name")
                    .absolutePath,
            )
            nogui.set(true)
            agreeEula.set(true)
        }
    }
}
