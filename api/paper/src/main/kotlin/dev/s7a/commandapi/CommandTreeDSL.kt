package dev.s7a.commandapi

import com.destroystokyo.paper.profile.PlayerProfile
import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.AsyncPlayerProfileArgument
import dev.jorel.commandapi.arguments.ChatArgument
import dev.jorel.commandapi.arguments.ChatColorArgument
import dev.jorel.commandapi.arguments.ChatComponentArgument
import dev.jorel.commandapi.arguments.PlayerProfileArgument
import dev.jorel.commandapi.arguments.RecipeArgument
import dev.jorel.commandapi.executors.CommandArguments
import net.kyori.adventure.chat.SignedMessage
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import org.bukkit.inventory.Recipe
import java.util.concurrent.CompletableFuture

inline fun CommandTree.chatColorArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor) -> Unit = {},
) = argument(ChatColorArgument(nodeName), block)

inline fun CommandTree.chatColorOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor?) -> Unit = {},
) = optionalArgument(ChatColorArgument(nodeName), block)

inline fun CommandTree.chatComponentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component) -> Unit = {},
) = argument(ChatComponentArgument(nodeName), block)

inline fun CommandTree.chatComponentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component?) -> Unit = {},
) = optionalArgument(ChatComponentArgument(nodeName), block)

inline fun CommandTree.chatArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> SignedMessage) -> Unit = {},
) = argument(ChatArgument(nodeName), block)

inline fun CommandTree.chatOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> SignedMessage?) -> Unit = {},
) = optionalArgument(ChatArgument(nodeName), block)

inline fun CommandTree.playerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>) -> Unit = {},
) = argument(PlayerProfileArgument(nodeName), block)

inline fun CommandTree.playerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>?) -> Unit = {},
) = optionalArgument(PlayerProfileArgument(nodeName), block)

inline fun CommandTree.asyncPlayerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>) -> Unit = {},
) = argument(AsyncPlayerProfileArgument(nodeName), block)

inline fun CommandTree.asyncPlayerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>?) -> Unit = {},
) = optionalArgument(AsyncPlayerProfileArgument(nodeName), block)

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
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor) -> Unit = {},
) = argument(ChatColorArgument(nodeName), block)

inline fun Argument<*>.chatColorOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor?) -> Unit = {},
) = optionalArgument(ChatColorArgument(nodeName), block)

inline fun Argument<*>.chatComponentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component) -> Unit = {},
) = argument(ChatComponentArgument(nodeName), block)

inline fun Argument<*>.chatComponentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component?) -> Unit = {},
) = optionalArgument(ChatComponentArgument(nodeName), block)

inline fun Argument<*>.chatArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> SignedMessage) -> Unit = {},
) = argument(ChatArgument(nodeName), block)

inline fun Argument<*>.chatOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> SignedMessage?) -> Unit = {},
) = optionalArgument(ChatArgument(nodeName), block)

inline fun Argument<*>.playerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>) -> Unit = {},
) = argument(PlayerProfileArgument(nodeName), block)

inline fun Argument<*>.playerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> List<PlayerProfile>?) -> Unit = {},
) = optionalArgument(PlayerProfileArgument(nodeName), block)

inline fun Argument<*>.asyncPlayerProfileArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>) -> Unit = {},
) = argument(AsyncPlayerProfileArgument(nodeName), block)

inline fun Argument<*>.asyncPlayerProfileOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<List<PlayerProfile>>?) -> Unit = {},
) = optionalArgument(AsyncPlayerProfileArgument(nodeName), block)

inline fun Argument<*>.recipeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe) -> Unit = {},
) = argument(RecipeArgument(nodeName), block)

inline fun Argument<*>.recipeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe?) -> Unit = {},
) = optionalArgument(RecipeArgument(nodeName), block)
