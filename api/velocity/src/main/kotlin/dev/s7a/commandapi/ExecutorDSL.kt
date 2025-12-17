package dev.s7a.commandapi

import com.velocitypowered.api.command.CommandSource
import com.velocitypowered.api.proxy.ConsoleCommandSource
import com.velocitypowered.api.proxy.Player
import dev.jorel.commandapi.VelocityExecutable
import dev.jorel.commandapi.commandsenders.VelocityCommandSender
import dev.jorel.commandapi.commandsenders.VelocityConsoleCommandSender
import dev.jorel.commandapi.commandsenders.VelocityPlayer
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutionInfo
import dev.jorel.commandapi.executors.CommandExecutor
import dev.jorel.commandapi.executors.ConsoleCommandExecutor
import dev.jorel.commandapi.executors.ConsoleExecutionInfo
import dev.jorel.commandapi.executors.ConsoleResultingCommandExecutor
import dev.jorel.commandapi.executors.ConsoleResultingExecutionInfo
import dev.jorel.commandapi.executors.ExecutionInfo
import dev.jorel.commandapi.executors.PlayerCommandExecutor
import dev.jorel.commandapi.executors.PlayerExecutionInfo
import dev.jorel.commandapi.executors.PlayerResultingCommandExecutor
import dev.jorel.commandapi.executors.PlayerResultingExecutionInfo
import dev.jorel.commandapi.executors.ResultingCommandExecutionInfo
import dev.jorel.commandapi.executors.ResultingCommandExecutor

inline fun VelocityExecutable<*>.anyExecutor(crossinline executor: (CommandSource, CommandArguments) -> Unit): VelocityExecutable<*> =
    executes(
        CommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun VelocityExecutable<*>.playerExecutor(crossinline executor: (Player, CommandArguments) -> Unit): VelocityExecutable<*> =
    executesPlayer(
        PlayerCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun VelocityExecutable<*>.consoleExecutor(
    crossinline executor: (ConsoleCommandSource, CommandArguments) -> Unit,
): VelocityExecutable<*> =
    executesConsole(
        ConsoleCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun VelocityExecutable<*>.anyResultingExecutor(
    crossinline executor: (CommandSource, CommandArguments) -> Int,
): VelocityExecutable<*> =
    executes(
        ResultingCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun VelocityExecutable<*>.playerResultingExecutor(crossinline executor: (Player, CommandArguments) -> Int): VelocityExecutable<*> =
    executesPlayer(
        PlayerResultingCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun VelocityExecutable<*>.consoleResultingExecutor(
    crossinline executor: (ConsoleCommandSource, CommandArguments) -> Int,
): VelocityExecutable<*> =
    executesConsole(
        ConsoleResultingCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun VelocityExecutable<*>.anyExecutionInfo(
    crossinline executor: (ExecutionInfo<CommandSource, VelocityCommandSender<out CommandSource>>) -> Unit,
): VelocityExecutable<*> =
    executes(
        CommandExecutionInfo { info ->
            executor(info)
        },
    )

inline fun VelocityExecutable<*>.playerExecutionInfo(
    crossinline executor: (ExecutionInfo<Player, VelocityPlayer>) -> Unit,
): VelocityExecutable<*> =
    executesPlayer(
        PlayerExecutionInfo { info ->
            executor(info)
        },
    )

inline fun VelocityExecutable<*>.consoleExecutionInfo(
    crossinline executor: (ExecutionInfo<ConsoleCommandSource, VelocityConsoleCommandSender>) -> Unit,
): VelocityExecutable<*> =
    executesConsole(
        ConsoleExecutionInfo { info ->
            executor(info)
        },
    )

inline fun VelocityExecutable<*>.anyResultingExecutionInfo(
    crossinline executor: (ExecutionInfo<CommandSource, VelocityCommandSender<out CommandSource>>) -> Int,
): VelocityExecutable<*> =
    executes(
        ResultingCommandExecutionInfo { info ->
            executor(info)
        },
    )

inline fun VelocityExecutable<*>.playerResultingExecutionInfo(
    crossinline executor: (ExecutionInfo<Player, VelocityPlayer>) -> Int,
): VelocityExecutable<*> =
    executesPlayer(
        PlayerResultingExecutionInfo { info ->
            executor(info)
        },
    )

inline fun VelocityExecutable<*>.consoleResultingExecutionInfo(
    crossinline executor: (ExecutionInfo<ConsoleCommandSource, VelocityConsoleCommandSender>) -> Int,
): VelocityExecutable<*> =
    executesConsole(
        ConsoleResultingExecutionInfo { info ->
            executor(info)
        },
    )
