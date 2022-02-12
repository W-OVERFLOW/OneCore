@file:JvmName("RenderUtils")

package cc.woverflow.onecore.utils

import net.minecraft.client.gui.FontRenderer

private val regex = Regex("(?i)\\u00A7[0-9a-f]")

/**
 * Draws a string with a border shadow effect.
 */
fun FontRenderer.drawBorderedString(
    text: String, x: Int, y: Int, color: Int, opacity: Int
): Int {
    val noColors = text.replace(regex, "\u00A7r")
    var yes = 0
    if ((opacity / 4) > 3) {
        bypassNameHighlight = true
        for (xOff in -2..2) {
            for (yOff in -2..2) {
                if (xOff * xOff != yOff * yOff) {
                    yes += drawString(
                        noColors, (xOff / 2f) + x, (yOff / 2f) + y, (opacity / 4) shl 24, false
                    )
                }
            }
        }
        bypassNameHighlight = false
    }
    yes += drawString(text, x, y, color)
    return yes
}

internal var bypassNameHighlight = false
    private set