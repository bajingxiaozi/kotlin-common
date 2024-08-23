pluginManagement {
    plugins {
        repositories {
            google()
            gradlePluginPortal()
            mavenCentral()
        }

        kotlin("jvm").version(extra["kotlin.version"] as String)
    }

}

rootProject.name = "kotlin-common"
include("common")

