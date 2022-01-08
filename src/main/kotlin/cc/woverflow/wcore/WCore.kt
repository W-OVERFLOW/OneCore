package cc.woverflow.wcore

import cc.woverflow.wcore.command.WCoreCommand
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
        WCoreCommand.register()
    }

    fun modPostInitialization() {
        Updater.update()
    }
}