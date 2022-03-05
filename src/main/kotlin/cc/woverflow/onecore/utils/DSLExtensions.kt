@file:JvmName("DSLExtensions")

package cc.woverflow.onecore.utils

import java.util.ArrayList

fun keybind(name: String, category: String, defaultKey: Int, builder: KeybindBuilder.() -> Unit) =
    KeybindBuilder(name, category, defaultKey).apply(builder).also { it.build() }

@JvmOverloads
fun command(
    name: String,
    aliases: ArrayList<String> = arrayListOf(),
    generateHelpCommand: Boolean = true,
    builder: CommandBuilder.() -> Unit
): CommandBuilder {
    return CommandBuilder(name, aliases, generateHelpCommand).apply(builder).also { it.build() }
}