package dev.s7a.commandapi.example

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.literalArgument
import org.bukkit.plugin.java.JavaPlugin

@Suppress("unused")
class ExamplePlugin : JavaPlugin() {
    override fun onLoad() {
        CommandAPI.onLoad(CommandAPIPaperConfig(this).verboseOutput(true))
        registerCommand()
    }

    override fun onEnable() {
        CommandAPI.onEnable()
    }

    override fun onDisable() {
        CommandAPI.onDisable()
    }

    private fun registerCommand() {
        commandTree("example") {
            literalArgument("integer") {
                integerArgument("value") {
                    anyExecutor { sender, args ->
                        val value = args.get("value") as Int
                        sender.sendMessage("You entered: $value")
                    }
                }
            }
        }
    }
}
