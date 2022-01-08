package cc.woverflow.wcore.command

import cc.woverflow.wcore.config.WCoreConfig
import cc.woverflow.wcore.openGUI
import gg.essential.api.commands.Command
import gg.essential.api.commands.DefaultHandler

/**
 * The W-CORE command. Meant for internal usage only. DO NOT TOUCH
 */
object WCoreCommand : Command("wcore") {
    @DefaultHandler
    fun handle() = WCoreConfig.openGUI()
}