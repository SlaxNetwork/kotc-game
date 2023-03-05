package io.github.slaxnetwork.events

import io.github.slaxnetwork.player.KOTCPlayer
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class KOTCPlayerDisconnectEvent(
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