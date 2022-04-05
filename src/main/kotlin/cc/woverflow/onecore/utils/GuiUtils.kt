@file:JvmName("GuiUtils")

package cc.woverflow.onecore.utils

import gg.essential.api.EssentialAPI
import gg.essential.api.utils.GuiUtil
import gg.essential.api.utils.Multithreading
import gg.essential.universal.utils.MCScreen
import gg.essential.vigilance.Vigilant

/**
 * Queue a new Vigilance GUI for opening. Runs async from main thread to prevent lag.
 * @see GuiUtil.openScreen
 */
fun Vigilant.openScreen() = Multithreading.runAsync { gui()?.openScreen() }

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