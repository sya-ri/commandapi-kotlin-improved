# API Surface Reference

This reference lists the public DSL helpers exposed by this repository, with the return type for each argument builder and the intended usage pattern.

For nested command trees, the same argument builder names are available on both `CommandTree` and `Argument<*>` unless noted otherwise.

Unless noted otherwise, every `*OptionalArgument` variant returns the same type as the base builder, but nullable.

## Common Usage Pattern

Use argument builders in the improved getter-lambda style:

```kotlin
commandTree("example") {
    integerArgument("value") { getValue ->
        anyExecutor { sender, args ->
            val value = getValue(args)
            sender.sendMessage("value=$value")
        }
    }
}
```

Use optional builders the same way, but handle `null`:

```kotlin
commandTree("example") {
    integerOptionalArgument("value") { getValue ->
        anyExecutor { sender, args ->
            val value = getValue(args)
            sender.sendMessage("value=$value")
        }
    }
}
```

## Bukkit Base

The Bukkit module is the base for both Spigot and Paper.

### Argument Builders

| Builder | Returns | Extra options | How to use |
| --- | --- | --- | --- |
| `integerArgument` | `Int` | `min: Int = Int.MIN_VALUE`, `max: Int = Int.MAX_VALUE` | Use for whole-number command input. Pass `min` and `max` when you need range validation. |
| `integerRangeArgument` | `IntegerRange` | none | Use when the command accepts a Minecraft-style integer range instead of a single number. |
| `floatArgument` | `Float` | `min: Float = -Float.MAX_VALUE`, `max: Float = Float.MAX_VALUE` | Use for decimal input where single-precision is sufficient. |
| `doubleArgument` | `Double` | `min: Double = -Double.MAX_VALUE`, `max: Double = Double.MAX_VALUE` | Use for decimal input where you want the default floating-point type in Kotlin. |
| `doubleRangeArgument` | `DoubleRange` | none | Use when the command accepts a Minecraft-style decimal range. |
| `longArgument` | `Long` | `min: Long = Long.MIN_VALUE`, `max: Long = Long.MAX_VALUE` | Use for large whole numbers beyond `Int`. |
| `booleanArgument` | `Boolean` | none | Use for true/false flags. |
| `stringArgument` | `String` | none | Use for a single string token. |
| `textArgument` | `String` | none | Use for text input parsed as a text argument by CommandAPI. |
| `greedyStringArgument` | `String` | none | Use when the final argument should consume the rest of the input line. |
| `locationArgument` | `Location` | `locationType: LocationType = LocationType.PRECISE_POSITION`, `centerPosition: Boolean = true` | Use for full 3D coordinates. Adjust `locationType` and `centerPosition` when needed. |
| `location2DArgument` | `Location2D` | `locationType: LocationType = LocationType.PRECISE_POSITION`, `centerPosition: Boolean = true` | Use for 2D coordinates when Y is not part of the input. |
| `rotationArgument` | `Rotation` | none | Use for pitch/yaw style rotation input. |
| `axisArgument` | `EnumSet<Axis>` | none | Use when the command accepts one or more axes such as X/Y/Z. |
| `entitySelectorArgumentOneEntity` | `Entity` | none | Use for a selector that must resolve to exactly one entity. |
| `entitySelectorArgumentManyEntities` | `Collection<Entity>` | `allowEmpty: Boolean = true` | Use for a selector that may resolve to multiple entities. Set `allowEmpty = false` when the selector must match at least one entity. |
| `entitySelectorArgumentOnePlayer` | `Player` | none | Use for a selector that must resolve to exactly one player. |
| `entitySelectorArgumentManyPlayers` | `Collection<Player>` | `allowEmpty: Boolean = true` | Use for a selector that may resolve to multiple players. Set `allowEmpty = false` when the selector must match at least one player. |
| `entityTypeArgument` | `EntityType` | none | Use when the user names an entity type such as zombie or cow. |
| `scoreHolderArgumentSingle` | `String` | none | Use for a single scoreboard holder name. |
| `scoreHolderArgumentMultiple` | `Collection<String>` | none | Use for multiple scoreboard holder names. |
| `scoreboardSlotArgument` | `ScoreboardSlot` | none | Use when the command targets a scoreboard display slot. |
| `objectiveArgument` | `Objective` | none | Use for an existing scoreboard objective. |
| `objectiveCriteriaArgument` | `String` | none | Use when the command accepts an objective criteria id. |
| `teamArgument` | `Team` | none | Use for an existing scoreboard team. |
| `angleArgument` | `Float` | none | Use for angle input. |
| `advancementArgument` | `Advancement` | none | Use for a Bukkit advancement reference. |
| `biomeArgument` | `Biome` | none | Use for a Bukkit biome value. |
| `biomeNamespacedKeyArgument` | `NamespacedKey` | none | Use when you want the biome identifier instead of the enum-like biome value. |
| `blockStateArgument` | `BlockState` | none | Use for a parsed block state. |
| `commandArgument` | `CommandResult` | none | Use when the command takes another command as input and you need the parsed command result. |
| `enchantmentArgument` | `Enchantment` | none | Use for a Bukkit enchantment. |
| `itemStackArgument` | `ItemStack` | none | Use for a parsed item stack. |
| `lootTableArgument` | `LootTable` | none | Use for a loot table reference. |
| `mathOperationArgument` | `MathOperation` | none | Use when the command takes a scoreboard-style math operation. |
| `namespacedKeyArgument` | `NamespacedKey` | none | Use for generic namespaced resource keys. |
| `particleArgument` | `ParticleData<*>` | none | Use for a parsed particle input, including any attached particle data. |
| `potionEffectArgument` | `PotionEffectType` | none | Use for a Bukkit potion effect type. |
| `potionEffectNamespacedKeyArgument` | `NamespacedKey` | none | Use when you want the potion effect identifier instead of the Bukkit type object. |
| `soundArgument` | `Sound` | none | Use for a Bukkit sound constant. |
| `soundNamespacedKeyArgument` | `NamespacedKey` | none | Use when you want the sound identifier instead of the Bukkit enum. |
| `timeArgument` | `Int` | none | Use for Minecraft time input parsed as ticks. |
| `uuidArgument` | `UUID` | none | Use for UUID input. |
| `worldArgument` | `World` | none | Use for a Bukkit world reference. |
| `blockPredicateArgument` | `Predicate<Block>` | none | Use when the command takes a block predicate and you want to test blocks against it. |
| `itemStackPredicateArgument` | `Predicate<ItemStack>` | none | Use when the command takes an item predicate and you want to test item stacks against it. |
| `nbtCompoundArgument<NBTContainer>` | `NBTContainer` | none | Use for NBT compound input. The return type is the reified generic type you request at the call site. |
| `literalArgument` | `String` | `literal: String = nodeName` | Use for a fixed literal branch in the command tree. Override `literal` when the exposed token should differ from the node name. |
| `multiLiteralArgument` | `String` | `vararg literals: String` | Use for one branch that accepts one of several fixed literal strings. Pass all accepted literal values through `literals`. |
| `functionArgument` | `Array<FunctionWrapper>` | none | Use when the command accepts one or more Minecraft functions. |

### Executors

| Helper | Receives | How to use |
| --- | --- | --- |
| `anyExecutor` | `CommandSender`, `CommandArguments` | Use when any Bukkit sender type may run the command. |
| `playerExecutor` | `Player`, `CommandArguments` | Use when only players should run the command. |
| `entityExecutor` | `Entity`, `CommandArguments` | Use when any entity sender is acceptable. |
| `consoleExecutor` | `ConsoleCommandSender`, `CommandArguments` | Use for console-only command execution. |
| `commandBlockExecutor` | `BlockCommandSender`, `CommandArguments` | Use for command block execution. |
| `proxyExecutor` | `ProxiedCommandSender`, `CommandArguments` | Use when proxied command senders matter. |
| `nativeExecutor` | `NativeProxyCommandSender`, `CommandArguments` | Use for native proxied sender handling. |
| `remoteConsoleExecutor` | `RemoteConsoleCommandSender`, `CommandArguments` | Use for remote console senders. |

### Resulting Executors

These are the same as the executor helpers above, but return `Int` as the command result:

- `anyResultingExecutor`
- `playerResultingExecutor`
- `entityResultingExecutor`
- `consoleResultingExecutor`
- `commandBlockResultingExecutor`
- `proxyResultingExecutor`
- `nativeResultingExecutor`
- `remoteConsoleResultingExecutor`

### ExecutionInfo Helpers

Use these when you want `ExecutionInfo<...>` instead of direct `(sender, args)` parameters:

- `anyExecutionInfo`
- `playerExecutionInfo`
- `entityExecutionInfo`
- `consoleExecutionInfo`
- `commandBlockExecutionInfo`
- `proxyExecutionInfo`
- `nativeExecutionInfo`
- `remoteConsoleExecutionInfo`

The `*ResultingExecutionInfo` variants return `Int`:

- `anyResultingExecutionInfo`
- `playerResultingExecutionInfo`
- `entityResultingExecutionInfo`
- `consoleResultingExecutionInfo`
- `commandBlockResultingExecutionInfo`
- `proxyResultingExecutionInfo`
- `nativeResultingExecutionInfo`
- `remoteConsoleResultingExecutionInfo`

## Paper Additions

Paper includes all Bukkit builders and executors, plus these extra arguments and executor helpers.

### Additional Argument Builders

| Builder | Returns | Extra options | How to use |
| --- | --- | --- | --- |
| `chatColorArgument` | `NamedTextColor` | none | Use for Adventure text colors on Paper. |
| `chatComponentArgument` | `Component` | none | Use for Adventure rich-text components. |
| `chatArgument` | `SignedMessage` | none | Use when you need Paper's signed chat message object. |
| `playerProfileArgument` | `List<PlayerProfile>` | none | Use when the command resolves one or more Paper player profiles. |
| `asyncPlayerProfileArgument` | `CompletableFuture<List<PlayerProfile>>` | none | Use when player profile resolution is asynchronous. Await or compose the future. |
| `recipeArgument` | `Recipe` | none | Use for a Bukkit/Paper recipe reference. |

### Additional Executor Helpers

| Helper | Receives | How to use |
| --- | --- | --- |
| `feedbackForwardingExecutor` | `CommandSender`, `CommandArguments` | Use for Paper feedback-forwarding command execution. |
| `feedbackForwardingResultingExecutor` | `CommandSender`, `CommandArguments` and returns `Int` | Use when you need both feedback forwarding and a numeric command result. |
| `feedbackForwardingExecutionInfo` | `ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>>` | Use when you need execution metadata in feedback-forwarding mode. |
| `feedbackForwardingResultingExecutionInfo` | `ExecutionInfo<CommandSender, BukkitFeedbackForwardingCommandSender<CommandSender>>` and returns `Int` | Use the info-based feedback-forwarding variant with a numeric command result. |

## Spigot Additions

Spigot includes all Bukkit builders and executors, plus these extra arguments.

### Additional Argument Builders

| Builder | Returns | Extra options | How to use |
| --- | --- | --- | --- |
| `chatColorArgument` | `ChatColor` | none | Use for Spigot/Bungee chat colors. |
| `chatComponentArgument` | `Array<BaseComponent>` | none | Use for Bungee chat components on Spigot. |
| `chatArgument` | `Array<BaseComponent>` | none | Use when the parsed chat payload should stay in Bungee component form. |
| `playerProfileArgument` | `List<PlayerProfile>` | none | Use when the command resolves one or more Bukkit player profiles. |
| `asyncPlayerProfileArgument` | `CompletableFuture<List<PlayerProfile>>` | none | Use when player profile resolution is asynchronous. |
| `recipeArgument` | `Recipe` | none | Use for a Bukkit recipe reference. |

## Velocity Surface

Velocity has its own smaller argument and executor set.

### Argument Builders

| Builder | Returns | Extra options | How to use |
| --- | --- | --- | --- |
| `integerArgument` | `Int` | `min: Int = Int.MIN_VALUE`, `max: Int = Int.MAX_VALUE` | Use for whole-number command input. |
| `floatArgument` | `Float` | `min: Float = -Float.MAX_VALUE`, `max: Float = Float.MAX_VALUE` | Use for single-precision decimal input. |
| `doubleArgument` | `Double` | `min: Double = -Double.MAX_VALUE`, `max: Double = Double.MAX_VALUE` | Use for decimal input using Kotlin's default floating-point type. |
| `longArgument` | `Long` | `min: Long = Long.MIN_VALUE`, `max: Long = Long.MAX_VALUE` | Use for large whole numbers. |
| `booleanArgument` | `Boolean` | none | Use for true/false flags. |
| `stringArgument` | `String` | none | Use for a single string token. |
| `textArgument` | `String` | none | Use for text input parsed as a text argument by CommandAPI. |
| `greedyStringArgument` | `String` | none | Use when the final argument should consume the rest of the line. |
| `literalArgument` | `String` | `literal: String = nodeName` | Use for a fixed literal branch. The returned value is the matched literal string. |
| `multiLiteralArgument` | `String` | `vararg literals: String` | Use for one branch that accepts one of several fixed literal strings. |

### Executors

| Helper | Receives | How to use |
| --- | --- | --- |
| `anyExecutor` | `CommandSource`, `CommandArguments` | Use when any Velocity command source may run the command. |
| `playerExecutor` | `Player`, `CommandArguments` | Use when only players should run the command. |
| `consoleExecutor` | `ConsoleCommandSource`, `CommandArguments` | Use for console-only command execution. |

### Resulting Executors

These are the same as the executor helpers above, but return `Int`:

- `anyResultingExecutor`
- `playerResultingExecutor`
- `consoleResultingExecutor`

### ExecutionInfo Helpers

Use these when you want `ExecutionInfo<...>` instead of direct `(sender, args)` parameters:

- `anyExecutionInfo`
- `playerExecutionInfo`
- `consoleExecutionInfo`

The `*ResultingExecutionInfo` variants return `Int`:

- `anyResultingExecutionInfo`
- `playerResultingExecutionInfo`
- `consoleResultingExecutionInfo`

## Practical Guidance

- If the platform is unknown, infer it from dependencies and imports before suggesting builders.
- If the user asks for available arguments, answer from this reference instead of guessing from upstream CommandAPI docs.
- When showing examples, prefer the improved getter-lambda style rather than `args.get("...") as ...`.
