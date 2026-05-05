# Usage Reference

## Supported Platforms

- `bukkit`
- `spigot`
- `paper`
- `velocity`

## Dependency Pattern

```kotlin
repositories {
    mavenCentral()
}

dependencies {
    implementation("dev.jorel:commandapi-{platform}-core:11.1.0")
    implementation("dev.s7a:commandapi-{platform}-kotlin-improved:<version>")
}
```

Replace `{platform}` with one of the supported platform names. In repositories already using the improved DSL, prefer the version already present in the build files.

## Core Imports

Typical imports look like this:

```kotlin
import dev.s7a.commandapi.anyExecutor
import dev.s7a.commandapi.commandTree
import dev.s7a.commandapi.integerArgument
import dev.s7a.commandapi.literalArgument
```

If a project still imports `dev.jorel.commandapi.kotlindsl.*`, treat that as a migration target.

## Preferred Command Pattern

```kotlin
private fun registerCommand() {
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
}
```

The important part is that `integerArgument("value") { getValue -> ... }` gives you a typed accessor for that node.

## Executor Selection

Use the narrowest executor that fits:

- `anyExecutor`
- `playerExecutor`
- `entityExecutor`
- `consoleExecutor`
- `commandBlockExecutor`
- `proxyExecutor`
- `nativeExecutor`
- `remoteConsoleExecutor`

If the command returns an integer result, use the corresponding `*ResultingExecutor` variant.

## Optional Arguments

Optional builders return nullable typed accessors:

```kotlin
integerOptionalArgument("amount") { getAmount ->
    anyExecutor { sender, args ->
        val amount = getAmount(args)
        sender.sendMessage("amount=$amount")
    }
}
```

Handle `null` explicitly instead of forcing casts.
