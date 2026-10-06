# CommandAPI detekt rules

The skill bundles `assets/commandapi-detekt.jar`, independently of Minecraft architecture checks. It supports detekt v2 only; the tested version and rule artifact version are in `SKILL.md` metadata. The library's API artifacts do not include this JAR.

## Installation

Install this skill from a fixed tag or commit, then add its project-local JAR:

```kotlin
dependencies {
    detektPlugins(files(rootProject.file(
        ".agents/skills/commandapi-kotlin-improved/assets/commandapi-detekt.jar",
    )))
}
```

```yaml
config:
  validation: true
  excludes: ['commandApi>.*']

commandApi:
  active: true
  NoMixedLiteralAndArgumentSiblings:
    active: true
    includes: ["**/src/main/**"]
    # Optional: the project's wrapper, not an improved-library API.
    commandBase: app.presentation.command.Command
```

Omit `commandBase` for a direct DSL-only project. Keep other rule-set validation exclusions already required by that project. Enable typed main/test analysis with real compilation classpaths and connect it to `check`; an untyped task cannot prove this rule ran. Include related parent/child declarations in one analysis invocation for cross-file checks.

## Behavior and limits

`NoMixedLiteralAndArgumentSiblings` resolves `dev.s7a.commandapi.commandTree` and argument receiver builders. At each completion parent it rejects operation literals beside dynamic argument nodes. `--` literals are exempt. Nested nodes are checked separately; executor callbacks are not scanned as command declarations.

Source receiver helper bodies are followed. The rule does not execute arbitrary Kotlin, evaluate dynamic literal expressions or inspect unavailable binary helper bodies. Unknown literal names cannot establish the `--` exemption. Review generated or dynamically assembled command trees separately.

With `commandBase`, a direct `Command.Root`/`Command.Child` subclass is collected and the file-process listener joins child literals to their declared parent type. The supported project wrapper uses the root name as its first constructor argument, and the child parent class/name as its first two arguments. Other wrapper shapes require a rule enhancement, not a misleading FQCN. Rule aliases and standard detekt suppressions are respected.

## Maintaining the bundled artifact

`quality/detekt-rules` is an independent included Gradle build. Its compiler and detekt versions are isolated from the library's existing published API build; updating these checks does not change the library version or ABI.

Use mise and the repository Wrapper:

```text
Windows:
mise exec -- ./gradlew.bat --no-daemon -p quality/detekt-rules check assemble
mise run commandapi:update-jar

Linux/macOS:
mise exec -- ./gradlew --no-daemon -p quality/detekt-rules check assemble
mise run commandapi:update-jar
```

The update task runs rule checks before copying the JAR. It does not require the old asset to match. Root `check` runs rule tests, skill validation and `verifyBundledJar`, which compares the rebuilt reproducible archive with the committed asset. Commit source, tests, descriptors, metadata and the updated asset together. The JAR contains only custom rules; detekt is `compileOnly` and no Paper implementation is bundled.
