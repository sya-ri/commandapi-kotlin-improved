# Migration Reference

## Goal

Move command code from the official CommandAPI Kotlin DSL to commandapi-kotlin-improved without changing behavior.

## Import Changes

Before:

```kotlin
import dev.jorel.commandapi.kotlindsl.anyExecutor
import dev.jorel.commandapi.kotlindsl.commandTree
import dev.jorel.commandapi.kotlindsl.integerArgument
import dev.jorel.commandapi.kotlindsl.literalArgument
```

After:

```kotlin
import dev.s7a.commandapi.anyExecutor
import dev.s7a.commandapi.commandTree
import dev.s7a.commandapi.integerArgument
import dev.s7a.commandapi.literalArgument
```

## Argument Access Changes

Before:

```kotlin
integerArgument("value") {
    anyExecutor { sender, args ->
        val value = args.get("value") as Int
        sender.sendMessage("You entered: $value")
    }
}
```

After:

```kotlin
integerArgument("value") { getValue ->
    anyExecutor { sender, args ->
        val value = getValue(args)
        sender.sendMessage("You entered: $value")
    }
}
```

## Migration Checklist

1. Keep the same command names and argument node names.
2. Replace official DSL imports with `dev.s7a.commandapi` imports.
3. Update each argument block to capture the provided getter lambda.
4. Replace every `args.get("...") as ...` cast with the typed getter.
5. Keep executor choice unchanged unless there is a clear type-safety improvement.
6. Do not rewrite a `commandTree` flow into `CommandAPICommand`.

## Review Heuristics

After migrating, quickly check for these leftovers:

- `as Int`, `as String`, or similar casts on command arguments
- `args.get("node")`
- imports from `dev.jorel.commandapi.kotlindsl`
- accidental node renames that would break argument lookup
