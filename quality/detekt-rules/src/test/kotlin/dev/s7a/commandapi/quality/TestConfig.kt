package dev.s7a.commandapi.quality

import dev.detekt.api.Config

@Suppress("UNCHECKED_CAST")
internal class TestConfig(
    private val values: Map<String, Any?>,
) : Config {
    override val parent: Config = Config.empty

    override fun subConfig(key: String): Config = TestConfig(values[key] as? Map<String, Any?> ?: emptyMap())

    override fun subConfigKeys(): Set<String> = values.filterValues { it is Map<*, *> }.keys

    override fun <T : Any> valueOrNull(key: String): T? = values[key] as? T
}
