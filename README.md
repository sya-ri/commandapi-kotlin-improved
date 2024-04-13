# commandapi-kotlin-improved

[![Compatible with CommandAPI v9.3.0](https://img.shields.io/badge/Compatible%20with-CommandAPI%20v9.3.0-brightgreen)](https://commandapi.jorel.dev/9.3.0/)

- A better alternative library to [official Kotlin DSL](https://commandapi.jorel.dev/9.3.0/kotlindsl.html)
  - shorter, more type-safe code
- Code compatibility. Change the import, and it will work (but doesn't support CommandAPICommand)
  - You can also cast by specifying the node name in the same way as the official one.
  - Recommend to obtain it via a getter.

## Implementation

- Replace `dev.jorel:commandapi-bukkit-kotlin` with this library.

```kotlin
dependencies {
    implementation("dev.s7a:commandapi-bukkit-kotlin-improved:1.0.0")
}
```

## Examples

### Official `dev.jorel:commandapi-bukkit-kotlin`

> [!CAUTION]
> Requires casts and enters the correct node name.

```kotlin
fun registerCommand() {
    commandTree("example") {
        literalArgument("integer") {
            integerArgument("value") {
                anyExecutor { sender, args ->
                    val value = args.get("value") as Int // Dangerous!!!
                    sender.sendMessage("You entered: $value")
                }
            }
        }
    }
}
```

### Improved `dev.s7a:commandapi-bukkit-kotlin-improved`

> [!IMPORTANT]
> Type-safe values can be obtained just by passing CommandArguments.

```kotlin
fun registerCommand() {
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
