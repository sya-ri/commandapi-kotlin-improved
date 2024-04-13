import groovy.lang.Closure

plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
    alias(libs.plugins.git.version)
}

val gitVersion: Closure<String> by extra

group = "dev.s7a"
version = "1.0-SNAPSHOT"

allprojects {
    repositories {
        mavenCentral()
        maven(url = "https://papermc.io/repo/repository/maven-public/")
    }
}

subprojects {
    apply(plugin = "kotlin")

    afterEvaluate {
        apply(plugin = libs.plugins.kotlinter.get().pluginId)
        apply(plugin = libs.plugins.git.version.get().pluginId)
    }
}
