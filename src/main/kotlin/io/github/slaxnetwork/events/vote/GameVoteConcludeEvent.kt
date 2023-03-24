package io.github.slaxnetwork.events.vote

import io.github.slaxnetwork.game.microgame.MicroGameType
import org.bukkit.event.Event
import org.bukkit.event.HandlerList

class GameVoteConcludeEvent(
    val winner: MicroGameType,
    val voters: Int
): Event() {
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