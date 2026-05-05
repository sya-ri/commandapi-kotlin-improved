---
name: commandapi-kotlin-improved
description: Use when working on Minecraft plugin commands built with commandapi-kotlin-improved, including adding new commands, migrating from the official CommandAPI Kotlin DSL, selecting the correct platform artifact, and replacing unsafe args.get(...) casts with type-safe getter lambdas.
license: MIT
---

# CommandAPI Kotlin Improved

Use this skill for Kotlin command code that should target `dev.s7a:commandapi-*-kotlin-improved`.

## What This Library Changes

The main improvement over the official CommandAPI Kotlin DSL is typed argument access.

Prefer this pattern:

```kotlin
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
```

Avoid this pattern when the improved DSL is available:

```kotlin
val value = args.get("value") as Int
```

## Workflow

1. Detect the platform from the existing project or requested target.
   Supported platforms are `bukkit`, `spigot`, `paper`, and `velocity`.
2. Make sure imports come from `dev.s7a.commandapi`, not `dev.jorel.commandapi.kotlindsl`.
3. Keep the existing command shape and node names unless the user asks to redesign them.
4. For each argument node, use the improved argument builder overload that provides a getter lambda.
5. Inside executors, read typed values via that getter lambda instead of `args.get(...) as ...`.
6. Choose the narrowest executor that matches the use case, such as `playerExecutor` instead of `anyExecutor`, when sender type matters.

## Dependency Guidance

Match the platform-specific artifacts:

- CommandAPI core: `dev.jorel:commandapi-{platform}-core`
- Improved DSL: `dev.s7a:commandapi-{platform}-kotlin-improved`

If the project already depends on CommandAPI, preserve its version line unless the user asked for an upgrade. This library targets CommandAPI `11.1.0` in this repository.

## Migration Rules

- Replace `dev.jorel.commandapi.kotlindsl.*` imports with `dev.s7a.commandapi.*`.
- Keep `commandTree(...)`-based structure where possible.
- Replace unsafe `args.get("node") as T` access with getter lambdas from the argument builder.
- Preserve node names so existing command lookups keep working.
- Do not migrate to `CommandAPICommand`; this library is intended around the `commandTree` DSL.

## When To Read References

- For install coordinates, platform mapping, and common command patterns, read [references/usage.md](references/usage.md).
- For side-by-side migration guidance from the official Kotlin DSL, read [references/migration.md](references/migration.md).
- For available argument builders and executor helpers by platform, read [references/api-surface.md](references/api-surface.md).
