rootProject.name = "commandapi-kotlin-improved"

include(
    ":api:bukkit",
    ":api:paper",
    ":api:spigot",
    ":api:velocity",
    ":examples:original",
    ":examples:improved",
)

includeBuild("quality/detekt-rules") {
    name = "commandapi-detekt-rules"
}
