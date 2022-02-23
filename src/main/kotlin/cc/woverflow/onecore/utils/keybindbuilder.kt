@file:JvmName("KeybindBuilderDSL")

package cc.woverflow.onecore.utils

import net.minecraft.client.settings.KeyBinding
import net.minecraftforge.common.MinecraftForge.EVENT_BUS
import net.minecraftforge.fml.client.registry.ClientRegistry
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent
import net.minecraftforge.fml.common.gameevent.InputEvent
import org.lwjgl.input.Keyboard

private val keybinds = arrayListOf<KeybindBuilder>()

fun keybind(name: String, category: String, defaultKey: Int, builder: KeybindBuilder.() -> Unit) =
    KeybindBuilder(name, category, defaultKey).apply(builder).also { it.build() }

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

    fun initialize() {
        EVENT_BUS.register(this)
    }

    @SubscribeEvent
    fun onKeyboardInput(event: InputEvent.KeyInputEvent) {
        val release = Keyboard.getEventKeyState()
        val repeat = Keyboard.isRepeatEvent()
        val keyCode = Keyboard.getEventKey()
        keybinds.forEach { it.onKeyPress(release, repeat, keyCode) }
    }
}