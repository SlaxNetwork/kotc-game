package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
import io.github.slaxnetwork.config.types.Config
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
    private val config by injectConfig<Config>()

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

        val (minimumPlayers) = config.game.start

        if(playerSize < minimumPlayers) {
            KOTCLogger.debug("game-start", "Cancelled game start due to lack of players.")
            gameManager.cancelGameCountdown()
        }
    }
}