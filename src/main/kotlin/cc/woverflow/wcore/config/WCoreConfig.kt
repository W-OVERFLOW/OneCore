package cc.woverflow.wcore.config

import cc.woverflow.wcore.WCore
import gg.essential.vigilance.Vigilant
import gg.essential.vigilance.data.Property
import gg.essential.vigilance.data.PropertyType
import java.io.File

/**
 * The W-CORE config.
 */
object WCoreConfig : Vigilant(File(WCore.configFile, "config.toml")) {

    @Property(
        type = PropertyType.SWITCH,
        name = "Show Update Notifications",
        description = "Show update notifications for W-OVERFLOW mods.",
        category = "Updater"
    )
    val showUpdateNotifications = true

    init {
        initialize()
    }
}