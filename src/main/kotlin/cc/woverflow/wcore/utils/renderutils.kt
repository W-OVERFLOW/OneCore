@file:JvmName("RenderUtils")

package cc.woverflow.wcore.utils

import net.minecraft.client.gui.FontRenderer

private val regex = Regex("(?i)\\u00A7[0-9a-f]")
var bypassWyvtils = false

fun FontRenderer.drawBorderedString(
    text: String, x: Int, y: Int, color: Int, opacity: Int
): Int {
    val noColors = text.replace(regex, "\u00A7r")
    var yes = 0
    if ((opacity / 4) > 3) {
        bypassWyvtils = true
        for (xOff in -2..2) {
            for (yOff in -2..2) {
                if (xOff * xOff != yOff * yOff) {
                    yes += drawString(
                        noColors, (xOff / 2f) + x, (yOff / 2f) + y, (opacity / 4) shl 24, false
                    )
                }
            }
        }
        bypassWyvtils = false
    }
    yes += drawString(text, x, y, color)
    return yes
}

