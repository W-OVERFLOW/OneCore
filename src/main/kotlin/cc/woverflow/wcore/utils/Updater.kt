package cc.woverflow.wcore.utils

import cc.woverflow.wcore.WCore
import cc.woverflow.wcore.utils.Updater.addToUpdater
import cc.woverflow.wcore.config.WCoreConfig
import gg.essential.api.EssentialAPI
import gg.essential.api.gui.buildConfirmationModal
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.dsl.childOf
import gg.essential.universal.UDesktop
import net.minecraftforge.fml.common.versioning.DefaultArtifactVersion
import java.io.File
import java.io.IOException
import java.util.*

/**
 * The updater, used for W-OVERFLOW mods.
 * To add a mod, use [addToUpdater].
 */
object Updater {

    private val mods: ArrayList<Mod> = arrayListOf()

    private val modsToRemove: ArrayList<Mod> = arrayListOf()

    fun addToUpdater(modFile: File, name: String, version: String, repo: String) {
        mods.add(Mod(modFile, name, version, repo))
    }

    fun update() {
        Multithreading.runAsync {
            for (mod in mods) {
                val latestRelease = WebUtil.fetchJson("https://api.github.com/repos/${mod.repo}/releases/latest")
                val latestTag = latestRelease["tag_name"].asString
                if (mod.isOutdated && WCoreConfig.showUpdateNotifications) {
                    EssentialAPI.getNotifications()
                        .push("W-CORE", "${mod.name} $latestTag is available!\nClick to open!", 5f) {
                            mod.handleUpdate()
                        }
                }
            }
            EssentialAPI.getShutdownHookUtil().register {
                for (mod in modsToRemove) {
                    println("Deleting old ${mod.name} jar file...")
                    try {
                        val runtime = getJavaRuntime()
                        if (System.getProperty("os.name").lowercase(Locale.ENGLISH).contains("mac")) {
                            val sipStatus = Runtime.getRuntime().exec("csrutil status")
                            sipStatus.waitFor()
                            if (!sipStatus.inputStream.use { it.bufferedReader().readText() }
                                    .contains("System Integrity Protection status: disabled.")) {
                                println("SIP is NOT disabled, opening Finder.")
                                UDesktop.open(mod.modFile.parentFile)
                            }
                        }
                        println("Using runtime $runtime")
                        val file = File(WCore.configFile, "Deleter-1.3.jar")
                        println("\"$runtime\" -jar \"${file.absolutePath}\" \"${mod.modFile.absolutePath}\"")
                        if (System.getProperty("os.name").lowercase(Locale.ENGLISH).containsAny("linux", "unix")) {
                            println("On Linux, giving Deleter jar execute permissions...")
                            Runtime.getRuntime()
                                .exec("chmod +x \"${file.absolutePath}\"")
                        }
                        Runtime.getRuntime()
                            .exec("\"$runtime\" -jar \"${file.absolutePath}\" \"${mod.modFile.absolutePath}\"")
                    } catch (e: Throwable) {
                        e.printStackTrace()
                    }
                }
            }
        }
    }

    /**
     * Gets the current Java runtime being used.
     * @link https://stackoverflow.com/a/47925649
     */
    @Throws(IOException::class)
    fun getJavaRuntime(): String {
        val os = System.getProperty("os.name")
        val java = "${System.getProperty("java.home")}${File.separator}bin${File.separator}${
            if (os != null && os.lowercase().startsWith("windows")) "java.exe" else "java"
        }"
        if (!File(java).isFile) {
            throw IOException("Unable to find suitable java runtime at $java")
        }
        return java
    }

    /**
     * Class which represents a mod. Used for version checking.
     */
    data class Mod(val modFile: File, val name: String, val version: String, val repo: String) {
        var isOutdated = false
            private set

        var upstreamVersion: UpdateVersion? = null

        init {
            Multithreading.runAsync {
                val latestRelease = WebUtil.fetchJson("https://api.github.com/repos/${repo}/releases/latest")
                upstreamVersion = UpdateVersion(
                    latestRelease["tag_name"].asString.substringAfter("v"),
                    latestRelease["assets"].asJsonArray[0].asJsonObject["browser_download_url"].asString
                )
                if (UpdateVersion(name) < upstreamVersion!!) {
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
                                    if (WebUtil.downloadToFile(
                                            upstreamVersion!!.url!!,
                                            File(
                                                "mods/${name.replace(" ", "-")}-${
                                                    upstreamVersion
                                                }.jar"
                                            )
                                        ) && WebUtil.downloadToFile(
                                            "https://github.com/W-OVERFLOW/Deleter/releases/download/v1.3/Deleter-1.3.jar",
                                            File(WCore.configFile, "Deleter-1.3.jar")
                                        )
                                    ) {
                                        EssentialAPI.getNotifications()
                                            .push(
                                                "W-CORE",
                                                "The ingame updater has successfully installed the newest version of $name."
                                            )
                                        isOutdated = false
                                        modsToRemove.add(this@Mod)
                                    } else {
                                        EssentialAPI.getNotifications().push(
                                            "W-CORE",
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

            private val matched by lazy {
                regex.find(version)
            }

            val isSafe = matched != null

            private val versionArtifact = DefaultArtifactVersion(matched!!.groups["version"]!!.value)
            val specialVersionType by lazy {
                val typeString = matched!!.groups["type"]?.value ?: return@lazy UpdateType.RELEASE

                return@lazy UpdateType.values().find { typeString == it.prefix } ?: UpdateType.UNKNOWN
            }
            val specialVersion by lazy {
                if (specialVersionType == UpdateType.RELEASE) return@lazy null
                return@lazy matched!!.groups["typever"]?.value?.toDoubleOrNull()
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
            UNKNOWN("unknown"),
            RELEASE(""),
            RELEASECANDIDATE("rc"),
            BETA("beta"),
        }
    }
}