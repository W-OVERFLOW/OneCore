import gg.essential.gradle.util.noServerRunConfigs
import gg.essential.gradle.util.setJvmDefault
import com.github.jengelman.gradle.plugins.shadow.tasks.ShadowJar
import gg.essential.gradle.util.RelocationTransform.Companion.registerRelocationAttribute
import gg.essential.gradle.util.prebundle

plugins {
    kotlin("jvm")
    id("gg.essential.multi-version")
    id("gg.essential.defaults.repo")
    id("gg.essential.defaults.java")
    id("gg.essential.defaults.loom")
    id("com.github.johnrengelman.shadow")
    id("net.kyori.blossom")
    `maven-publish`
}

val mod_name: String by project
val mod_version: String by project
val mod_id: String by project

blossom {
    replaceToken("@VER@", mod_version)
    replaceToken("@NAME@", mod_name)
    replaceToken("@ID@", mod_id)
}

preprocess {
    vars.put("MODERN", if (project.platform.mcMinor >= 16) 1 else 0)
    vars.put("MODERNFORGE", if (project.platform.mcMinor >= 16 && project.platform.isForge) 1 else 0)
}

version = mod_version
group = "cc.woverflow"
base {
    archivesName.set("${mod_name}-$platform")
}

tasks.compileKotlin.setJvmDefault(if (platform.mcVersion >= 11400) "all" else "all-compatibility")
loom.noServerRunConfigs()
java {
    withSourcesJar()
    withJavadocJar()
}

blossom {
    val className = "src/main/kotlin/cc/woverflow/onecore/OneCore.kt"
    replaceToken("@VER@", mod_version, className)
    replaceToken("@NAME@", mod_name, className)
    replaceToken("@ID@", mod_id, className)
}

loom {
    launchConfigs {
        launchConfigs.named("client") {
            if (project.platform.isLegacyForge) {
                arg("--tweakClass", "gg.essential.loader.stage0.EssentialSetupTweaker")
            }
            arg("--mixin", "mixins.onecore.json")
        }
    }
    mixin.defaultRefmapName.set("mixins.onecore.refmap.json")
}

repositories {
    maven("https://repo.woverflow.cc/")
}

val shade: Configuration by configurations.creating {
    configurations.api.get().extendsFrom(this)
}

val shadeMod: Configuration by configurations.creating {
    configurations.modApi.get().extendsFrom(this)
}

val relocated = registerRelocationAttribute("relocate-commons-codec") {
    relocate("org.apache.commons.codec", "org.apache.hc.client5.libs.codec")
}

val commonsCodec by configurations.creating {
    attributes { attribute(relocated, true) }
}

dependencies {
    val elementaVersion: String by project
    val dom4jVersion: String by project
    val vigilanceVersion: String by project
    val nightconfigVersion: String by project
    val universalCraftVersion: String by project
    if (platform.isForge) {
        if (platform.mcMinor < 16) {
            val essential = "gg.essential:essential-$platform:1933"
            api(essential) {
                exclude(module = "keventbus")
            }
            compileOnly(essential) {
                exclude(module = "keventbus")
            }
            runtimeOnly ("gg.essential:loader-launchwrapper:1.1.3") {
                isTransitive = false
            }
            runtimeOnly("me.djtheredstoner:DevAuth-forge-legacy:1.0.0")
        } else {
            shade(("org.jetbrains.kotlin:kotlin-stdlib:1.6.10"))
            shade(("org.jetbrains.kotlin:kotlin-stdlib-jdk7:1.6.10"))
            shade(("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.6.10"))
            shade(("org.jetbrains.kotlin:kotlin-reflect:1.6.10"))
            shade(("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.0"))
            shade(("org.jetbrains.kotlinx:kotlinx-coroutines-core-jvm:1.6.0"))
            shade(("org.jetbrains.kotlinx:kotlinx-coroutines-jdk8:1.6.0"))
            shade(("org.jetbrains.kotlinx:kotlinx-serialization-core-jvm:1.3.2"))
            shade(("org.jetbrains.kotlinx:kotlinx-serialization-json-jvm:1.3.2"))
            shade(("org.jetbrains.kotlinx:kotlinx-serialization-cbor-jvm:1.3.2"))
            shadeMod(("gg.essential:elementa-$platform:${elementaVersion}"))

            shadeMod(("gg.essential:vigilance-$platform:${vigilanceVersion}"))
            shade("com.electronwill.night-config:core:$nightconfigVersion")
            shade("com.electronwill.night-config:toml:$nightconfigVersion")

            shadeMod(("gg.essential:universalcraft-$platform:${universalCraftVersion}"))
        }
    } else {
        val fabricApiVersion: String by project
        val fabricLanguageKotlinVersion: String by project
        val modMenuVersion: String by project
        modImplementation ("net.fabricmc.fabric-api:fabric-api:$fabricApiVersion")
        modImplementation("net.fabricmc:fabric-language-kotlin:$fabricLanguageKotlinVersion")
        modImplementation("com.terraformersmc:modmenu:$modMenuVersion")

        shadeMod(("gg.essential:elementa-$platform:${elementaVersion}"))

        shadeMod(("gg.essential:vigilance-$platform:${vigilanceVersion}"))
        shade("com.electronwill.night-config:toml:$nightconfigVersion")

        shadeMod(("gg.essential:universalcraft-$platform:${universalCraftVersion}"))
    }

    shade("com.github.Wyvest:keventbus:e8e05ea")
    compileOnly("org.spongepowered:mixin:0.8.5-SNAPSHOT")
    commonsCodec("org.apache.httpcomponents.client5:httpclient5:5.1.3") {
        exclude(module = "commons-codec")
    }
    commonsCodec("commons-codec:commons-codec:1.15")
    shade(prebundle(commonsCodec))
    shade("org.java-websocket:Java-WebSocket:1.5.2")
}

tasks {

    processResources {
        // this will ensure that this task is redone when the versions change.
        inputs.property ("id", mod_id)
        inputs.property ("version", mod_version)
        val java = if (project.platform.mcMinor > 18) {
            17
        } else {if (project.platform.mcMinor >= 17) 16 else 8 }
        val compatLevel = "JAVA_${java}"
        inputs.property ("java", java)
        inputs.property ("java_level", compatLevel)
        inputs.property ("mcversion", platform.mcVersionStr)

        filesMatching("mixins.${mod_id}.json") {
            expand(mapOf(
                "java_level" to compatLevel,
                "id" to mod_id
            ))
        }
        filesMatching("META-INF/mods.toml") {
            expand(mapOf(
                "version" to mod_version
            ))
        }
        filesMatching("fabric.mod.json") {
            expand(mapOf(
                "version" to mod_version,
                "java" to java,
                "mcversion" to platform.mcVersionStr.substring(0, platform.mcVersionStr.length - 1) + "x" //if minecraft decides to have another 2 digit patch number we need to replace this with something better
            ))
        }
    }

    remapJar {
        archiveClassifier.set("nodeps")
        if (project.platform.isForge) {
            exclude("fabric.mod.json", "**/module-info.class")
        }
    }
    jar {
        archiveClassifier.set("deobf-nodeps")
        manifest.attributes(mapOf(
            "ModSide" to "CLIENT",
            "TweakOrder" to "0",
            "MixinConfigs" to "mixins.onecore.json",
            "ForceLoadAsMod" to true
        ))
        if (project.platform.isForge) {
            exclude("fabric.mod.json", "**/module-info.class") //todo: fix this, for some reason it doesnt actually exclude it
        }
    }
    named<Jar>("sourcesJar") {
        exclude("cc/woverflow/onecore/internal/**")
        exclude("cc/woverflow/onecore/OneCore.**")
        exclude("mixins.onecore.json")
        exclude("fabric.mod.json")
    }
    named<Jar>("javadocJar") {
        exclude("cc/woverflow/onecore/internal/**")
        exclude("cc/woverflow/onecore/OneCore.**")
        exclude("mixins.onecore.json")
        exclude("fabric.mod.json")
    }
    val deobfShadowJar by registering(ShadowJar::class) {
        archiveClassifier.set("deobf")
        jar.orNull?.let { from(it.archiveFile) }
        configurations = listOf(shade, shadeMod)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        if (project.platform.isForge) {
            exclude("fabric.mod.json", "**/module-info.class") //todo: fix this, for some reason it doesnt actually exclude it
        }
    }
    shadowJar {
        archiveClassifier.set("")
        remapJar.orNull?.let { from(it.archiveFile) }
        configurations = listOf(shade, shadeMod)
        duplicatesStrategy = DuplicatesStrategy.EXCLUDE
        if (project.platform.isForge) {
            exclude("fabric.mod.json", "**/module-info.class")
        }
    }
    shadowJar.orNull?.dependsOn(deobfShadowJar)
    assemble.orNull?.dependsOn(shadowJar)
}

publishing {
    publications {
        register<MavenPublication>("onecore-$platform") {
            groupId = "cc.woverflow"
            artifactId = "onecore-$platform"

            from(components["java"])
            artifact(tasks["deobfShadowJar"])
        }
    }

    repositories {
        if (hasProperty("woverflow.token")) {
            logger.log(LogLevel.INFO, "Publishing to W-OVERFLOW")
            maven(url = "https://repo.woverflow.cc/") {
                credentials {
                    username = "wyvest"
                    password = property("woverflow.token") as? String
                }
            }
        }
    }
}