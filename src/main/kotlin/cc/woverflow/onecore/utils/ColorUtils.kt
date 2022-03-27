@file:JvmName("ColorUtils")

package cc.woverflow.onecore.utils

import java.awt.Color

/**
 * @return A changing colour based on the users' computer time. Simulates a "chroma" colour.
 */
fun timeBasedChroma(): Int {
    val l = System.currentTimeMillis()
    return Color.HSBtoRGB(l % 2000L / 2000.0f, 1.0f, 1.0f)
}

/**
 * @return The red value of the provided RGBA value.
 */
fun Int.getRed(): Int {
    return (this shr 16) and 0xFF
}

/**
 * @return The green value of the provided RGBA value.
 */
fun Int.getGreen(): Int {
    return (this shr 8) and 0xFF
}

/**
 * @return The blue value of the provided RGBA value.
 */
fun Int.getBlue(): Int {
    return (this shr 0) and 0xFF
}

/**
 * @return The alpha value of the provided RGBA value.
 */
fun Int.getAlpha(): Int {
    return (this shr 24) and 0xFF
}

/**
 * @return The provided [Color] with the alpha value.
 */
fun Color.withAlpha(alpha: Int): Color = Color(red, green, blue, alpha)

/**
 * @return The provided [Color] with the alpha red.
 */
fun Color.withRed(red: Int): Color = Color(red, green, blue, alpha)

/**
 * @return The provided [Color] with the green value.
 */
fun Color.withGreen(green: Int): Color = Color(red, green, blue, alpha)

/**
 * @return The provided [Color] with the blue value.
 */
fun Color.withBlue(blue: Int): Color = Color(red, green, blue, alpha)