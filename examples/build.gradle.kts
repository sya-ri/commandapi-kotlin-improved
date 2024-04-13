import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask
import dev.s7a.gradle.minecraft.server.tasks.LaunchMinecraftServerTask.JarUrl
import groovy.lang.Closure
import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    alias(libs.plugins.shadow) apply false
    alias(libs.plugins.plugin.yml.bukkit) apply false
    alias(libs.plugins.minecraft.server) apply false
}

subprojects {
    afterEvaluate {
        apply(plugin = libs.plugins.shadow.get().pluginId)
        apply(plugin = libs.plugins.plugin.yml.bukkit.get().pluginId)
        apply(plugin = libs.plugins.minecraft.server.get().pluginId)

        val gitVersion: Closure<String> by extra

        dependencies {
            compileOnly(libs.paper.api)
        }

        configure<BukkitPluginDescription> {
            main = "dev.s7a.commandapi.example.ExamplePlugin"
            version = gitVersion()
            apiVersion = "1.16"
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
        "15" to "1.15.2",
        "16" to "1.16.5",
        "17" to "1.17.1",
        "18" to "1.18.2",
        "19" to "1.19.4",
        "20_1" to "1.20.1",
        "20_2" to "1.20.2",
        "20_4" to "1.20.4",
    ).forEach { (name, version) ->
        task<LaunchMinecraftServerTask>("buildAndLaunchServer$name") {
            dependsOn("build")
            doFirst {
                copy {
                    from(layout.buildDirectory.asFile.get().resolve("libs/${project.name}.jar"))
                    into(layout.buildDirectory.asFile.get().resolve("MinecraftServer$name/plugins"))
                }
            }

            jarUrl.set(JarUrl.Paper(version))
            jarName.set("server.jar")
            serverDirectory.set(layout.buildDirectory.asFile.get().resolve("MinecraftServer$name").absolutePath)
            nogui.set(true)
            agreeEula.set(true)
        }
    }
}
