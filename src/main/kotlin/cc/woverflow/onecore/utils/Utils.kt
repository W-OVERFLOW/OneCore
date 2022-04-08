@file:JvmName("Utils")

package cc.woverflow.onecore.utils

//#if MODERN==0
import gg.essential.api.EssentialAPI
import gg.essential.api.gui.Notifications
import gg.essential.api.gui.Slot
//#endif
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.components.UIImage
import gg.essential.elementa.dsl.constrain
import gg.essential.elementa.dsl.pixels
import kotlinx.coroutines.CoroutineName
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.atomic.AtomicInteger

//#if FABRIC==1
//$$ import gg.essential.universal.UChat
//$$ import gg.essential.universal.ChatColor
//$$ import net.minecraft.text.ClickEvent
//$$ import net.minecraft.text.LiteralText
//$$ import cc.woverflow.onecore.internal.hook.ClickEventHook
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
    //#endif
    //#if FABRIC==1
    //$$ net.minecraft.client.MinecraftClient.getInstance().inGameHud.chatHud.addMessage(createRunnableComponent("${if (!title.contains("§")) "${ChatColor.AQUA}[${title.trim()}]" else "[${title.trim()}]"}${ChatColor.RESET} ${message.trim()}", action))
    //#endif
}

//#if FABRIC==1
//$$ fun createRunnableComponent(text: String, runnable: Runnable) = LiteralText(text).also { it.style = it.style.withClickEvent(ClickEvent(null, null).also { event -> (event as ClickEventHook).setOneCoreEvent(true); (event as ClickEventHook).setOneCoreRunnable(runnable); }) }
//#endif

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

private var number = AtomicInteger(0)

/**
 * Creates a [CoroutineScope] and launches it asynchronously.
 */
fun launchCoroutine(name: String = "OneCore Coroutine ${number.incrementAndGet()}", block: suspend CoroutineScope.() -> Unit) {
    CoroutineScope(Dispatchers.IO + CoroutineName(name)).launch(block = block)
}

/**
 * The Minecraft instance.
 */
//#if MODERN==0
val mc: net.minecraft.client.Minecraft
    get() = net.minecraft.client.Minecraft.getMinecraft()
//#else
    //#if FABRIC==1
    //$$ val mc: net.minecraft.client.MinecraftClient
    //$$    get() = net.minecraft.client.MinecraftClient.getInstance()
    //#else
    //$$ val mc: net.minecraft.client.Minecraft
    //$$    get() = net.minecraft.client.Minecraft.getInstance()
    //#endif
//#endif

/**
 * The player's session info.
 */
val session
get() = mc.
//#if MODERNFORGE==0
session
//#else
//$$user
//#endif

/**
 * The player's UUID.
 */
val playerID
get() = session.
        //#if MODERN==0
        playerID
        //#else
        //$$ uuid
        //#endif

/**
 * The run directory of the current Minecraft instance.
 */
val runDirectory
get() = mc.
    //#if MODERN==0
    mcDataDir
    //#else
    //#if MODERNFORGE==1
    //$$ gameDirectory
    //#else
    //$$ runDirectory
    //#endif
    //#endif