# Project-owned command classes

The published improved DSL provides `commandTree`, typed argument builders and executors. It does **not** provide `Command.Root` or `Command.Child`. Existing commands do not need a wrapper. Use this pattern when the application already separates root and child declarations into injectable classes, or when that structure helps a requested command hierarchy.

## Ownership and shape

Keep the application's abstract `Command` wrapper with command entrypoints, usually `presentation.command`. A root owns its name and `CommandTree.() -> Unit` block. A child owns its parent class, literal name and `Argument<*>.() -> Unit` block. Parent type and parent class reference must agree. Nested `Root`/`Child` types belong to this wrapper; concrete command classes remain in separate files.

This example uses the project-owned shape found in [SabMinato's Command wrapper](https://github.com/sabs-cool/SabMinato/blob/master/paper-common/src/main/kotlin/online/sabap/minato/common/presentation/command/Command.kt):

```kotlin
@Single(binds = [Command.Root::class])
class ExampleCommand : Command.Root<ExampleCommand>(
    "example",
    {
        permission = CommandPermission.fromString("example.admin")
    },
)

@Single(binds = [Command.Child::class])
class CreateCommand(
    createService: CreateService,
) : Command.Child<ExampleCommand, CreateCommand>(
    ExampleCommand::class,
    "create",
    {
        stringArgument("name") { getName ->
            anyExecutor { sender, arguments ->
                createService.create(sender, getName(arguments))
            }
        }
    },
)
```

`@Single` and `binds` belong to Koin, and `CreateService` is an illustrative application contract. Resolve and execute the service from the injected constructor dependency. Keep input conversion and response handling in the command; delegate business rules to the service.

## Parent attachment and registration

The wrapper is responsible for attaching children before invoking the parent's block. A root uses `commandTree(name)`. A child uses `parent.literalArgument(name)`; descendants attach inside that literal's receiver. Support both `CommandTree` and `Argument<*>` parents. A child is not registered as a separate top-level command.

The application's bootstrap can group DI-bound children by their explicit `parentType` and register DI-bound roots once:

```kotlin
val childrenByParent = koin.getAll<Command.Child<*, *>>()
    .groupBy(Command.Child<*, *>::parentType)

koin.getAll<Command.Root<*>>().forEach { root ->
    root.register(childrenByParent)
}
```

This is a bootstrap pattern, not a function supplied by the improved library. Construct dependencies with the project's DI integration and keep container resolution at bootstrap. Initialize CommandAPI before registration and connect `onEnable`/`onDisable` to the owning plugin lifecycle. Preserve the project's existing load/enable registration timing and namespace/replacement behavior; do not add a second registration path.

Validate unique command names, resolvable parent classes and absence of cycles when introducing or modifying the wrapper. Do not add unrelated registration frameworks or change existing command names solely to adopt this example.

## Completion contract

At one parent, use literal operation siblings or dynamic argument siblings. Put the operation before its arguments: `/example create <name>` and `/example info <name>`. Avoid sibling `create` and `<name>` under `/example`: completion and parsing then mix operations with values. Literal names beginning with `--` are the configured rule's exception for flags. Executors do not add a completion node.

A child class contributes its literal to its declared parent just as an inline `literalArgument` does. A parent with both a dynamic argument block and a child operation violates the same contract, even when those declarations are in different files. Use the dedicated [detekt rule](detekt.md) with the application's wrapper FQCN; do not write a SabMinato-specific lint rule.
