package com.example.template

import com.example.template.command.TemplateCommand
import com.example.template.config.TemplateConfig
import com.example.template.updater.Updater
import net.minecraft.client.Minecraft
import net.minecraftforge.fml.common.Mod
import net.minecraftforge.fml.common.event.FMLInitializationEvent
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent
import java.io.File

@Mod(
    modid = ForgeTemplate.ID,
    name = ForgeTemplate.NAME,
    version = ForgeTemplate.VER,
    modLanguageAdapter = "gg.essential.api.utils.KotlinAdapter"
)
object ForgeTemplate {

    const val NAME = "@NAME@"
    const val VER = "@VER@"
    const val ID = "@ID@"
    lateinit var jarFile: File
        private set

    val modDir = File(File(Minecraft.getMinecraft().mcDataDir, "W-OVERFLOW"), NAME)

    @Mod.EventHandler
    fun onFMLPreInitialization(event: FMLPreInitializationEvent) {
        if (!modDir.exists()) modDir.mkdirs()
        jarFile = event.sourceFile
    }

    @Mod.EventHandler
    fun onInitialization(event: FMLInitializationEvent) {
        TemplateConfig.preload()
        TemplateCommand.register()
        Updater.update()
    }
}
