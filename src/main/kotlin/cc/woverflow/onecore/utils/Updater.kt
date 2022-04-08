package cc.woverflow.onecore.utils

//#if MODERN==0
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
//#endif
import cc.woverflow.onecore.OneCore
import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.files.StupidFileHack
import cc.woverflow.onecore.utils.Updater.addToUpdater
import cc.woverflow.onecore.utils.gui.ConfirmationGui
import gg.essential.universal.ChatColor
import gg.essential.universal.UDesktop
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.*

/**
 * The updater, used for W-OVERFLOW mods.
 * To add a mod, use [addToUpdater].
 */
object Updater {

    private val mods: ArrayList<Mod> = arrayListOf()

    private val modsToRemove: ArrayList<Mod> = arrayListOf()

    //#if MODERN==0
    /**
     * Adds the mod specified. For forge mods, it is recommended to run this method during the [FMLPreInitializationEvent] event to easily access the file the mod is running on.
     * The repo of the mod will automatically be inferred as "W-OVERFLOW/ID_OF_MOD".
     * @param modFile The file of the mod.
     * @param mod The main class of the mod (needs to be annotated with [net.minecraftforge.fml.common.Mod]
     */
    fun addToUpdater(modFile: File, mod: Any) {
        if (mod.javaClass.isAnnotationPresent(net.minecraftforge.fml.common.Mod::class.java)) {
            val annotation = mod.javaClass.getDeclaredAnnotation(net.minecraftforge.fml.common.Mod::class.java)
            mods.add(
                Mod(
                    modFile,
                    annotation.name,
                    annotation.modid,
                    annotation.version,
                    "W-OVERFLOW/${annotation.modid}"
                )
            )
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

    //#endif

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
        launchCoroutine {
            @Suppress("SimplifyBooleanWithConstants")
            while (
                //#if MODERN==0
                false
                //#else
                    //#if FABRIC==1
                    //$$ net.minecraft.client.MinecraftClient.getInstance().world == null
                    //#else
                    //$$ net.minecraft.client.Minecraft.getInstance().level == null
                    //#endif
                //#endif
            ) { withContext(Dispatchers.IO) {
                Thread.sleep(2000)
            } }
            while (!mods.all { it.finishedProcessing }) {
                withContext(Dispatchers.IO) {
                    Thread.sleep(2000)
                }
            }
            for (mod in mods) {
                if (mod.isOutdated && OneCoreConfig.showUpdateNotifications) {
                    sendBrandedNotification("OneCore",
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
                        val file = StupidFileHack.getFileFrom(OneCore.configFile, "Deleter-1.3.jar")
                        if (UDesktop.isLinux) {
                            Runtime.getRuntime().exec("chmod +x \"${file.absolutePath}\"")
                        } else if (UDesktop.isMac) {
                            Runtime.getRuntime().exec("chmod 755 \"${file.absolutePath}\"")
                        }
                        Runtime.getRuntime().exec(
                            "java -jar ${file.name} ${modsToRemove.joinToString(" ") { it.modFile.absolutePath }}",
                            null,
                            file.parentFile
                        )
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

        var finishedProcessing = false

        var upstreamVersion: UpdateVersion? = null

        init {
            try {
                launchCoroutine {
                    APIUtil.getJsonElement("https://api.github.com/repos/${repo}/releases/latest")?.asJsonObject?.let { latestRelease ->
                        upstreamVersion = UpdateVersion(
                            latestRelease["tag_name"].asString.substringAfter("v"),
                            latestRelease["assets"].asJsonArray[0].asJsonObject["browser_download_url"].asString
                        )
                        if (UpdateVersion(version) < upstreamVersion!!) {
                            isOutdated = true
                        }
                    }
                    finishedProcessing = true
                }
            } catch (e: Exception) {
                e.printStackTrace()
                finishedProcessing = true
            }
        }

        fun handleUpdate() {
            ConfirmationGui(
                "${ChatColor.AQUA}OneCore",
                "${ChatColor.RED}Are you sure you want to update $name?",
                "${ChatColor.YELLOW}(This will update from v$version to ${upstreamVersion?.version})",
                onConfirm = {
                    restorePreviousScreen()
                            launchCoroutine {
                                if (APIUtil.download(
                                        upstreamVersion!!.url!!, StupidFileHack.getFileFrom(modFile.parentFile, "${name.replace(" ", "-")}-${
                                            upstreamVersion?.version
                                        }.jar")
                                    ) && APIUtil.download(
                                        "https://github.com/W-OVERFLOW/Deleter/releases/download/v1.3/Deleter-1.3.jar",
                                        StupidFileHack.getFileFrom(OneCore.configFile, "Deleter-1.3.jar")
                                    )
                                ) {
                                    sendBrandedNotification(
                                        "OneCore", "The ingame updater has successfully installed the newest version of $name."
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
                },
                onDeny = { restorePreviousScreen() }).openScreen()
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

            val versionArtifact = Version.fromString(matched!!.groups["version"]!!.value) ?: throw NullPointerException()
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

        class Version(
            private val major: Int, private val minor: Int = 0, private val patch: Int = 0
        ) {
            operator fun compareTo(other: Version): Int {
                var result = major - other.major
                if (result == 0) {
                    result = minor - other.minor
                    if (result == 0) {
                        result = patch - other.patch
                    }
                }

                return result
            }

            override fun equals(other: Any?): Boolean {
                if (this === other) return true
                if (other !is Version) return false

                return compareTo(other) == 0
            }

            override fun toString(): String = "$major.$minor.$patch"

            override fun hashCode(): Int {
                var result = major
                result = 31 * result + minor
                result = 31 * result + patch
                return result
            }

            companion object {
                val regex = Regex("(?<major>\\d+)(?:\\.(?<minor>\\d+)(?:\\.(?<patch>\\d+)?)?)?")

                fun fromString(version: String): Version? {
                    val match = regex.find(version)
                    return if (match != null) {
                        Version(
                            match.groups["major"]!!.value.toInt(),
                            match.groups["minor"]?.value?.toInt() ?: 0,
                            match.groups["patch"]?.value?.toInt() ?: 0
                        )
                    } else {
                        null
                    }
                }
            }
        }

        enum class UpdateType(val prefix: String) {
            RELEASE(""), PRERELEASE("pre"), BETA("beta"), ALPHA("alpha"), UNKNOWN("unknown")
        }
    }
}