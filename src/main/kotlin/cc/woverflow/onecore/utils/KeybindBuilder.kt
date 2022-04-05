package cc.woverflow.onecore.utils

//#if MODERN==0
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard
//#else
//$$ import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
//$$ import net.minecraft.client.option.KeyBinding
//#endif

internal val keybinds = arrayListOf<KeybindBuilder>()

class KeybindBuilder private constructor(name: String, category: String, defaultKey: Int, modern: Boolean, inputType: InputType?) : Builder {

    //#if MODERN==1
    //$$ constructor(name: String, category: String, defaultKey: Int, inputType: InputUtil.Type) : this(name, category, defaultKey, true, when (inputKey) {
    //$$ InputUtil.Type.KEYSYM -> InputType.KEYBOARD
    //$$ InputUtil.Type.MOUSE -> InputType.MOUSE
    //$$ InputUtil.Type.SCANCODE -> InputType.SCAN
    //$$ else -> InputType.KEYBOARD
    //$$ })
    //#endif

    //#if MODERN==1
    //$$ @Deprecated("Will not work on 1.16+")
    //#endif
    constructor(name: String, category: String, defaultKey: Int) : this(name, category, defaultKey, false, null)


    internal val internalKeybind by lazy {
        if (modern && inputType == null) throw UnsupportedOperationException("constructor(name: String, category: String, defaultKey: Int) unsupported in 1.16+!")
        //#if MODERN==0
        KeyBinding(name, defaultKey, category)
        //#else
        //$$ KeyBinding(name, when(inputType) {
        //$$ InputType.KEYBOARD -> InputUtil.Type.KEYSYM
        //$$ InputType.MOUSE -> InputUtil.Type.MOUSE
        //$$ InputType.SCAN -> InputUtil.Type.SCANCODE
        //$$ else -> InputUtil.Type.KEYSYM
        //$$ }, defaultKey, category)
        //#endif
    }


    var onPress: () -> Unit = {}
    var onHold: () -> Unit = {}
    var onRelease: () -> Unit = {}

    override fun build() {
        //#if MODERN==0
        ClientRegistry.registerKeyBinding(
        //#else
        //$$ KeyBindingHelper.registerKeyBinding(
        //#endif
            internalKeybind)
        keybinds.add(this)
    }

    fun onKeyPress(
        release: Boolean, repeat: Boolean
    ) {
        if (
            //#if MODERN==0
            internalKeybind.isKeyDown
            //#else
            //$$ internalKeybind.isPressed
            //#endif
        ) {
            if (repeat) {
                onHold.invoke()
            } else if (!release) {
                onRelease.invoke()
            } else {
                onPress.invoke()
            }
        }
    }
}

private enum class InputType {
    KEYBOARD, MOUSE, SCAN // i have no idea what a scan input is but that's there anyways
}

internal object KeybindHandler {

    fun onKeyboardInput() {
        //#if MODERN==0
        val release = Keyboard.getEventKeyState()
        val repeat = Keyboard.isRepeatEvent()
        //#else
        //$$ val release = false
        //$$ val repeat = internalKeybind.isPressed && !internalKeybind.wasPressed
        //#endif
        keybinds.forEach { it.onKeyPress(release, repeat) }
    }
}