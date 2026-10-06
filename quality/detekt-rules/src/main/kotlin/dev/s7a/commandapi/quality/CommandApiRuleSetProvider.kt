package dev.s7a.commandapi.quality

import dev.detekt.api.RuleSet
import dev.detekt.api.RuleSetId
import dev.detekt.api.RuleSetProvider

/**
 * Registers the independently installable CommandAPI completion rules with detekt v2.
 */
class CommandApiRuleSetProvider : RuleSetProvider {
    override val ruleSetId = RuleSetId("commandApi")

    /**
     * Creates completion rules with consumer-owned command wrapper configuration.
     */
    override fun instance(): RuleSet = RuleSet(ruleSetId, listOf(::NoMixedLiteralAndArgumentSiblings))
}
