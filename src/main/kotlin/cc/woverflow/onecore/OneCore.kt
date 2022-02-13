package cc.woverflow.onecore

import cc.woverflow.onecore.config.OneCoreConfig
import cc.woverflow.onecore.utils.Updater
import cc.woverflow.onecore.utils.command
import cc.woverflow.onecore.utils.openScreen
import net.minecraft.client.Minecraft
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
            OneCoreConfig.preload()
            command("onecore") {
                main {
                    OneCoreConfig.openScreen()
                }
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
}