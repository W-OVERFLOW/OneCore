pluginManagement {
    repositories {
        maven("https://repo.woverflow.cc/")
        flatDir {
            dirs=setOf(file("../../libs"))
        }
    }
    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.replaymod.preprocess" -> {
                    useModule("com.github.replaymod:preprocessor:${requested.version}")
                }
            }
        }
    }
}

rootProject.buildFileName = "root.gradle.kts"

listOf(
    "1.8.9",
    "1.12.2",
    "1.18.1-forge",
    "1.18.1-fabric"
).forEach { version ->
    include(":$version")
    project(":$version").apply {
        projectDir = file("versions/$version")
        buildFileName = "../../build.gradle"
    }

}