@file:JvmName("RenderUtils")

package cc.woverflow.onecore.utils

import gg.essential.universal.UGraphics
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

inline fun <T, R> T.newMatrix(block: T.() -> R) {
    UGraphics.GL.pushMatrix()
    block.invoke(this)
    UGraphics.GL.popMatrix()
}

inline fun <T, R> T.withColor(rgba: Int, block: T.() -> R) = newMatrix {
    UGraphics.color4f(
        rgba.getRed().toFloat(),
        rgba.getGreen().toFloat(),
        rgba.getBlue().toFloat(),
        rgba.getAlpha().toFloat()
    )
    block.invoke(this)
}

inline fun <T, R> T.withScale(xScale: Float, yScale: Float, zScale: Float, block: T.() -> R) = newMatrix {
    UGraphics.GL.translate(xScale, yScale, zScale)
    block.invoke(this)
}

inline fun <T, R> T.withTranslate(x: Float, y: Float, z: Float, block: T.() -> R) = newMatrix {
    UGraphics.GL.translate(x, y, z)
    block.invoke(this)
}

inline fun <T, R> T.withRotation(angle: Float, x: Float, y: Float, z: Float, block: T.() -> R) = newMatrix {
    UGraphics.GL.rotate(angle, x, y, z)
    block.invoke(this)
}

internal var bypassNameHighlight = false
    private set