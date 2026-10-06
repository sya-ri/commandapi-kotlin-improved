pluginManagement {
    repositories {
        gradlePluginPortal()
        mavenCentral()
    }
}

dependencyResolutionManagement {
    repositories {
        mavenCentral()
        maven("https://repo.papermc.io/repository/maven-public/")
    }

    versionCatalogs {
        create("hostLibs") {
            from(files("../../gradle/libs.versions.toml"))
        }
    }
}

rootProject.name = "commandapi-detekt-rules"
