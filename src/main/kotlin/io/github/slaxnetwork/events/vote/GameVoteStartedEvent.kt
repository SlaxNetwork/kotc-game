package io.github.slaxnetwork.events.vote

import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class GameVoteStartedEvent: Event() {
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