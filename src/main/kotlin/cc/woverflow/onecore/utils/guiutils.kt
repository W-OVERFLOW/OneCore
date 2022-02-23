@file:JvmName("GuiUtils")

package cc.woverflow.onecore.utils

import gg.essential.api.EssentialAPI
import gg.essential.api.utils.GuiUtil
import gg.essential.api.utils.Multithreading
import gg.essential.vigilance.Vigilant
import net.minecraft.client.gui.GuiScreen

/**
 * Queue a new Vigilance GUI for opening. Runs async from main thread to prevent lag.
 * @see GuiUtil.openScreen
 */
fun Vigilant.openScreen() = Multithreading.runAsync { gui()?.openScreen() }

/**
 * Queue a new screen for opening.
 * @see GuiUtil.openScreen
 */
fun GuiScreen.openScreen() = EssentialAPI.getGuiUtil().openScreen(this)