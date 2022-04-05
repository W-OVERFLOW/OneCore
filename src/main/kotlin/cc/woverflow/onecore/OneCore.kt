package cc.woverflow.onecore

import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.internal.Events
import cc.woverflow.onecore.utils.*
import cc.woverflow.onecore.websocket.Client
import cc.woverflow.onecore.websocket.WebsocketUtils
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import net.minecraft.launchwrapper.Launch
import net.minecraft.util.Session
import java.io.File


object OneCore {

    private var init = false

    const val NAME = "@NAME@"
    const val ID = "@ID@"
    const val VERSION = "@VER@"

    val configFile = File(File(mc.mcDataDir, "W-OVERFLOW"), "OneCore")

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
            Events //initialize
            Multithreading.runAsync {
                UniqueUsersMetric.putApi()
            }
            Updater.update()
            Multithreading.runAsync {
                val session: Session = mc.session
                val profile = session.profile

                val uuid = profile.id.toString().replace("-", "")
                val serverHash: String = WebsocketUtils.hash(uuid + WebsocketUtils.nextSalt)
                val status = WebsocketUtils.authenticate(session.token, uuid, serverHash)
                if (status / 100 != 2) {
                    println("Authentication failed: error $status")
                } else {
                    println("Authentication success! code: $status")
                    Client.connectBlocking()
                    Runtime.getRuntime().addShutdownHook(Thread {
                        if (Client.isOpen) {
                            Client.closeBlocking()
                        }
                    })
                }
            }
        }
    }

    private object UniqueUsersMetric {
        fun putApi() {
            try {
                if (!(Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false) as Boolean)) {
                    val url = "https://api.isxander.dev/metric/put/onecore?type=users&uuid=${mc.session.profile.id}"
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
