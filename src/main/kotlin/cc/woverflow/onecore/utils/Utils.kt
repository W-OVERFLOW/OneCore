@file:JvmName("Utils")

package cc.woverflow.onecore.utils

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Notifications
import gg.essential.api.gui.Slot
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import gg.essential.universal.UMinecraft
import gg.essential.universal.utils.MCMinecraft

//#if MODERN==1
//$$ import gg.essential.universal.UChat
//$$ import gg.essential.universal.ChatColor
//#endif

/**
 * Push a new notification with the given title, message, customizable duration, action, and close action
 * with the W-OVERFLOW logo alongside it.
 *
 * @param title notification header
 * @param message notification body
 * @param duration how long in seconds the notification will stay on screen
 * @param action ran when the player clicks the notification
 * @param close ran when the notification has expired
 *
 * @see Notifications
 */
@JvmOverloads
fun sendBrandedNotification(
    title: String, message: String, duration: Float = 4f, action: () -> Unit = {}, close: () -> Unit = {}
) {
    //#if MODERN==0
    EssentialAPI.getNotifications().push(title, message, duration, action, close) {
        this.elementaVersion = ElementaVersion.V1
        this.withCustomComponent(Slot.PREVIEW, UIImage.ofResource("/assets/onecore/woverflow.png") constrain {
            width = 40.pixels()
            height = 40.pixels()
        })
    }
    //#else
    //$$ UChat.chat("${if (!title.contains("§")) "${ChatColor.AQUA}${title.trim()}" else title.trim()}${ChatColor.RESET} ${message.trim()}")
    //#endif
}

/**
 * Push a new notification with the given title, message, customizable duration, [action] which will be invoked
 * when the user clicks on the notification, and [close] which will be invoked when the notification has expired.
 * This can be used for all sorts of purposes, as it is generally useful
 * to respond with some type of action when the user clicks a notification.
 *
 * Meant to be used in Java ONLY.
 *
 * @param title notification header
 * @param message notification body
 * @param duration how long in seconds the notification will stay on screen
 * @param action ran when the player clicks the notification
 * @param close ran when the notification has expired
 */
@JvmOverloads
fun pushNotification(
    title: String,
    message: String,
    duration: Float = 4f,
    action: Runnable = Runnable {  },
    close: Runnable = Runnable {  }
) {
    //#if MODERN==0
    EssentialAPI.getNotifications().push(title, message, duration, { action.run() }, { close.run() })
    //#endif
}

val mc: MCMinecraft
    get() = UMinecraft.getMinecraft()