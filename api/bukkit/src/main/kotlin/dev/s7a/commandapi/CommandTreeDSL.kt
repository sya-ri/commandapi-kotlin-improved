@file:Suppress("DEPRECATION")

package dev.s7a.commandapi

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.AdvancementArgument
import dev.jorel.commandapi.arguments.AdventureChatArgument
import dev.jorel.commandapi.arguments.AdventureChatColorArgument
import dev.jorel.commandapi.arguments.AdventureChatComponentArgument
import dev.jorel.commandapi.arguments.AngleArgument
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.AsyncOfflinePlayerArgument
import dev.jorel.commandapi.arguments.AxisArgument
import dev.jorel.commandapi.arguments.BiomeArgument
import dev.jorel.commandapi.arguments.BlockPredicateArgument
import dev.jorel.commandapi.arguments.BlockStateArgument
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.arguments.ChatArgument
import dev.jorel.commandapi.arguments.ChatColorArgument
import dev.jorel.commandapi.arguments.ChatComponentArgument
import dev.jorel.commandapi.arguments.CommandArgument
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.EnchantmentArgument
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.EntityTypeArgument
import dev.jorel.commandapi.arguments.FloatArgument
import dev.jorel.commandapi.arguments.FloatRangeArgument
import dev.jorel.commandapi.arguments.FunctionArgument
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.IntegerRangeArgument
import dev.jorel.commandapi.arguments.ItemStackArgument
import dev.jorel.commandapi.arguments.ItemStackPredicateArgument
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.arguments.Location2DArgument
import dev.jorel.commandapi.arguments.LocationArgument
import dev.jorel.commandapi.arguments.LocationType
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.arguments.LootTableArgument
import dev.jorel.commandapi.arguments.MathOperationArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.NBTCompoundArgument
import dev.jorel.commandapi.arguments.NamespacedKeyArgument
import dev.jorel.commandapi.arguments.ObjectiveArgument
import dev.jorel.commandapi.arguments.ObjectiveCriteriaArgument
import dev.jorel.commandapi.arguments.OfflinePlayerArgument
import dev.jorel.commandapi.arguments.ParticleArgument
import dev.jorel.commandapi.arguments.PlayerArgument
import dev.jorel.commandapi.arguments.PotionEffectArgument
import dev.jorel.commandapi.arguments.RecipeArgument
import dev.jorel.commandapi.arguments.RotationArgument
import dev.jorel.commandapi.arguments.ScoreHolderArgument
import dev.jorel.commandapi.arguments.ScoreboardSlotArgument
import dev.jorel.commandapi.arguments.SoundArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.arguments.TimeArgument
import dev.jorel.commandapi.arguments.UUIDArgument
import dev.jorel.commandapi.arguments.WorldArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.wrappers.CommandResult
import dev.jorel.commandapi.wrappers.FloatRange
import dev.jorel.commandapi.wrappers.FunctionWrapper
import dev.jorel.commandapi.wrappers.IntegerRange
import dev.jorel.commandapi.wrappers.Location2D
import dev.jorel.commandapi.wrappers.MathOperation
import dev.jorel.commandapi.wrappers.ParticleData
import dev.jorel.commandapi.wrappers.Rotation
import dev.jorel.commandapi.wrappers.ScoreboardSlot
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.NamedTextColor
import net.md_5.bungee.api.chat.BaseComponent
import org.bukkit.Axis
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.NamespacedKey
import org.bukkit.OfflinePlayer
import org.bukkit.Sound
import org.bukkit.World
import org.bukkit.advancement.Advancement
import org.bukkit.block.Biome
import org.bukkit.block.Block
import org.bukkit.block.data.BlockData
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Entity
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack
import org.bukkit.inventory.Recipe
import org.bukkit.loot.LootTable
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.potion.PotionEffectType
import org.bukkit.scoreboard.Objective
import org.bukkit.scoreboard.Team
import java.util.EnumSet
import java.util.UUID
import java.util.concurrent.CompletableFuture
import java.util.function.Predicate

inline fun commandTree(
    name: String,
    tree: CommandTree.() -> Unit = {},
) = CommandTree(name).apply(tree).register()

inline fun commandTree(
    name: String,
    namespace: String,
    tree: CommandTree.() -> Unit = {
    },
) = CommandTree(name).apply(tree).register(namespace)

inline fun commandTree(
    name: String,
    namespace: JavaPlugin,
    tree: CommandTree.() -> Unit = {
    },
) = CommandTree(name).apply(tree).register(namespace)

inline fun <reified T, reified U : T> CommandTree.argument(
    base: Argument<T>,
    crossinline block: Argument<*>.((CommandArguments) -> U) -> Unit = {},
): CommandTree =
    then(
        base.apply {
            this.block { args ->
                args.get(base.nodeName) as U
            }
        },
    )

inline fun <reified T, reified U : T> CommandTree.optionalArgument(
    base: Argument<T>,
    crossinline block: Argument<*>.((CommandArguments) -> U?) -> Unit = {},
) = argument<T, U>(base.setOptional(true), block)

inline fun CommandTree.integerArgument(
    nodeName: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Int) -> Unit = {},
) = argument(IntegerArgument(nodeName, min, max), block)

inline fun CommandTree.integerOptionalArgument(
    nodeName: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Int?) -> Unit = {},
) = optionalArgument(IntegerArgument(nodeName, min, max), block)

inline fun CommandTree.integerRangeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> IntegerRange) -> Unit = {},
) = argument(IntegerRangeArgument(nodeName), block)

inline fun CommandTree.integerRangeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> IntegerRange?) -> Unit = {},
) = optionalArgument(IntegerRangeArgument(nodeName), block)

inline fun CommandTree.floatArgument(
    nodeName: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Float) -> Unit = {},
) = argument(FloatArgument(nodeName, min, max), block)

inline fun CommandTree.floatOptionalArgument(
    nodeName: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Float?) -> Unit = {},
) = optionalArgument(FloatArgument(nodeName, min, max), block)

inline fun CommandTree.floatRangeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> FloatRange) -> Unit = {},
) = argument(FloatRangeArgument(nodeName), block)

inline fun CommandTree.floatRangeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> FloatRange?) -> Unit = {},
) = optionalArgument(FloatRangeArgument(nodeName), block)

inline fun CommandTree.doubleArgument(
    nodeName: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Double) -> Unit = {},
) = argument(DoubleArgument(nodeName, min, max), block)

inline fun CommandTree.doubleOptionalArgument(
    nodeName: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Double?) -> Unit = {},
) = optionalArgument(DoubleArgument(nodeName, min, max), block)

inline fun CommandTree.longArgument(
    nodeName: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Long) -> Unit = {},
) = argument(LongArgument(nodeName, min, max), block)

inline fun CommandTree.longOptionalArgument(
    nodeName: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Long?) -> Unit = {},
) = optionalArgument(LongArgument(nodeName, min, max), block)

inline fun CommandTree.booleanArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Boolean) -> Unit = {},
) = argument(BooleanArgument(nodeName), block)

inline fun CommandTree.booleanOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Boolean?) -> Unit = {},
) = optionalArgument(BooleanArgument(nodeName), block)

inline fun CommandTree.stringArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(StringArgument(nodeName), block)

inline fun CommandTree.stringOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(StringArgument(nodeName), block)

inline fun CommandTree.textArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(TextArgument(nodeName), block)

inline fun CommandTree.textOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(TextArgument(nodeName), block)

inline fun CommandTree.greedyStringArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(GreedyStringArgument(nodeName), block)

inline fun CommandTree.greedyStringOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(GreedyStringArgument(nodeName), block)

inline fun CommandTree.locationArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location) -> Unit = {},
) = argument(LocationArgument(nodeName, locationType, centerPosition), block)

inline fun CommandTree.locationOptionalArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location?) -> Unit = {},
) = optionalArgument(LocationArgument(nodeName, locationType, centerPosition), block)

inline fun CommandTree.location2DArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location2D) -> Unit = {},
) = argument(Location2DArgument(nodeName, locationType, centerPosition), block)

inline fun CommandTree.location2DOptionalArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location2D?) -> Unit = {},
) = optionalArgument(Location2DArgument(nodeName, locationType, centerPosition), block)

inline fun CommandTree.rotationArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Rotation) -> Unit = {},
) = argument(RotationArgument(nodeName), block)

inline fun CommandTree.rotationOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Rotation?) -> Unit = {},
) = optionalArgument(RotationArgument(nodeName), block)

inline fun CommandTree.axisArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EnumSet<Axis>) -> Unit = {},
) = argument(AxisArgument(nodeName), block)

inline fun CommandTree.axisOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EnumSet<Axis>?) -> Unit = {},
) = optionalArgument(AxisArgument(nodeName), block)

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

inline fun CommandTree.adventureChatColorArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor) -> Unit = {},
) = argument(AdventureChatColorArgument(nodeName), block)

inline fun CommandTree.adventureChatColorOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor?) -> Unit = {},
) = optionalArgument(AdventureChatColorArgument(nodeName), block)

inline fun CommandTree.adventureChatComponentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component) -> Unit = {},
) = argument(AdventureChatComponentArgument(nodeName), block)

inline fun CommandTree.adventureChatComponentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component?) -> Unit = {},
) = optionalArgument(AdventureChatComponentArgument(nodeName), block)

inline fun CommandTree.adventureChatArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component) -> Unit = {},
) = argument(AdventureChatArgument(nodeName), block)

inline fun CommandTree.adventureChatOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component?) -> Unit = {},
) = optionalArgument(AdventureChatArgument(nodeName), block)

inline fun CommandTree.entitySelectorArgumentOneEntity(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Entity) -> Unit = {},
) = argument(EntitySelectorArgument.OneEntity(nodeName), block)

inline fun CommandTree.entitySelectorOptionalArgumentOneEntity(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Entity?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.OneEntity(nodeName), block)

inline fun CommandTree.entitySelectorArgumentManyEntities(
    nodeName: String,
    allowEmpty: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Entity>) -> Unit = {},
) = argument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)

inline fun CommandTree.entitySelectorOptionalArgumentManyEntities(
    nodeName: String,
    allowEmpty: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Entity>?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)

inline fun CommandTree.entitySelectorArgumentOnePlayer(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player) -> Unit = {},
) = argument(EntitySelectorArgument.OnePlayer(nodeName), block)

inline fun CommandTree.entitySelectorOptionalArgumentOnePlayer(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.OnePlayer(nodeName), block)

inline fun CommandTree.entitySelectorArgumentManyPlayers(
    nodeName: String,
    allowEmpty: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Player>) -> Unit = {},
) = argument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), block)

inline fun CommandTree.entitySelectorOptionalArgumentManyPlayers(
    nodeName: String,
    allowEmpty: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Player>?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.ManyPlayers(nodeName, allowEmpty), block)

inline fun CommandTree.playerArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player) -> Unit = {},
) = argument(PlayerArgument(nodeName), block)

inline fun CommandTree.playerOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player?) -> Unit = {},
) = optionalArgument(PlayerArgument(nodeName), block)

inline fun CommandTree.offlinePlayerArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> OfflinePlayer) -> Unit = {},
) = argument(OfflinePlayerArgument(nodeName), block)

inline fun CommandTree.offlinePlayerOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> OfflinePlayer?) -> Unit = {},
) = optionalArgument(OfflinePlayerArgument(nodeName), block)

inline fun CommandTree.asyncOfflinePlayerArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<OfflinePlayer>) -> Unit = {},
) = argument(AsyncOfflinePlayerArgument(nodeName), block)

inline fun CommandTree.asyncOfflinePlayerOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<OfflinePlayer>?) -> Unit = {},
) = optionalArgument(AsyncOfflinePlayerArgument(nodeName), block)

inline fun CommandTree.entityTypeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EntityType) -> Unit = {},
) = argument(EntityTypeArgument(nodeName), block)

inline fun CommandTree.entityTypeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EntityType?) -> Unit = {},
) = optionalArgument(EntityTypeArgument(nodeName), block)

inline fun CommandTree.scoreHolderArgumentSingle(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(ScoreHolderArgument.Single(nodeName), block)

inline fun CommandTree.scoreHolderOptionalArgumentSingle(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(ScoreHolderArgument.Single(nodeName), block)

inline fun CommandTree.scoreHolderArgumentMultiple(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<String>) -> Unit = {},
) = argument(ScoreHolderArgument.Multiple(nodeName), block)

inline fun CommandTree.scoreHolderOptionalArgumentMultiple(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<String>?) -> Unit = {},
) = optionalArgument(ScoreHolderArgument.Multiple(nodeName), block)

inline fun CommandTree.scoreboardSlotArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ScoreboardSlot) -> Unit = {},
) = argument(ScoreboardSlotArgument(nodeName), block)

inline fun CommandTree.scoreboardSlotOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ScoreboardSlot?) -> Unit = {},
) = optionalArgument(ScoreboardSlotArgument(nodeName), block)

inline fun CommandTree.objectiveArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Objective) -> Unit = {},
) = argument(ObjectiveArgument(nodeName), block)

inline fun CommandTree.objectiveOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Objective?) -> Unit = {},
) = optionalArgument(ObjectiveArgument(nodeName), block)

inline fun CommandTree.objectiveCriteriaArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(ObjectiveCriteriaArgument(nodeName), block)

inline fun CommandTree.objectiveCriteriaOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(ObjectiveCriteriaArgument(nodeName), block)

inline fun CommandTree.teamArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Team) -> Unit = {},
) = argument(ObjectiveArgument(nodeName), block)

inline fun CommandTree.teamOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Team?) -> Unit = {},
) = optionalArgument(ObjectiveArgument(nodeName), block)

inline fun CommandTree.angleArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Float) -> Unit = {},
) = argument(AngleArgument(nodeName), block)

inline fun CommandTree.angleOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Float?) -> Unit = {},
) = optionalArgument(AngleArgument(nodeName), block)

inline fun CommandTree.advancementArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Advancement) -> Unit = {},
) = argument(AdvancementArgument(nodeName), block)

inline fun CommandTree.advancementOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Advancement?) -> Unit = {},
) = optionalArgument(AdvancementArgument(nodeName), block)

inline fun CommandTree.biomeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Biome) -> Unit = {},
) = argument(BiomeArgument(nodeName), block)

inline fun CommandTree.biomeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Biome?) -> Unit = {},
) = optionalArgument(BiomeArgument(nodeName), block)

inline fun CommandTree.biomeNamespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(BiomeArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.biomeNamespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(BiomeArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.blockStateArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> BlockData) -> Unit = {},
) = argument(BlockStateArgument(nodeName), block)

inline fun CommandTree.blockStateOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> BlockData?) -> Unit = {},
) = optionalArgument(BlockStateArgument(nodeName), block)

inline fun CommandTree.commandArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CommandResult) -> Unit = {},
) = argument(CommandArgument(nodeName), block)

inline fun CommandTree.commandOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CommandResult?) -> Unit = {},
) = optionalArgument(CommandArgument(nodeName), block)

inline fun CommandTree.enchantmentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Enchantment) -> Unit = {},
) = argument(EnchantmentArgument(nodeName), block)

inline fun CommandTree.enchantmentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Enchantment?) -> Unit = {},
) = optionalArgument(EnchantmentArgument(nodeName), block)

inline fun CommandTree.itemStackArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ItemStack) -> Unit = {},
) = argument(ItemStackArgument(nodeName), block)

inline fun CommandTree.itemStackOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ItemStack?) -> Unit = {},
) = optionalArgument(ItemStackArgument(nodeName), block)

inline fun CommandTree.lootTableArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> LootTable) -> Unit = {},
) = argument(LootTableArgument(nodeName), block)

inline fun CommandTree.lootTableOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> LootTable?) -> Unit = {},
) = optionalArgument(LootTableArgument(nodeName), block)

inline fun CommandTree.mathOperationArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> MathOperation) -> Unit = {},
) = argument(MathOperationArgument(nodeName), block)

inline fun CommandTree.mathOperationOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> MathOperation?) -> Unit = {},
) = optionalArgument(MathOperationArgument(nodeName), block)

inline fun CommandTree.namespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(NamespacedKeyArgument(nodeName), block)

inline fun CommandTree.namespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(NamespacedKeyArgument(nodeName), block)

inline fun CommandTree.particleArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ParticleData<*>) -> Unit = {},
) = argument(ParticleArgument(nodeName), block)

inline fun CommandTree.particleOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ParticleData<*>?) -> Unit = {},
) = optionalArgument(ParticleArgument(nodeName), block)

inline fun CommandTree.potionEffectArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> PotionEffectType) -> Unit = {},
) = argument(PotionEffectArgument(nodeName), block)

inline fun CommandTree.potionEffectOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> PotionEffectType?) -> Unit = {},
) = optionalArgument(PotionEffectArgument(nodeName), block)

inline fun CommandTree.potionEffectNamespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(PotionEffectArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.potionEffectNamespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(PotionEffectArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.recipeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe) -> Unit = {},
) = argument(RecipeArgument(nodeName), block)

inline fun CommandTree.recipeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe?) -> Unit = {},
) = optionalArgument(RecipeArgument(nodeName), block)

inline fun CommandTree.soundArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Sound) -> Unit = {},
) = argument(SoundArgument(nodeName), block)

inline fun CommandTree.soundOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Sound?) -> Unit = {},
) = optionalArgument(SoundArgument(nodeName), block)

inline fun CommandTree.soundNamespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(SoundArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.soundNamespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(SoundArgument.NamespacedKey(nodeName), block)

inline fun CommandTree.timeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Int) -> Unit = {},
) = argument(TimeArgument(nodeName), block)

inline fun CommandTree.timeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Int?) -> Unit = {},
) = optionalArgument(TimeArgument(nodeName), block)

inline fun CommandTree.uuidArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> UUID) -> Unit = {},
) = argument(UUIDArgument(nodeName), block)

inline fun CommandTree.uuidOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> UUID?) -> Unit = {},
) = optionalArgument(UUIDArgument(nodeName), block)

inline fun CommandTree.worldArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> World) -> Unit = {},
) = argument(WorldArgument(nodeName), block)

inline fun CommandTree.worldOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> World?) -> Unit = {},
) = optionalArgument(WorldArgument(nodeName), block)

inline fun CommandTree.blockPredicateArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<Block>) -> Unit = {},
) = argument(BlockPredicateArgument(nodeName), block)

inline fun CommandTree.blockPredicateOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<Block>?) -> Unit = {},
) = optionalArgument(BlockPredicateArgument(nodeName), block)

inline fun CommandTree.itemStackPredicateArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<ItemStack>) -> Unit = {},
) = argument(ItemStackPredicateArgument(nodeName), block)

inline fun CommandTree.itemStackPredicateOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<ItemStack>?) -> Unit = {},
) = optionalArgument(ItemStackPredicateArgument(nodeName), block)

inline fun <reified NBTContainer> CommandTree.nbtCompoundArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NBTContainer) -> Unit = {},
) = argument(NBTCompoundArgument(nodeName), block)

inline fun <reified NBTContainer> CommandTree.nbtCompoundOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NBTContainer?) -> Unit = {},
) = optionalArgument(NBTCompoundArgument(nodeName), block)

inline fun CommandTree.literalArgument(
    nodeName: String,
    literal: String = nodeName,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(LiteralArgument.of(nodeName, literal), block)

inline fun CommandTree.literalOptionalArgument(
    nodeName: String,
    literal: String = nodeName,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(LiteralArgument.of(nodeName, literal), block)

inline fun CommandTree.multiLiteralArgument(
    nodeName: String,
    vararg literals: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(MultiLiteralArgument(nodeName, *literals), block)

inline fun CommandTree.multiLiteralOptionalArgument(
    nodeName: String,
    vararg literals: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(MultiLiteralArgument(nodeName, *literals), block)

inline fun CommandTree.functionArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<FunctionWrapper>) -> Unit = {},
) = argument(FunctionArgument(nodeName), block)

inline fun CommandTree.functionOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<FunctionWrapper>?) -> Unit = {},
) = optionalArgument(FunctionArgument(nodeName), block)

inline fun <reified T, reified U : T> Argument<*>.argument(
    base: Argument<T>,
    crossinline block: Argument<*>.((CommandArguments) -> U) -> Unit = {},
): Argument<*> =
    then(
        base.apply {
            this.block { args ->
                args.get(base.nodeName) as U
            }
        },
    )

inline fun <reified T, reified U : T> Argument<*>.optionalArgument(
    base: Argument<T>,
    crossinline block: Argument<*>.((CommandArguments) -> U?) -> Unit = {},
) = argument<T, U>(base.setOptional(true), block)

inline fun Argument<*>.integerArgument(
    nodeName: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Int) -> Unit = {},
) = argument(IntegerArgument(nodeName, min, max), block)

inline fun Argument<*>.integerOptionalArgument(
    nodeName: String,
    min: Int = Int.MIN_VALUE,
    max: Int = Int.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Int?) -> Unit = {},
) = optionalArgument(IntegerArgument(nodeName, min, max), block)

inline fun Argument<*>.integerRangeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> IntegerRange) -> Unit = {},
) = argument(IntegerRangeArgument(nodeName), block)

inline fun Argument<*>.integerRangeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> IntegerRange?) -> Unit = {},
) = optionalArgument(IntegerRangeArgument(nodeName), block)

inline fun Argument<*>.floatArgument(
    nodeName: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Float) -> Unit = {},
) = argument(FloatArgument(nodeName, min, max), block)

inline fun Argument<*>.floatOptionalArgument(
    nodeName: String,
    min: Float = -Float.MAX_VALUE,
    max: Float = Float.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Float?) -> Unit = {},
) = optionalArgument(FloatArgument(nodeName, min, max), block)

inline fun Argument<*>.floatRangeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> FloatRange) -> Unit = {},
) = argument(FloatRangeArgument(nodeName), block)

inline fun Argument<*>.floatRangeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> FloatRange?) -> Unit = {},
) = optionalArgument(FloatRangeArgument(nodeName), block)

inline fun Argument<*>.doubleArgument(
    nodeName: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Double) -> Unit = {},
) = argument(DoubleArgument(nodeName, min, max), block)

inline fun Argument<*>.doubleOptionalArgument(
    nodeName: String,
    min: Double = -Double.MAX_VALUE,
    max: Double = Double.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Double?) -> Unit = {},
) = optionalArgument(DoubleArgument(nodeName, min, max), block)

inline fun Argument<*>.longArgument(
    nodeName: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Long) -> Unit = {},
) = argument(LongArgument(nodeName, min, max), block)

inline fun Argument<*>.longOptionalArgument(
    nodeName: String,
    min: Long = Long.MIN_VALUE,
    max: Long = Long.MAX_VALUE,
    crossinline block: Argument<*>.((CommandArguments) -> Long?) -> Unit = {},
) = optionalArgument(LongArgument(nodeName, min, max), block)

inline fun Argument<*>.booleanArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Boolean) -> Unit = {},
) = argument(BooleanArgument(nodeName), block)

inline fun Argument<*>.booleanOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Boolean?) -> Unit = {},
) = optionalArgument(BooleanArgument(nodeName), block)

inline fun Argument<*>.stringArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(StringArgument(nodeName), block)

inline fun Argument<*>.stringOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(StringArgument(nodeName), block)

inline fun Argument<*>.textArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(TextArgument(nodeName), block)

inline fun Argument<*>.textOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(TextArgument(nodeName), block)

inline fun Argument<*>.greedyStringArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(GreedyStringArgument(nodeName), block)

inline fun Argument<*>.greedyStringOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(GreedyStringArgument(nodeName), block)

inline fun Argument<*>.locationArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location) -> Unit = {},
) = argument(LocationArgument(nodeName, locationType, centerPosition), block)

inline fun Argument<*>.locationOptionalArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location?) -> Unit = {},
) = optionalArgument(LocationArgument(nodeName, locationType, centerPosition), block)

inline fun Argument<*>.location2DArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location2D) -> Unit = {},
) = argument(Location2DArgument(nodeName, locationType, centerPosition), block)

inline fun Argument<*>.location2DOptionalArgument(
    nodeName: String,
    locationType: LocationType = LocationType.PRECISE_POSITION,
    centerPosition: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Location2D?) -> Unit = {},
) = optionalArgument(Location2DArgument(nodeName, locationType, centerPosition), block)

inline fun Argument<*>.rotationArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Rotation) -> Unit = {},
) = argument(RotationArgument(nodeName), block)

inline fun Argument<*>.rotationOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Rotation?) -> Unit = {},
) = optionalArgument(RotationArgument(nodeName), block)

inline fun Argument<*>.axisArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EnumSet<Axis>) -> Unit = {},
) = argument(AxisArgument(nodeName), block)

inline fun Argument<*>.axisOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EnumSet<Axis>?) -> Unit = {},
) = optionalArgument(AxisArgument(nodeName), block)

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

inline fun Argument<*>.adventureChatColorArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor) -> Unit = {},
) = argument(AdventureChatColorArgument(nodeName), block)

inline fun Argument<*>.adventureChatColorOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamedTextColor?) -> Unit = {},
) = optionalArgument(AdventureChatColorArgument(nodeName), block)

inline fun Argument<*>.adventureChatComponentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component) -> Unit = {},
) = argument(AdventureChatComponentArgument(nodeName), block)

inline fun Argument<*>.adventureChatComponentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component?) -> Unit = {},
) = optionalArgument(AdventureChatComponentArgument(nodeName), block)

inline fun Argument<*>.adventureChatArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component) -> Unit = {},
) = argument(AdventureChatArgument(nodeName), block)

inline fun Argument<*>.adventureChatOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Component?) -> Unit = {},
) = optionalArgument(AdventureChatArgument(nodeName), block)

inline fun Argument<*>.entitySelectorArgumentOneEntity(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Entity) -> Unit = {},
) = argument(EntitySelectorArgument.OneEntity(nodeName), block)

inline fun Argument<*>.entitySelectorOptionalArgumentOneEntity(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Entity?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.OneEntity(nodeName), block)

inline fun Argument<*>.entitySelectorArgumentManyEntities(
    nodeName: String,
    allowEmpty: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Entity>) -> Unit = {},
) = argument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)

inline fun Argument<*>.entitySelectorOptionalArgumentManyEntities(
    nodeName: String,
    allowEmpty: Boolean = true,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Entity>?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.ManyEntities(nodeName, allowEmpty), block)

inline fun Argument<*>.entitySelectorArgumentOnePlayer(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player) -> Unit = {},
) = argument(EntitySelectorArgument.OnePlayer(nodeName), block)

inline fun Argument<*>.entitySelectorOptionalArgumentOnePlayer(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.OnePlayer(nodeName), block)

inline fun Argument<*>.entitySelectorArgumentManyPlayers(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Player>) -> Unit = {},
) = argument(EntitySelectorArgument.ManyPlayers(nodeName), block)

inline fun Argument<*>.entitySelectorOptionalArgumentManyPlayers(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<Player>?) -> Unit = {},
) = optionalArgument(EntitySelectorArgument.ManyPlayers(nodeName), block)

inline fun Argument<*>.playerArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player) -> Unit = {},
) = argument(PlayerArgument(nodeName), block)

inline fun Argument<*>.playerOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Player?) -> Unit = {},
) = optionalArgument(PlayerArgument(nodeName), block)

inline fun Argument<*>.offlinePlayerArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> OfflinePlayer) -> Unit = {},
) = argument(OfflinePlayerArgument(nodeName), block)

inline fun Argument<*>.offlinePlayerOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> OfflinePlayer?) -> Unit = {},
) = optionalArgument(OfflinePlayerArgument(nodeName), block)

inline fun Argument<*>.asyncOfflinePlayerArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<OfflinePlayer>) -> Unit = {},
) = argument(AsyncOfflinePlayerArgument(nodeName), block)

inline fun Argument<*>.asyncOfflinePlayerOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CompletableFuture<OfflinePlayer>?) -> Unit = {},
) = optionalArgument(AsyncOfflinePlayerArgument(nodeName), block)

inline fun Argument<*>.entityTypeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EntityType) -> Unit = {},
) = argument(EntityTypeArgument(nodeName), block)

inline fun Argument<*>.entityTypeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> EntityType?) -> Unit = {},
) = optionalArgument(EntityTypeArgument(nodeName), block)

inline fun Argument<*>.scoreHolderArgumentSingle(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(ScoreHolderArgument.Single(nodeName), block)

inline fun Argument<*>.scoreHolderOptionalArgumentSingle(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(ScoreHolderArgument.Single(nodeName), block)

inline fun Argument<*>.scoreHolderArgumentMultiple(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<String>) -> Unit = {},
) = argument(ScoreHolderArgument.Multiple(nodeName), block)

inline fun Argument<*>.scoreHolderOptionalArgumentMultiple(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Collection<String>?) -> Unit = {},
) = optionalArgument(ScoreHolderArgument.Multiple(nodeName), block)

inline fun Argument<*>.scoreboardSlotArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ScoreboardSlot) -> Unit = {},
) = argument(ScoreboardSlotArgument(nodeName), block)

inline fun Argument<*>.scoreboardSlotOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ScoreboardSlot?) -> Unit = {},
) = optionalArgument(ScoreboardSlotArgument(nodeName), block)

inline fun Argument<*>.objectiveArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Objective) -> Unit = {},
) = argument(ObjectiveArgument(nodeName), block)

inline fun Argument<*>.objectiveOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Objective?) -> Unit = {},
) = optionalArgument(ObjectiveArgument(nodeName), block)

inline fun Argument<*>.objectiveCriteriaArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(ObjectiveCriteriaArgument(nodeName), block)

inline fun Argument<*>.objectiveCriteriaOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(ObjectiveCriteriaArgument(nodeName), block)

inline fun Argument<*>.teamArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Team) -> Unit = {},
) = argument(ObjectiveArgument(nodeName), block)

inline fun Argument<*>.teamOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Team?) -> Unit = {},
) = optionalArgument(ObjectiveArgument(nodeName), block)

inline fun Argument<*>.angleArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Float) -> Unit = {},
) = argument(AngleArgument(nodeName), block)

inline fun Argument<*>.angleOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Float?) -> Unit = {},
) = optionalArgument(AngleArgument(nodeName), block)

inline fun Argument<*>.advancementArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Advancement) -> Unit = {},
) = argument(AdvancementArgument(nodeName), block)

inline fun Argument<*>.advancementOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Advancement?) -> Unit = {},
) = optionalArgument(AdvancementArgument(nodeName), block)

inline fun Argument<*>.biomeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Biome) -> Unit = {},
) = argument(BiomeArgument(nodeName), block)

inline fun Argument<*>.biomeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Biome?) -> Unit = {},
) = optionalArgument(BiomeArgument(nodeName), block)

inline fun Argument<*>.biomeNamespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(BiomeArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.biomeNamespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(BiomeArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.blockStateArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> BlockData) -> Unit = {},
) = argument(BlockStateArgument(nodeName), block)

inline fun Argument<*>.blockStateOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> BlockData?) -> Unit = {},
) = optionalArgument(BlockStateArgument(nodeName), block)

inline fun Argument<*>.commandArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CommandResult) -> Unit = {},
) = argument(CommandArgument(nodeName), block)

inline fun Argument<*>.commandOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> CommandResult?) -> Unit = {},
) = optionalArgument(CommandArgument(nodeName), block)

inline fun Argument<*>.enchantmentArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Enchantment) -> Unit = {},
) = argument(EnchantmentArgument(nodeName), block)

inline fun Argument<*>.enchantmentOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Enchantment?) -> Unit = {},
) = optionalArgument(EnchantmentArgument(nodeName), block)

inline fun Argument<*>.itemStackArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ItemStack) -> Unit = {},
) = argument(ItemStackArgument(nodeName), block)

inline fun Argument<*>.itemStackOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ItemStack?) -> Unit = {},
) = optionalArgument(ItemStackArgument(nodeName), block)

inline fun Argument<*>.lootTableArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> LootTable) -> Unit = {},
) = argument(LootTableArgument(nodeName), block)

inline fun Argument<*>.lootTableOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> LootTable?) -> Unit = {},
) = optionalArgument(LootTableArgument(nodeName), block)

inline fun Argument<*>.mathOperationArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> MathOperation) -> Unit = {},
) = argument(MathOperationArgument(nodeName), block)

inline fun Argument<*>.mathOperationOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> MathOperation?) -> Unit = {},
) = optionalArgument(MathOperationArgument(nodeName), block)

inline fun Argument<*>.namespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(NamespacedKeyArgument(nodeName), block)

inline fun Argument<*>.namespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(NamespacedKeyArgument(nodeName), block)

inline fun Argument<*>.particleArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ParticleData<*>) -> Unit = {},
) = argument(ParticleArgument(nodeName), block)

inline fun Argument<*>.particleOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> ParticleData<*>?) -> Unit = {},
) = optionalArgument(ParticleArgument(nodeName), block)

inline fun Argument<*>.potionEffectArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> PotionEffectType) -> Unit = {},
) = argument(PotionEffectArgument(nodeName), block)

inline fun Argument<*>.potionEffectOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> PotionEffectType?) -> Unit = {},
) = optionalArgument(PotionEffectArgument(nodeName), block)

inline fun Argument<*>.potionEffectNamespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(PotionEffectArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.potionEffectNamespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(PotionEffectArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.recipeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe) -> Unit = {},
) = argument(RecipeArgument(nodeName), block)

inline fun Argument<*>.recipeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Recipe?) -> Unit = {},
) = optionalArgument(RecipeArgument(nodeName), block)

inline fun Argument<*>.soundArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Sound) -> Unit = {},
) = argument(SoundArgument(nodeName), block)

inline fun Argument<*>.soundOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Sound?) -> Unit = {},
) = optionalArgument(SoundArgument(nodeName), block)

inline fun Argument<*>.soundNamespacedKeyArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey) -> Unit = {},
) = argument(SoundArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.soundNamespacedKeyOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NamespacedKey?) -> Unit = {},
) = optionalArgument(SoundArgument.NamespacedKey(nodeName), block)

inline fun Argument<*>.timeArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Int) -> Unit = {},
) = argument(TimeArgument(nodeName), block)

inline fun Argument<*>.timeOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Int?) -> Unit = {},
) = optionalArgument(TimeArgument(nodeName), block)

inline fun Argument<*>.uuidArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> UUID) -> Unit = {},
) = argument(UUIDArgument(nodeName), block)

inline fun Argument<*>.uuidOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> UUID?) -> Unit = {},
) = optionalArgument(UUIDArgument(nodeName), block)

inline fun Argument<*>.worldArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> World) -> Unit = {},
) = argument(WorldArgument(nodeName), block)

inline fun Argument<*>.worldOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> World?) -> Unit = {},
) = optionalArgument(WorldArgument(nodeName), block)

inline fun Argument<*>.blockPredicateArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<Block>) -> Unit = {},
) = argument(BlockPredicateArgument(nodeName), block)

inline fun Argument<*>.blockPredicateOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<Block>?) -> Unit = {},
) = optionalArgument(BlockPredicateArgument(nodeName), block)

inline fun Argument<*>.itemStackPredicateArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<ItemStack>) -> Unit = {},
) = argument(ItemStackPredicateArgument(nodeName), block)

inline fun Argument<*>.itemStackPredicateOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Predicate<ItemStack>?) -> Unit = {},
) = optionalArgument(ItemStackPredicateArgument(nodeName), block)

inline fun <reified NBTContainer> Argument<*>.nbtCompoundArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NBTContainer) -> Unit = {},
) = argument(NBTCompoundArgument(nodeName), block)

inline fun <reified NBTContainer> Argument<*>.nbtCompoundOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> NBTContainer?) -> Unit = {},
) = optionalArgument(NBTCompoundArgument(nodeName), block)

inline fun Argument<*>.literalArgument(
    nodeName: String,
    literal: String = nodeName,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(LiteralArgument.of(nodeName, literal), block)

inline fun Argument<*>.literalOptionalArgument(
    nodeName: String,
    literal: String = nodeName,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(LiteralArgument.of(nodeName, literal), block)

inline fun Argument<*>.multiLiteralArgument(
    nodeName: String,
    vararg literals: String,
    crossinline block: Argument<*>.((CommandArguments) -> String) -> Unit = {},
) = argument(MultiLiteralArgument(nodeName, *literals), block)

inline fun Argument<*>.multiLiteralOptionalArgument(
    nodeName: String,
    vararg literals: String,
    crossinline block: Argument<*>.((CommandArguments) -> String?) -> Unit = {},
) = optionalArgument(MultiLiteralArgument(nodeName, *literals), block)

inline fun Argument<*>.functionArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<FunctionWrapper>) -> Unit = {},
) = argument(FunctionArgument(nodeName), block)

inline fun Argument<*>.functionOptionalArgument(
    nodeName: String,
    crossinline block: Argument<*>.((CommandArguments) -> Array<FunctionWrapper>?) -> Unit = {},
) = optionalArgument(FunctionArgument(nodeName), block)
