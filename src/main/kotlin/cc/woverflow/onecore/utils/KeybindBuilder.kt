package cc.woverflow.onecore.utils

import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.fml.client.registry.ClientRegistry
import org.lwjgl.input.Keyboard

internal val keybinds = arrayListOf<KeybindBuilder>()

class KeybindBuilder(name: String, category: String, defaultKey: Int) : Builder {

    private val internalKeybind by lazy { KeyBinding(name, defaultKey, category) }
    var onPress: () -> Unit = {}
    var onHold: () -> Unit = {}
    var onRelease: () -> Unit = {}

    override fun build() {
        ClientRegistry.registerKeyBinding(internalKeybind)
        keybinds.add(this)
    }

    fun onKeyPress(
        release: Boolean, repeat: Boolean, keyCode: Int
    ) {
        if (keyCode == internalKeybind.keyCode) {
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

internal object KeybindHandler {

    fun onKeyboardInput() {
        val release = Keyboard.getEventKeyState()
        val repeat = Keyboard.isRepeatEvent()
        val keyCode = Keyboard.getEventKey()
        keybinds.forEach { it.onKeyPress(release, repeat, keyCode) }
    }
}