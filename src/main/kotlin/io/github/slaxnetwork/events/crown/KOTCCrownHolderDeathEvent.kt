package io.github.slaxnetwork.events.crown

import io.github.slaxnetwork.player.KOTCPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * [KOTCPlayer] that was formerly holding the crown has died.
 */
class KOTCCrownHolderDeathEvent(
    val victim: KOTCPlayer,
    val killer: KOTCPlayer?
) : Event() {
    companion object {
        private val handlers = HandlerList()

        @JvmStatic
        fun getHandlerList(): HandlerList {
            return handlers
        }
    }

    override fun getHandlers(): HandlerList {
        return Companion.handlers
    }
}