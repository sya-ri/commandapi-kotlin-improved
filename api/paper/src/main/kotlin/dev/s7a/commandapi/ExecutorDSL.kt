@file:JvmName("ExecutorDSLPaper")

package dev.s7a.commandapi

import dev.jorel.commandapi.BukkitExecutable
import dev.jorel.commandapi.commandsenders.BukkitFeedbackForwardingCommandSender
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.ExecutionInfo
import dev.jorel.commandapi.executors.FeedbackForwardingCommandExecutor
import dev.jorel.commandapi.executors.FeedbackForwardingExecutionInfo
import dev.jorel.commandapi.executors.FeedbackForwardingResultingCommandExecutor
import dev.jorel.commandapi.executors.FeedbackForwardingResultingExecutionInfo
import org.bukkit.command.CommandSender

inline fun BukkitExecutable<*>.feedbackForwardingExecutor(
    crossinline executor: (CommandSender, CommandArguments) -> Unit,
): BukkitExecutable<*> =
    executesFeedbackForwarding(
        FeedbackForwardingCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun BukkitExecutable<*>.feedbackForwardingResultingExecutor(
    crossinline executor: (CommandSender, CommandArguments) -> Int,
): BukkitExecutable<*> =
    executesFeedbackForwarding(
        FeedbackForwardingResultingCommandExecutor { sender, args ->
            executor(sender, args)
        },
    )

inline fun BukkitExecutable<*>.feedbackForwardingExecutionInfo(
    crossinline executor: (ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>>) -> Unit,
): BukkitExecutable<*> =
    executesFeedbackForwarding(
        FeedbackForwardingExecutionInfo { info ->
            executor(info)
        },
    )

inline fun BukkitExecutable<*>.feedbackForwardingResultingExecutionInfo(
    crossinline executor: (ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>>) -> Int,
): BukkitExecutable<*> =
    executesFeedbackForwarding(
        FeedbackForwardingResultingExecutionInfo { info ->
            executor(info)
        },
    )
