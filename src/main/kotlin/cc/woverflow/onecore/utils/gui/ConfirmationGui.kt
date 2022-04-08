package cc.woverflow.onecore.utils.gui

import cc.woverflow.onecore.utils.gui.components.TextButton
import gg.essential.elementa.ElementaVersion
import gg.essential.elementa.WindowScreen
import gg.essential.elementa.components.UIContainer
import gg.essential.elementa.components.UIWrappedText
import gg.essential.elementa.constraints.CenterConstraint
import gg.essential.elementa.constraints.SiblingConstraint
import gg.essential.elementa.dsl.*
import gg.essential.universal.ChatColor
import java.awt.Color
import java.util.function.Consumer

class ConfirmationGui @JvmOverloads constructor(title: String, text: String, subText: String? = null, confirmText: String = "${ChatColor.GREEN}Yes", denyText: String = "${ChatColor.RED}No", onConfirm: ConfirmationGui.() -> Unit, onDeny: ConfirmationGui.() -> Unit) : WindowScreen(version = ElementaVersion.V1) {
    @JvmOverloads constructor(title: String, text: String, subText: String? = null, confirmText: String = "${ChatColor.GREEN}Yes", denyText: String = "${ChatColor.RED}No", onConfirm: Consumer<ConfirmationGui>, onDeny: Consumer<ConfirmationGui>) : this(title, text, subText, confirmText, denyText, { onConfirm.accept(this) }, { onDeny.accept(this) })

    val title by UIWrappedText(text = title, centered = true) constrain {
        textScale = 3.pixels()
        width = 100.percent()
        y = 10.percent()
    } childOf window

    val text by UIWrappedText(text = text, centered = true) constrain {
        x = CenterConstraint()
        textScale = 2.pixels()
        width = 50.percent()
        y = SiblingConstraint(20f)
    } childOf window

    val subText by UIWrappedText(text = subText ?: "", centered = true) constrain {
        x = CenterConstraint()
        width = 50.percent()
        y = SiblingConstraint(5f)
    } childOf window

    private val buttonContainer by UIContainer() constrain {
        x = CenterConstraint()
        y = 75.percent()
        width = 100.percent()
        height = 25.percent()
    } childOf window

    private val yes by TextButton(confirmText, black, white, providedValue = { this }, onClick = onConfirm, width = 60) constrain {
        x = CenterConstraint()
        y = 5.pixels()
    } childOf buttonContainer

    val no by TextButton(denyText, black, white, providedValue = { this }, onClick = onDeny, width = 60) constrain {
        x = CenterConstraint()
        y = SiblingConstraint(5f)
    } childOf buttonContainer

    private companion object {
        val white = Color(255, 255, 255, 200).toConstraint()
        val black = Color(0, 0, 0, 200).toConstraint()
    }
}