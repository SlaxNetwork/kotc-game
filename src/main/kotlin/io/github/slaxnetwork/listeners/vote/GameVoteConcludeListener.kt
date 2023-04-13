package io.github.slaxnetwork.listeners.vote

import io.github.slaxnetwork.events.vote.GameVoteConcludeEvent
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameVoteConcludeListener(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager
) : Listener {
    @EventHandler
    fun onGameVoteConclude(ev: GameVoteConcludeEvent) {
        gameManager.startMicroGame(ev.winner)
    }
}