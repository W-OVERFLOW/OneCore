package com.example.template.command

import com.example.template.ForgeTemplate
import com.example.template.config.TemplateConfig
import gg.essential.api.EssentialAPI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler

object TemplateCommand : Command(ForgeTemplate.ID, true) {

    @DefaultHandler
    fun handle() {
        EssentialAPI.getGuiUtil().openScreen(TemplateConfig.gui())
    }
}