package cc.woverflow.onecore

import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.files.StupidFileHack
import cc.woverflow.onecore.internal.Events
import cc.woverflow.onecore.internal.websocket.Client
import cc.woverflow.onecore.utils.*
import cc.woverflow.onecore.internal.websocket.WebsocketUtils
//#if MODERN==0
import net.minecraft.launchwrapper.Launch
//#endif


object OneCore {

    private var init = false

    const val NAME = "@NAME@"
    const val ID = "@ID@"
    const val VERSION = "@VER@"

    val configFile = StupidFileHack.getFileFrom(StupidFileHack.getFileFrom(runDirectory,
        "W-OVERFLOW"), "OneCore")

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
            Updater.update()
            /*/
            launchCoroutine {
                val uuid = playerID.toString().replace("-", "")
                val serverHash = WebsocketUtils.hash(uuid + WebsocketUtils.nextSalt)
                val status = WebsocketUtils.authenticate(
                    //#if MODERN==0
                    session.token,
                    //#else
                    //$$ session.sessionId,
                    //#endif
                    uuid, serverHash)
                if (status / 100 != 2) {
                    println("Authentication failed: error $status")
                } else {
                    println("Authentication success! code: $status")
                    UniqueUsersMetric.putApi()
                    Client.connectBlocking()
                    Runtime.getRuntime().addShutdownHook(Thread {
                        if (Client.isOpen) {
                            Client.closeBlocking()
                        }
                    })
                }
            }

             */
        }
    }

    private object UniqueUsersMetric {
        fun putApi() {
            try {
                if (!isNotEligible()) {
                    val url = "https://api.isxander.dev/metric/put/onecore?type=users&uuid=${playerID}"
                    val response = APIUtil.getJsonElement(url)!!.asJsonObject
                    if (!response["success"].asBoolean) {
                        println("Metric API could not be called: ${response["error"].asString}")
                        return
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }

        private fun isNotEligible(): Boolean {
            //#if MODERN==0
            return Launch.blackboard.getOrDefault("fml.deobfuscatedEnvironment", false) as Boolean
            //#else
                //#if MC>=11800
                //$$ return (mc as cc.woverflow.onecore.internal.mixin.MinecraftAccessor).userApiService != com.mojang.authlib.minecraft.UserApiService.OFFLINE
                //#else
                //$$ return false
                //#endif
            //#endif
        }
    }
}
