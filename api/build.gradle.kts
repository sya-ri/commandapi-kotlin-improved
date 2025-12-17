import com.vanniktech.maven.publish.JavadocJar
import com.vanniktech.maven.publish.KotlinJvm

plugins {
    alias(libs.plugins.dokka)
    alias(libs.plugins.dokka.javadoc)
    alias(libs.plugins.maven.publish)
}

subprojects {
    afterEvaluate {
        apply(plugin = "org.jetbrains.dokka")
        apply(plugin = "org.jetbrains.dokka-javadoc")
        apply(plugin = "com.vanniktech.maven.publish")

        version = "1.3.0"

        mavenPublishing {
            publishToMavenCentral()
            signAllPublications()
            coordinates("dev.s7a", "commandapi-${project.name}-kotlin-improved", version.toString())
            configure(
                KotlinJvm(
                    javadocJar = JavadocJar.Dokka("dokkaGeneratePublicationJavadoc"),
                    sourcesJar = true,
                ),
            )
            pom {
                name = "commandapi-${project.name}-kotlin-improved"
                description = "Improve CommandAPI Kotlin DSL"
                inceptionYear = "2024"
                url = "https://github.com/sya-ri/commandapi-kotlin-improved"
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
