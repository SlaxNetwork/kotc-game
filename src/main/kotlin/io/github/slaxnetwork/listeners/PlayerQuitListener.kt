package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager
) : Listener {
    @EventHandler
    fun onPlayerQuit(ev: PlayerQuitEvent) {
        val uuid = ev.player.uniqueId

        val kotcPlayer = kotcPlayerRegistry.findByUUID(uuid)
            ?: return

        if(!gameManager.hasGameStarted) {
            kotcPlayerRegistry.remove(uuid)
            return
        }

        kotcPlayer.connected = false
    }

    @EventHandler
    fun checkGameStateInvalidate(ev: PlayerQuitEvent) {
        if(gameManager.hasGameStarted) {
            return
        }

        val playerSize = kotcPlayerRegistry.players.size

        if(playerSize < WaitingRoomManager.MIN_PLAYERS_TO_START) {
            KOTCLogger.debug("game-start", "Cancelled game start due to lack of players.")
            gameManager.cancelGameCountdown()
        }
    }
}