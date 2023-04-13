package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
import io.github.slaxnetwork.config.types.Config
import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager,
    private val waitingRoomManager: WaitingRoomManager,
    private val scoreboardManager: ScoreboardManager
) : Listener {
    private val config by injectConfig<Config>()

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

        checkIfGameIsStartable()
    }

    private fun checkIfGameIsStartable() {
        KOTCLogger.debug("game-start", "has game started, ${gameManager.hasGameStarted}")
        if(gameManager.hasGameStarted) {
            return
        }

        val playerSize = kotcPlayerRegistry.players.size
        KOTCLogger.debug("game-start", "player size is $playerSize.")

        val (minimumPlayers, maximumPlayers) = config.game.start

        if(playerSize >= maximumPlayers) {
            KOTCLogger.debug("game-start", "Using fast start.")
            // Fast start.
            gameManager.startGameCountdown(slow = false)
        } else if(playerSize >= minimumPlayers) {
            KOTCLogger.debug("game-start", "Using slow start.")
            // Slow start.
            gameManager.startGameCountdown(slow = true)
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