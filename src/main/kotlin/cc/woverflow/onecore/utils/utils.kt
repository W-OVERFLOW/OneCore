@file:JvmName("Utils")

package cc.woverflow.onecore.utils

import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Notifications
import gg.essential.api.gui.Slot
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels

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
) = EssentialAPI.getNotifications().push(title, message, duration, action, close) {
    this.elementaVersion = ElementaVersion.V1
    this.withCustomComponent(Slot.PREVIEW, UIImage.ofResource("/assets/onecore/woverflow.png") constrain {
        width = 30.pixels()
        height = 30.pixels()
    })
}