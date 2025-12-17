@file:Suppress("DEPRECATION")

package dev.s7a.commandapi

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.AsyncPlayerProfileArgument
import dev.jorel.commandapi.arguments.ChatArgument
import dev.jorel.commandapi.arguments.ChatColorArgument
import dev.jorel.commandapi.arguments.ChatComponentArgument
import dev.jorel.commandapi.arguments.PlayerProfileArgument
import dev.jorel.commandapi.arguments.RecipeArgument
import dev.jorel.commandapi.executors.CommandArguments
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.ChatColor
import org.bukkit.inventory.Recipe
import org.bukkit.profile.PlayerProfile
import java.util.concurrent.CompletableFuture

inline fun CommandTree.chatColorArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ChatColor) -> Unit = {},
) = argument(ChatColorArgument(nodeName), block)

inline fun CommandTree.chatColorOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ChatColor?) -> Unit = {},
) = optionalArgument(ChatColorArgument(nodeName), block)

inline fun CommandTree.chatComponentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>) -> Unit = {},
) = argument(ChatComponentArgument(nodeName), block)

inline fun CommandTree.chatComponentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>?) -> Unit = {},
) = optionalArgument(ChatComponentArgument(nodeName), block)

inline fun CommandTree.chatArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>) -> Unit = {},
) = argument(ChatArgument(nodeName), block)

inline fun CommandTree.chatOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>?) -> Unit = {},
) = optionalArgument(ChatArgument(nodeName), block)

inline fun CommandTree.playerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>) -> Unit = {},
) = argument(PlayerProfileArgument(nodeName), block)

inline fun CommandTree.playerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>?) -> Unit = {},
) = optionalArgument(PlayerProfileArgument(nodeName).setOptional(true), block)

inline fun CommandTree.asyncPlayerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>) -> Unit = {},
) = argument(AsyncPlayerProfileArgument(nodeName), block)

inline fun CommandTree.asyncPlayerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>?) -> Unit = {},
) = optionalArgument(AsyncPlayerProfileArgument(nodeName).setOptional(true), block)

inline fun CommandTree.recipeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe) -> Unit = {},
) = argument(RecipeArgument(nodeName), block)

inline fun CommandTree.recipeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe?) -> Unit = {},
) = optionalArgument(RecipeArgument(nodeName), block)

inline fun Argument<*>.chatColorArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ChatColor) -> Unit = {},
) = argument(ChatColorArgument(nodeName), block)

inline fun Argument<*>.chatColorOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ChatColor?) -> Unit = {},
) = optionalArgument(ChatColorArgument(nodeName), block)

inline fun Argument<*>.chatComponentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>) -> Unit = {},
) = argument(ChatComponentArgument(nodeName), block)

inline fun Argument<*>.chatComponentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>?) -> Unit = {},
) = optionalArgument(ChatComponentArgument(nodeName), block)

inline fun Argument<*>.chatArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>) -> Unit = {},
) = argument(ChatArgument(nodeName), block)

inline fun Argument<*>.chatOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<BaseComponent>?) -> Unit = {},
) = optionalArgument(ChatArgument(nodeName), block)

inline fun Argument<*>.playerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>) -> Unit = {},
) = argument(PlayerProfileArgument(nodeName), block)

inline fun Argument<*>.playerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>?) -> Unit = {},
) = optionalArgument(PlayerProfileArgument(nodeName).setOptional(true), block)

inline fun Argument<*>.asyncPlayerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>) -> Unit = {},
) = argument(AsyncPlayerProfileArgument(nodeName), block)

inline fun Argument<*>.asyncPlayerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>?) -> Unit = {},
) = optionalArgument(AsyncPlayerProfileArgument(nodeName).setOptional(true), block)

inline fun Argument<*>.recipeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe) -> Unit = {},
) = argument(RecipeArgument(nodeName), block)

inline fun Argument<*>.recipeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe?) -> Unit = {},
) = optionalArgument(RecipeArgument(nodeName), block)
