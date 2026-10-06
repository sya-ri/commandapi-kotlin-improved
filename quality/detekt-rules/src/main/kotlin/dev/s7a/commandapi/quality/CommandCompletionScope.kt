package dev.s7a.commandapi.quality

import org.jetbrains.kotlin.psi.KtElement

/**
 * One statically known command node, before separately declared children are attached.
 */
internal data class CommandCompletionScope(
    val element: KtElement,
    val commandClass: String? = null,
    val parentClass: String? = null,
    val name: String? = null,
    val literals: MutableList<String?> = mutableListOf(),
    var hasArgument: Boolean = false,
)
