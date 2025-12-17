import groovy.lang.Closure
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.git.version)
}

val gitVersion: Closure<String> by extra

group = "dev.s7a"
version = gitVersion()

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://repo.papermc.io/repository/maven-public/")
    }
}

subprojects {
    apply(plugin = "kotlin")

    afterEvaluate {
        apply(
            plugin =
                libs.plugins.kotlinter
                    .get()
                    .pluginId,
        )
        apply(
            plugin =
                libs.plugins.git.version
                    .get()
                    .pluginId,
        )
    }

    java {
        toolchain {
            languageVersion.set(JavaLanguageVersion.of(17))
        }
    }

    tasks.withType<KotlinCompile>().configureEach {
        kotlinOptions.jvmTarget = "17"
    }
}
