package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.game.microgame.death.RespawnableMicroGame
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager,
    private val waitingRoomManager: WaitingRoomManager
) : Listener {
    @EventHandler
    fun onPlayerJoin(ev: PlayerJoinEvent) {
        val uuid = ev.player.uniqueId

        val kotcPlayer = kotcPlayerRegistry.findByUUID(uuid)
        // reconnect.
        if(kotcPlayer != null) {
            kotcPlayer.connected = true
            return
        }

        kotcPlayerRegistry.add(uuid)

        if(!gameManager.hasGameStarted) {
            waitingRoomManager.teleport(ev.player)
        }
    }

    @EventHandler
    fun onPlayerReconnect(ev: KOTCPlayerReconnectEvent) {
        val bukkitPlayer = ev.kotcPlayer.bukkitPlayer
            ?: return

        // there is currently an active microgame running.
        if(gameManager.isRunningMicroGame) {
            gameManager.currentMicroGame?.let { microGame ->
                val gamePlayer = microGame.findGamePlayerByUUID(bukkitPlayer.uniqueId)
                    ?: return@let

                // TODO: 3/22/2023 Place them into spectator.
            }
            return
        }

        // waiting room
        waitingRoomManager.teleport(bukkitPlayer)
    }
}