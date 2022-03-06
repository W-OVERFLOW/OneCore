package cc.woverflow.onecore

import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.utils.*
import gg.essential.api.utils.Multithreading
import gg.essential.api.utils.WebUtil
import net.minecraft.client.Minecraft
import net.minecraft.launchwrapper.Launch
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import java.io.File


@Mod(
    name = OneCore.NAME,
    modid = OneCore.ID,
    version = OneCore.VERSION,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object OneCore {

    private var init = false
    private var postInit = false

    const val NAME = "@NAME@"
    const val ID = "@ID@"
    const val VERSION = "@VER@"

    val configFile = File(File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), "OneCore")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?) {
        if (!init) {
            init = true
            if (!configFile.exists()) {
                configFile.mkdirs()
            }
            KeybindHandler.initialize()
            OneCoreConfig.preload()
            command("onecore") {
                main {
                    OneCoreConfig.openScreen()
                }
            }
            Multithreading.runAsync {
                UniqueUsersMetric.putApi()
            }
        }
    }

    @Mod.EventHandler
    fun onPostInit(event: FMLPostInitializationEvent?) {
        if (!postInit) {
            postInit = true
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