@file:JvmName("GuiUtils")

package cc.woverflow.onecore.utils

//#if MODERN==0
import gg.essential.api.EssentialAPI
import gg.essential.api.utils.GuiUtil
//#else
//$$ import gg.essential.universal.UScreen
//#endif
import gg.essential.universal.utils.MCScreen
import gg.essential.vigilance.Vigilant

/**
 * Queue a new Vigilance GUI for opening. Runs async from main thread to prevent lag.
 */
fun Vigilant.openScreen() = launchCoroutine { gui()?.openScreen() }

/**
 * Queue a new screen for opening.
 * @see GuiUtil.openScreen
 */
fun MCScreen.openScreen() {
    //#if MODERN==0
    EssentialAPI.getGuiUtil().openScreen(this)
    //#else
    //$$ tick(1) { UScreen.displayScreen(this) }
    //#endif
}