package dev.s7a.commandapi.example

import dev.jorel.commandapi.CommandAPI
import dev.jorel.commandapi.CommandAPIPaperConfig
import dev.s7a.commandapi.anyExecutor
import dev.s7a.commandapi.commandTree
import dev.s7a.commandapi.integerArgument
import dev.s7a.commandapi.literalArgument
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
                integerArgument("value") { getValue ->
                    anyExecutor { sender, args ->
                        val value = getValue(args)
                        sender.sendMessage("You entered: $value")
                    }
                }
            }
        }
    }
}
