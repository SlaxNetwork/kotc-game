package io.github.slaxnetwork.events.crown

import io.github.slaxnetwork.player.KOTCPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

/**
 * [KOTCPlayer] has lost their crown.
 */
class KOTCPlayerCrownLostEvent(
    val kotcPlayer: KOTCPlayer
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