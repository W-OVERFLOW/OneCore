package cc.woverflow.onecore.utils.gui.components

import gg.essential.elementa.components.UIBlock
import gg.essential.elementa.components.UIText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.ChildBasedSizeConstraint
import gg.essential.elementa.constraints.ColorConstraint
import gg.essential.elementa.constraints.animation.Animations
import gg.essential.elementa.dsl.*
import gg.essential.universal.USound

open class Button<T>(
    private val unfocusedColor: ColorConstraint,
    private val focusedColor: ColorConstraint,
    private val providedValue: () -> T,
    private val enabled: T.() -> Boolean = { true },
    private val onClick: T.() -> Unit
) : UIBlock(unfocusedColor) {

    init {
        onMouseEnter {
            animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, focusedColor)
            }
        }
        onMouseLeave {
            animate {
                setColorAnimation(Animations.OUT_EXP, 0.5f, unfocusedColor)
            }
        }
        onMouseClick {
            providedValue().let {
                if (enabled.invoke(it)) {
                    USound.playButtonPress()
                    onClick.invoke(it)
                }
            }
        }
    }
}

class TextButton<T>(
    text: String,
    unfocusedColor: ColorConstraint,
    focusedColor: ColorConstraint,
    providedValue: () -> T,
    enabled: T.() -> Boolean = { true },
    onClick: T.() -> Unit
) : Button<T>(unfocusedColor, focusedColor, providedValue, enabled, onClick) {

    private val theThing by UIText(text, shadow = false) constrain {
        x = CenterConstraint()
        y = CenterConstraint()
    } childOf this

    init {
        constrain {
            width = ChildBasedSizeConstraint() + 10.pixels()
            height = ChildBasedSizeConstraint() + 10.pixels()
        }
    }

    fun setText(string: String) {
        theThing.setText(string)
    }
}