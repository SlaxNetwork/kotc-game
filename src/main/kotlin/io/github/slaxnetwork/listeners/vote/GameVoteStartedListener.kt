package io.github.slaxnetwork.listeners.vote

import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.events.vote.GameVoteStartedEvent
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class GameVoteStartedListener(private val kotcPlayerRegistry: KOTCPlayerRegistry): Listener {
    @EventHandler
    fun onGameVoteStarted(ev: GameVoteStartedEvent) {
        kotcPlayerRegistry.broadcastLocalizedMessage("<text>", "game.vote.start")
    }
}