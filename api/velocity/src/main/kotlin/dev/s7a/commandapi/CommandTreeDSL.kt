@file:JvmName("CommandTreeDSLVelocity")

package dev.s7a.commandapi

import dev.jorel.commandapi.CommandTree
import dev.jorel.commandapi.arguments.Argument
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.arguments.DoubleArgument
import dev.jorel.commandapi.arguments.FloatArgument
import dev.jorel.commandapi.arguments.GreedyStringArgument
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.LiteralArgument
import dev.jorel.commandapi.arguments.LongArgument
import dev.jorel.commandapi.arguments.MultiLiteralArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.arguments.TextArgument
import dev.jorel.commandapi.executors.CommandArguments

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
