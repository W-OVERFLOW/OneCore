package cc.woverflow.onecore.utils

//#if MODERN==0
import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard
//#endif
//#if FABRIC==1
//$$ import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper
//$$ import net.minecraft.client.option.KeyBinding
//$$ import net.minecraft.client.util.InputUtil
//#endif

private val keybinds = arrayListOf<KeybindBuilder>()

class KeybindBuilder private constructor(name: String, category: String, defaultKey: Int, modern: Boolean, inputType: InputType?) : Builder {

    //#if FABRIC==1
    //$$ constructor(name: String, category: String, defaultKey: Int, inputType: InputUtil.Type) : this(name, category, defaultKey, true, when (inputType) {
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


    private val internalKeybind by lazy {
        if (modern && inputType == null) throw UnsupportedOperationException("constructor(name: String, category: String, defaultKey: Int) unsupported in 1.16+!")
        //#if MODERN==0
        KeyBinding(name, defaultKey, category)
        //#else
            //#if FABRIC==1
            //$$ KeyBinding(name, when(inputType) {
            //$$ InputType.KEYBOARD -> InputUtil.Type.KEYSYM
            //$$ InputType.MOUSE -> InputUtil.Type.MOUSE
            //$$ InputType.SCAN -> InputUtil.Type.SCANCODE
            //$$ else -> InputUtil.Type.KEYSYM
            //$$ }, defaultKey, category)
            //#else
            //$$ if (true) throw RuntimeException("Keybinds are not supported in forge!")
            //#endif
        //#endif
    }


    var onPress: () -> Unit = {}
    var onHold: () -> Unit = {}
    var onRelease: () -> Unit = {}

    override fun build() {
        //#if MODERN==0
        ClientRegistry.registerKeyBinding(internalKeybind)
        //#endif
        //#if FABRIC==1
        //$$ KeyBindingHelper.registerKeyBinding(internalKeybind)
        //#endif
        keybinds.add(this)
    }

    fun onKeyPress() {
        if (
            //#if MODERN==0
            internalKeybind.isKeyDown
            //#else
                //#if FABRIC==1
                //$$ internalKeybind.isPressed
                //#else
                //$$ false
                //#endif
            //#endif
        ) {
            if (
                //#if MODERN==0
                Keyboard.isRepeatEvent()
                //#else
                    //#if FABRIC==1
                    //$$ internalKeybind.isPressed && !internalKeybind.wasPressed()
                    //#else
                    //$$ false
                    //#endif
                //#endif
            ) {
                onHold.invoke()
            } else if (!
                //#if MODERN==0
                Keyboard.getEventKeyState()
                //#else
                    //#if FABRIC==1
                    //$$ internalKeybind.isPressed
                    //#else
                    //$$ false
                    //#endif
                //#endif
            ) {
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
        keybinds.forEach { it.onKeyPress() }
    }
}