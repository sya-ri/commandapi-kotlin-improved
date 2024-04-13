plugins {
    `maven-publish`
    signing
}

subprojects {
    apply(plugin = "maven-publish")
    apply(plugin = "signing")

    version = "1.0.0-SNAPSHOT"

    val sourceJar by tasks.registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets["main"].allSource)
    }

    publishing {
        repositories {
            maven {
                url =
                    uri(
                        if (version.toString().endsWith("SNAPSHOT")) {
                            "https://s01.oss.sonatype.org/content/repositories/snapshots/"
                        } else {
                            "https://s01.oss.sonatype.org/service/local/staging/deploy/maven2/"
                        },
                    )
                credentials {
                    username = properties["sonatypeUsername"].toString()
                    password = properties["sonatypePassword"].toString()
                }
            }
        }
        publications {
            register<MavenPublication>("maven") {
                groupId = "dev.s7a"
                artifactId = "commandapi-${project.name}-kotlin-improved"
                from(components["kotlin"])
                artifact(sourceJar.get())
                pom {
                    name.set("commandapi-${project.name}-kotlin-improved")
                    description.set("Improve CommandAPI Kotlin DSL")
                    url.set("https://github.com/sya-ri/commandapi-kotlin-improved")

                    licenses {
                        license {
                            name.set("MIT License")
                            url.set("https://github.com/sya-ri/commandapi-kotlin-improved/blob/master/LICENSE")
                        }
                    }
                    developers {
                        developer {
                            id.set("sya-ri")
                            name.set("sya-ri")
                            email.set("contact@s7a.dev")
                        }
                    }
                    scm {
                        url.set("https://github.com/sya-ri/commandapi-kotlin-improved")
                    }
                }
            }
        }
    }

    signing {
        val key = properties["signingKey"]?.toString()?.replace("\\n", "\n")
        val password = properties["signingPassword"]?.toString()

        useInMemoryPgpKeys(key, password)
        sign(publishing.publications["maven"])
    }
}
