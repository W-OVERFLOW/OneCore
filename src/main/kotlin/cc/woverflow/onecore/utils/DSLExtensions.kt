@file:JvmName("DSLExtensions")

package cc.woverflow.onecore.utils

import java.util.ArrayList

fun tick(ticks: Int, block: () -> Unit) = TickDelay(ticks, block)

@Deprecated("Will not work on 1.16+", ReplaceWith("keybind(name, category, defaultKey, inputType, builder)"))
fun keybind(name: String, category: String, defaultKey: Int, builder: KeybindBuilder.() -> Unit) =
    KeybindBuilder(name, category, defaultKey).apply(builder).also { it.build() }

fun keybind(name: String, category: String, defaultKey: Int, inputType: InputType, builder: KeybindBuilder.() -> Unit) =
    KeybindBuilder(name, category, defaultKey, inputType).apply(builder).also { it.build() }

@JvmOverloads
fun command(
    name: String,
    aliases: ArrayList<String> = arrayListOf(),
    generateHelpCommand: Boolean = true,
    builder: CommandBuilder.() -> Unit
) = CommandBuilder(name, aliases, generateHelpCommand).apply(builder).also { it.build() }