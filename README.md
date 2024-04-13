# commandapi-kotlin-improved

[![Compatible with CommandAPI v9.3.0](https://img.shields.io/badge/Compatible%20with-CommandAPI%20v9.3.0-brightgreen)](https://commandapi.jorel.dev/9.3.0/)

Improve [CommandAPI](https://github.com/JorelAli/CommandAPI) Kotlin DSL.

- A better alternative library to [official Kotlin DSL](https://commandapi.jorel.dev/9.3.0/kotlindsl.html)
  - shorter, more type-safe code
- Code compatibility. Change the import, and it will work (but doesn't support CommandAPICommand)
  - You can also cast by specifying the node name in the same way as the official one.
  - Recommend to obtain it via a getter.

## What's CommandAPI?

<a href="https://github.com/JorelAli/CommandAPI">
<div align="center">

> <img src="https://github.com/JorelAli/CommandAPI/raw/master/images/cmdapi.svg" alt="CommandAPI logo" width="600">
>
> A Bukkit/Spigot API to use the command UI introduced in Minecraft 1.13

</div>
</a>

A great library for creating Minecraft commands. It's a library written in Java and also features a Kotlin DSL. But it's not type safe.

## Installation

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
                    val value = getValue(args) // Returns Int
                    sender.sendMessage("You entered: $value")
                }
            }
        }
    }
}
```

## License

```
MIT License

Copyright (c) 2024 sya-ri

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
copies of the Software, and to permit persons to whom the Software is
furnished to do so, subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
SOFTWARE.
```
