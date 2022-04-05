package cc.woverflow.onecore.config

import cc.woverflow.onecore.OneCore
import cc.woverflow.onecore.files.StupidFileHack
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType

/**
 * The OneCore config.
 */
object OneCoreConfig : Vigilant(StupidFileHack.getFileFrom(OneCore.configFile, "config.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Update Notifications",
        description = "Show update notifications for W-OVERFLOW mods.",
        category = "Updater"
    )
    var showUpdateNotifications = true

    init {
        initialize()
    }
}