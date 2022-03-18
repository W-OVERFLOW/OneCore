package cc.woverflow.onecore

import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.utils.Updater
import cc.woverflow.onecore.utils.command
import cc.woverflow.onecore.utils.fetchJsonElement
import cc.woverflow.onecore.utils.openScreen
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import net.minecraft.client.Minecraft
import net.minecraft.launchwrapper.Launch
import java.io.File


object OneCore {

    private var init = false

    const val NAME = "@NAME@"
    const val ID = "@ID@"
    const val VERSION = "@VER@"

    val configFile = File(File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), "OneCore")

    fun init() {
        if (!init) {
            init = true
            if (!configFile.exists()) {
                configFile.mkdirs()
            }
            OneCoreConfig.preload()
            command("onecore") {
                main {
                    OneCoreConfig.openScreen()
                }
            }
            Multithreading.runAsync {
                UniqueUsersMetric.putApi()
            }
            Updater.update()
        }
    }

    private object UniqueUsersMetric {
        fun putApi() {
            try {
                if (!(Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false) as Boolean)) {
                    val url = "https://api.isxander.dev/metric/put/onecore?type=unique_users&uuid=${Minecraft.getMinecraft().session.profile.id}"
                    val response = WebUtil.fetchJsonElement(url).asJsonObject
                    if (!response["success"].asBoolean) {
                        println("Metric API could not be called: ${response["error"].asString}")
                        return
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}