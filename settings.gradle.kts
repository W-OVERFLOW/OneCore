pluginManagement {
    repositories {
        maven("https://repo.polyfrost.cc/releases")
        // adding essential repo until i finish mirroing it
        maven("https://repo.essential.gg/repository/maven-public")
        maven("https://maven.architectury.dev/")
        flatDir {
            dirs=setOf(file("../../libs"))
        }
    }
    plugins {
        val egtVersion = "0.1.6"
        id("gg.essential.multi-version.root") version egtVersion
    }
}

val mod_name: String by settings

rootProject.name = mod_name

rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.8.9-forge",
    "1.12.2-forge",
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle.kts"
    }

}