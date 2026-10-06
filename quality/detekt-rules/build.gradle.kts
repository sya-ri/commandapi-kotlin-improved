plugins {
    alias(libs.plugins.kotlin.jvm)
    alias(libs.plugins.kotlinter)
}

group = "dev.s7a.commandapi"
version = "1.0.0"

kotlin {
    jvmToolchain(25)
}

dependencies {
    compileOnly(libs.detekt.api)
    testImplementation(kotlin("test-junit5"))
    testImplementation(libs.detekt.core)
    testImplementation(libs.detekt.test.utils)
    testImplementation(libs.kotest.runner)
    testImplementation(libs.kotest.assertions)
    testRuntimeOnly(libs.junit.platform.launcher)
    testImplementation(gradleTestKit())
}

tasks.jar {
    archiveFileName.set("commandapi-detekt.jar")
    isPreserveFileTimestamps = false
    isReproducibleFileOrder = true
}

tasks.test {
    useJUnitPlatform()
    maxHeapSize = "2g"
    dependsOn(tasks.jar)
    systemProperty("rules.kotlin.version", libs.versions.kotlin.get())
    systemProperty("rules.detekt.version", libs.versions.detekt.get())
    systemProperty("rules.commandapi.version", hostLibs.versions.commandapi.asProvider().get())
    systemProperty("rules.paper.version", hostLibs.versions.paper.get())
    systemProperty("rules.improved.version", hostLibs.versions.commandapi.improved.get())

    doFirst {
        systemProperty("rules.jar", tasks.jar.get().archiveFile.get().asFile.absolutePath)
    }
}

val bundledJar = layout.projectDirectory.file("../../skills/commandapi-kotlin-improved/assets/commandapi-detekt.jar")

val verifyBundledJar = tasks.register("verifyBundledJar") {
    mustRunAfter("updateBundledJar")
    dependsOn(tasks.jar)
    inputs.file(tasks.jar.flatMap {
        it.archiveFile
    })
    inputs.file(bundledJar)

    doLast {
        check(bundledJar.asFile.isFile) {
            "Build the bundled JAR with mise run commandapi:update-jar."
        }
        check(tasks.jar.get().archiveFile.get().asFile.readBytes().contentEquals(bundledJar.asFile.readBytes())) {
            "The bundled JAR is stale. Run mise run commandapi:update-jar."
        }
    }
}

tasks.register<Copy>("updateBundledJar") {
    dependsOn(tasks.named("check"), tasks.jar)
    from(tasks.jar.flatMap {
        it.archiveFile
    })
    into(bundledJar.asFile.parentFile)
}

val validateSkill = tasks.register("validateSkill") {
    val directory = layout.projectDirectory.dir("../../skills/commandapi-kotlin-improved")
    inputs.dir(directory)

    doLast {
        val skill = directory.file("SKILL.md").asFile.readText()
        check(skill.startsWith("---\n") || skill.startsWith("---\r\n")) {
            "Missing skill frontmatter."
        }
        directory.asFile.walkTopDown().filter {
            it.isFile && it.extension == "md"
        }.forEach { document ->
            val text = document.readText()
            check(!text.contains("[TODO") && !text.contains("[INSERT")) {
                "Unfinished skill reference: $document"
            }
            Regex("\\[[^]]*]\\(([^)]+)\\)").findAll(text).forEach { link ->
                val destination = link.groupValues[1]
                if (!destination.contains("://") && !destination.startsWith("#")) {
                    check(document.parentFile.resolve(destination.substringBefore('#')).exists()) {
                        "Missing skill reference: $destination"
                    }
                }
            }
        }
    }
}

tasks.named("check") {
    dependsOn(validateSkill)
}

