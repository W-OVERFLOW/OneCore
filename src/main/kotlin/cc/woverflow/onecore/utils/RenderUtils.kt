@file:JvmName("RenderUtils")

package cc.woverflow.onecore.utils

import gg.essential.universal.UGraphics
import gg.essential.universal.UMatrixStack
import gg.essential.universal.utils.MCFontRenderer

private val regex = Regex("(?i)\\u00A7[0-9a-f]")

/**
 * Draws a string with a border shadow effect.
 */
fun MCFontRenderer.drawBorderedString(
    matrix: UMatrixStack?, text: String, x: Int, y: Int, color: Int, opacity: Int
): Int {
    //#if MODERN==1
    //$$ if (matrix == null) throw NullPointerException("UMatrixStack in MCFontRenderer.drawBorderedString was not provided!")
    //#endif
    val noColors = text.replace(regex, "\u00A7r")
    var yes = 0
    if (opacity > 3) {
        bypassNameHighlight = true
        for (xOff in -2..2) {
            for (yOff in -2..2) {
                if (xOff * xOff != yOff * yOff) {
                    yes +=
                        //#if MODERN==0
                        drawString(
                        noColors, (xOff / 2f) + x, (yOff / 2f) + y, (opacity) shl 24, false
                        )
                        //#else
                        //$$ draw(
                        //$$     matrix.toMC(), noColors, (xOff / 2f) + x, (yOff / 2f) + y, (opacity) shl 24
                        //$$     )
                        //#endif
                }
            }
        }
        bypassNameHighlight = false
    }
    yes +=
        //#if MODERN==0
        drawString(text, x, y, color)
        //#else
        //$$ draw(matrix.toMC(), text, x.toFloat(), y.toFloat(), color)
        //#endif
    return yes
}

inline infix fun <R> UMatrixStack.newMatrix(block: UMatrixStack.() -> R) {
    push()
    block()
    pop()
}

inline fun <R> newMatrixCompat(block: UMatrixStack.() -> R) = UMatrixStack.Compat.get().newMatrix(block)

inline fun <R> newMatrix(block: UMatrixStack.() -> R) = UMatrixStack().newMatrix(block)

inline fun <R> UMatrixStack.withColor(rgba: Int, block: UMatrixStack.() -> R) = this newMatrix {
    UGraphics.color4f(
        rgba.getRed().toFloat(),
        rgba.getGreen().toFloat(),
        rgba.getBlue().toFloat(),
        rgba.getAlpha().toFloat()
    )
    block(this)
}

inline fun <R> withColor(rgba: Int, block: UMatrixStack.() -> R) = UMatrixStack().withColor(rgba, block)

inline fun <R> withColorCompat(rgba: Int, block: UMatrixStack.() -> R) = UMatrixStack.Compat.get().withColor(rgba, block)

inline fun <R> UMatrixStack.withScale(xScale: Float, yScale: Float, zScale: Float, block: UMatrixStack.() -> R) = this newMatrix {
    scale(xScale, yScale, zScale)
    block(this)
}

inline fun <R> withScale(xScale: Float, yScale: Float, zScale: Float, block: UMatrixStack.() -> R) = UMatrixStack().withScale(xScale, yScale, zScale, block)

inline fun <R> withScaleCompat(xScale: Float, yScale: Float, zScale: Float, block: UMatrixStack.() -> R) = UMatrixStack.Compat.get().withScale(xScale, yScale, zScale, block)

inline fun <R> UMatrixStack.withTranslate(x: Float, y: Float, z: Float, block: UMatrixStack.() -> R) = this newMatrix {
    translate(x, y, z)
    block(this)
}

inline fun <R> withTranslate(x: Float, y: Float, z: Float, block: UMatrixStack.() -> R) = UMatrixStack().withTranslate(x, y, z, block)

inline fun <R> withTranslateCompat(x: Float, y: Float, z: Float, block: UMatrixStack.() -> R) = UMatrixStack.Compat.get().withTranslate(x, y, z, block)

inline fun <R> UMatrixStack.withRotation(angle: Float, x: Float, y: Float, z: Float, block: UMatrixStack.() -> R) = this newMatrix {
    rotate(angle, x, y, z)
    block(this)
}

inline fun <R> withRotation(angle: Float, x: Float, y: Float, z: Float, block: UMatrixStack.() -> R) = UMatrixStack().withRotation(angle, x, y, z, block)

inline fun <R> withRotationCompat(angle: Float, x: Float, y: Float, z: Float, block: UMatrixStack.() -> R) = UMatrixStack.Compat.get().withRotation(angle, x, y, z, block)

//#if MODERN==0
@Deprecated("Does not work in 1.17+.")
inline fun <T, R> T.newMatrix(block: T.() -> R) {
    UGraphics.GL.pushMatrix()
    block.invoke(this)
    UGraphics.GL.popMatrix()
}

@Deprecated("Does not work in 1.17+.")
inline fun <T, R> T.withColor(rgba: Int, block: T.() -> R) = newMatrix {
    UGraphics.color4f(
        rgba.getRed().toFloat(),
        rgba.getGreen().toFloat(),
        rgba.getBlue().toFloat(),
        rgba.getAlpha().toFloat()
    )
    block.invoke(this)
}

@Deprecated("Does not work in 1.17+.")
inline fun <T, R> T.withScale(xScale: Float, yScale: Float, zScale: Float, block: T.() -> R) = newMatrix {
    UGraphics.GL.translate(xScale, yScale, zScale)
    block.invoke(this)
}

@Deprecated("Does not work in 1.17+.")
inline fun <T, R> T.withTranslate(x: Float, y: Float, z: Float, block: T.() -> R) = newMatrix {
    UGraphics.GL.translate(x, y, z)
    block.invoke(this)
}

@Deprecated("Does not work in 1.17+.")
inline fun <T, R> T.withRotation(angle: Float, x: Float, y: Float, z: Float, block: T.() -> R) = newMatrix {
    UGraphics.GL.rotate(angle, x, y, z)
    block.invoke(this)
}
//#endif

internal var bypassNameHighlight = false
    private set