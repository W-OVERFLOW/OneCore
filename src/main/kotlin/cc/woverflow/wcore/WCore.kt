package cc.woverflow.wcore

import cc.woverflow.wcore.config.WCoreConfig
import cc.woverflow.wcore.utils.Updater
import cc.woverflow.wcore.utils.command
import cc.woverflow.wcore.utils.openGUI
import net.minecraft.client.Minecraft
import org.spongepowered.asm.launch.MixinBootstrap
import java.io.File

object WCore {
    val mc: Minecraft
    get() = Minecraft.getMinecraft()

    val configFile = File(File(mc.mcDataDir, "W-OVERFLOW"), "W-CORE")

    @Suppress("unused")
    @JvmStatic
    fun initialize() {
        MixinBootstrap.init()
    }

    fun modInitialization() {
        command("wcore") {
            main {
                WCoreConfig.openGUI()
            }
        }
    }

    fun modPostInitialization() {
        Updater.update()
    }
}