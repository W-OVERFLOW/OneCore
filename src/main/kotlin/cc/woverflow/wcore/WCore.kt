package cc.woverflow.wcore

import cc.woverflow.wcore.config.WCoreConfig
import cc.woverflow.wcore.utils.Updater
import cc.woverflow.wcore.utils.command
import cc.woverflow.wcore.utils.openScreen
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent
import java.io.File

@Mod(
    name = WCore.NAME,
    modid = WCore.ID,
    version = WCore.VERSION,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object WCore {

    private var init = false
    private var postInit = false

    const val NAME = "@NAME@"
    const val ID = "@ID@"
    const val VERSION = "@VER@"

    val configFile = File(File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), "W-CORE")

    @Mod.EventHandler
    fun onInit(event: FMLInitializationEvent?) {
        if (!init) {
            init = true
            if (!configFile.exists()) {
                configFile.mkdirs()
            }
            WCoreConfig.preload()
            command("wcore") {
                main {
                    WCoreConfig.openScreen()
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