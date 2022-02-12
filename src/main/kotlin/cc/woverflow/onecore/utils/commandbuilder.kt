package cc.woverflow.onecore.utils

import gg.essential.universal.UChat
import net.minecraft.command.CommandBase
import net.minecraft.command.ICommandSender
import net.minecraft.util.BlockPos
import net.minecraftforge.client.ClientCommandHandler
import java.util.*

@JvmOverloads
fun command(
    name: String,
    aliases: ArrayList<String> = arrayListOf(),
    generateHelpCommand: Boolean = true,
    builder: CommandBuilder.() -> Unit
): CommandBuilder {
    return CommandBuilder(name, aliases, generateHelpCommand).apply(builder).also { it.build() }
}

class CommandBuilder @JvmOverloads internal constructor(
    private val name: String,
    private val aliases: ArrayList<String> = arrayListOf(),
    private val generateHelpCommand: Boolean = true
) : Builder {
    private var mainCommand: MainCommand? = null
    private val subCommands = hashMapOf<String, SubCommand>()
    private var tabCompletion: ((sender: ICommandSender?, args: Array<String>?, pos: BlockPos?) -> MutableList<String>)? =
        null

    private val subCommandList: String by lazy {
        subCommands.keys.joinToString(separator = "|", prefix = "[", postfix = "]")
    }


    private val subCommandHelpList: String by lazy {
        val builder = StringBuilder()
        mainCommand?.let {
            builder.append("\n/$name${if (it.description.isBlank()) "" else " - ${it.description}"}")
        }
        for (subcommand in subCommands) {
            builder.append(
                "\n/$name ${subcommand.key}${
                    if (subcommand.value.options.isNotEmpty()) subcommand.value.options.joinToString(
                        separator = "|", prefix = " [", postfix = "]"
                    ) else ""
                }${if (subcommand.value.description.isBlank()) "" else " - ${subcommand.value.description}"}"
            )
        }
        builder.append("\n")
        return@lazy builder.toString()
    }

    @JvmOverloads
    fun main(description: String = "", action: () -> Unit) {
        mainCommand = MainCommand(description, action)
    }

    operator fun String.invoke(handle: SubCommandBuilder.() -> Unit) {
        val builder = SubCommandBuilder(this).apply(handle)
        subCommand(builder.name, builder.description, builder.options, builder.action)
    }

    @JvmOverloads
    fun subCommand(
        name: String, description: String = "", options: List<String> = emptyList(), action: (List<String>) -> Unit
    ) {
        subCommands[name] = SubCommand(description, options, action)
    }

    fun onTabCompletion(handle: (sender: ICommandSender?, args: Array<String>?, pos: BlockPos?) -> MutableList<String>) {
        tabCompletion = handle
    }

    override fun build() {
        if (generateHelpCommand && !subCommands.contains("help")) {
            "help" {
                description = "Shows all the available options in this command."
                action = {
                    UChat.chat(this@CommandBuilder.subCommandHelpList)
                }
            }
        }
        ClientCommandHandler.instance.registerCommand(object : CommandBase() {

            override fun getCommandName(): String {
                return name
            }

            override fun getCommandUsage(sender: ICommandSender?): String {
                return "/$name $subCommandList"
            }

            override fun getCommandAliases(): MutableList<String> {
                return aliases
            }

            override fun processCommand(sender: ICommandSender?, args: Array<String>) {
                if (args.isEmpty()) {
                    mainCommand?.action?.invoke()
                } else {
                    for (command in subCommands) {
                        if (command.key.lowercase(Locale.ENGLISH) == args[0].lowercase(Locale.ENGLISH)) {
                            command.value.action.invoke(args.copyOfRange(1, args.size).toMutableList())
                        }
                    }
                }
            }

            override fun addTabCompletionOptions(
                sender: ICommandSender?, args: Array<String>?, pos: BlockPos?
            ): MutableList<String> {
                return tabCompletion?.invoke(sender, args, pos) ?: run {
                    if (args != null && args.isNotEmpty()) {
                        val last = args[args.size - 1]
                        val subcommands = arrayListOf<String>()
                        for (subcommand in this@CommandBuilder.subCommands) {
                            val name = subcommand.key
                            if (name.lowercase(Locale.ENGLISH).startsWith(last.lowercase(Locale.ENGLISH))) {
                                subcommands.add(name)
                            }
                        }
                        return subcommands
                    }
                    return arrayListOf()
                }
            }

            override fun getRequiredPermissionLevel(): Int {
                return -1
            }
        })
    }

    inner class MainCommand @JvmOverloads internal constructor(
        var description: String = "", var action: () -> Unit
    )

    inner class SubCommandBuilder(val name: String) {
        var description: String = ""
        var options: List<String> = emptyList()
        var action: (List<String>) -> Unit = {}
    }

    inner class SubCommand @JvmOverloads internal constructor(
        var description: String = "", var options: List<String> = emptyList(), var action: (List<String>) -> Unit
    )
}