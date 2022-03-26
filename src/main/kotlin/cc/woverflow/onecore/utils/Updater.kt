package cc.woverflow.onecore.utils

import cc.woverflow.onecore.OneCore
import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.utils.Updater.addToUpdater
import gg.essential.api.EssentialAPI
import gg.essential.api.gui.buildConfirmationModal
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UDesktop
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import java.io.File
import java.util.*

/**
 * The updater, used for W-OVERFLOW mods.
 * To add a mod, use [addToUpdater].
 */
object Updater {

    private val mods: ArrayList<Mod> = arrayListOf()

    private val modsToRemove: ArrayList<Mod> = arrayListOf()

    /**
     * Adds the mod specified. For forge mods, it is recommended to run this method during the [FMLPreInitializationEvent] event to easily access the file the mod is running on.
     * The repo of the mod will automatically be inferred as "W-OVERFLOW/ID_OF_MOD".
     * @param modFile The file of the mod.
     * @param mod The main class of the mod (needs to be annotated with [net.minecraftforge.fml.common.Mod]
     */
    fun addToUpdater(modFile: File, mod: Any) {
        if (mod.javaClass.isAnnotationPresent(net.minecraftforge.fml.common.Mod::class.java)) {
            val annotation = mod.javaClass.getDeclaredAnnotation(net.minecraftforge.fml.common.Mod::class.java)
            mods.add(Mod(modFile, annotation.name, annotation.modid, annotation.version, "W-OVERFLOW/${annotation.modid}"))
        }
    }

    /**
     * Adds the mod specified. For forge mods, it is recommended to run this method during the [FMLPreInitializationEvent] event to easily access the file the mod is running on.
     * @param modFile The file of the mod.
     * @param mod The main class of the mod (needs to be annotated with [net.minecraftforge.fml.common.Mod]
     * @param repo The GitHub repository to check for updates.
     */
    fun addToUpdater(modFile: File, mod: Any, repo: String) {
        if (mod.javaClass.isAnnotationPresent(net.minecraftforge.fml.common.Mod::class.java)) {
            val annotation = mod.javaClass.getDeclaredAnnotation(net.minecraftforge.fml.common.Mod::class.java)
            mods.add(Mod(modFile, annotation.name, annotation.modid, annotation.version, repo))
        }
    }

    /**
     * Adds the mod specified. For forge mods, it is recommended to run this method during the [FMLPreInitializationEvent] event to easily access the file the mod is running on.
     * @param modFile The file of the mod.
     * @param name The name of the mod, visual only.
     * @param id The ID of the mod.
     * @param version The version of the mod.
     * @param repo The GitHub repository to check for updates.
     */
    fun addToUpdater(modFile: File, name: String, id: String, version: String, repo: String) {
        mods.add(Mod(modFile, name, id, version, repo))
    }

    internal fun update() {
        Multithreading.runAsync {
            for (mod in mods) {
                if (mod.isOutdated && OneCoreConfig.showUpdateNotifications) {
                    sendBrandedNotification(
                        "OneCore",
                        "${mod.name} ${mod.upstreamVersion?.version} is available!\nClick to open!",
                        20f,
                        action = {
                            mod.handleUpdate()
                        })
                }
            }
            Runtime.getRuntime().addShutdownHook(Thread {
                if (modsToRemove.isNotEmpty()) {
                    try {
                        if (System.getProperty("os.name").lowercase(Locale.ENGLISH).contains("mac")) {
                            val sipStatus = Runtime.getRuntime().exec("csrutil status")
                            sipStatus.waitFor()
                            if (!sipStatus.inputStream.use { it.bufferedReader().readText() }
                                    .contains("System Integrity Protection status: disabled.")) {
                                UDesktop.open(modsToRemove.first().modFile.parentFile)
                            }
                        }
                        val file = File(OneCore.configFile, "Deleter-1.3.jar")
                        if (UDesktop.isLinux) {
                            Runtime.getRuntime().exec("chmod +x \"${file.absolutePath}\"")
                        } else if (UDesktop.isMac) {
                            Runtime.getRuntime().exec("chmod 755 \"${file.absolutePath}\"")
                        }
                        Runtime.getRuntime()
                            .exec("java -jar ${file.name} ${modsToRemove.joinToString(" ") {it.modFile.absolutePath}}", null, file.parentFile)
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            })
        }
    }

    /**
     * Class which represents a mod. Used for version checking.
     */
    internal data class Mod(
        val modFile: File, val name: String, val id: String, val version: String, val repo: String
    ) {
        var isOutdated = false
            private set

        var upstreamVersion: UpdateVersion? = null

        init {
            Multithreading.runAsync {
                val latestRelease = WebUtil.fetchJsonElement("https://api.github.com/repos/${repo}/releases/latest").asJsonObject
                upstreamVersion = UpdateVersion(
                    latestRelease["tag_name"].asString.substringAfter("v"),
                    latestRelease["assets"].asJsonArray[0].asJsonObject["browser_download_url"].asString
                )
                if (UpdateVersion(version) < upstreamVersion!!) {
                    isOutdated = true
                }
            }
        }

        fun handleUpdate() {
            EssentialAPI.getGuiUtil()
                .openScreen(object : WindowScreen(restoreCurrentGuiOnClose = true, version = ElementaVersion.V1) {


                    override fun initScreen(width: Int, height: Int) {
                        super.initScreen(width, height)
                        EssentialAPI.getEssentialComponentFactory().buildConfirmationModal {
                            this.text = "Are you sure you want to update $name?"
                            this.secondaryText = "(This will update from v$version to ${upstreamVersion?.version})"
                            this.onConfirm = {
                                restorePreviousScreen()
                                Multithreading.runAsync {
                                    if (WebUtil.downloadToFileSafe(
                                            upstreamVersion!!.url!!, File(
                                                modFile.parentFile, "${name.replace(" ", "-")}-${
                                                    upstreamVersion?.version
                                                }.jar"
                                            )
                                        ) && WebUtil.downloadToFileSafe(
                                            "https://github.com/W-OVERFLOW/Deleter/releases/download/v1.3/Deleter-1.3.jar",
                                            File(OneCore.configFile, "Deleter-1.3.jar")
                                        )
                                    ) {
                                        sendBrandedNotification(
                                            "OneCore",
                                            "The ingame updater has successfully installed the newest version of $name."
                                        )
                                        isOutdated = false
                                        modsToRemove.add(this@Mod)
                                    } else {
                                        sendBrandedNotification(
                                            "OneCore",
                                            "The ingame updater has NOT installed the newest version of $name as something went wrong."
                                        )
                                    }
                                }
                            }
                            this.onDeny = {
                                restorePreviousScreen()
                            }
                        } childOf this.window
                    }
                })
        }

        companion object {
            val regex = Regex("^(?<version>[\\d.]+)-?(?<type>\\D+)?(?<typever>\\d+\\.?\\d*)?$")
        }

        /**
         * Adapted from SimpleTimeChanger under AGPLv3
         * https://github.com/My-Name-Is-Jeff/SimpleTimeChanger/blob/master/LICENSE
         */
        inner class UpdateVersion(val version: String, val url: String? = null) : Comparable<UpdateVersion> {

            private val matched = regex.find(version)

            val isSafe = matched != null

            private val versionArtifact = DefaultArtifactVersion(matched!!.groups["version"]!!.value)
            val specialVersionType = run {
                val typeString = matched!!.groups["type"]?.value ?: return@run UpdateType.RELEASE

                return@run UpdateType.values().find { typeString == it.prefix } ?: UpdateType.UNKNOWN
            }
            val specialVersion = run {
                if (specialVersionType == UpdateType.RELEASE) return@run null
                return@run matched!!.groups["typever"]?.value?.toDoubleOrNull()
            }

            override fun compareTo(other: UpdateVersion): Int {
                if (!isSafe || !other.isSafe) return -1
                return if (versionArtifact.compareTo(other.versionArtifact) == 0) {
                    if (specialVersionType.ordinal == other.specialVersionType.ordinal) {
                        (specialVersion ?: 0.0).compareTo(other.specialVersion ?: 0.0)
                    } else other.specialVersionType.ordinal - specialVersionType.ordinal
                } else versionArtifact.compareTo(other.versionArtifact)
            }
        }

        enum class UpdateType(val prefix: String) {
            RELEASE(""), PRERELEASE("pre"), BETA("beta"), ALPHA("alpha"), UNKNOWN("unknown")
        }
    }
}