package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.game.microgame.death.RespawnableMicroGame
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.scoreboard.TestScoreboard
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

            kotcPlayerRegistry.findByUUID(ev.player.uniqueId)?.let {
                scoreboardManager.setBoard(ev.player, TestScoreboard(it.profile))
                scoreboardManager.updateLine(ev.player, 1)
            }
        }
    }

    @EventHandler
    fun onCheckGameStartable(ev: PlayerJoinEvent) {
        if(gameManager.hasGameStarted) {
            return
        }

        val playerSize = kotcPlayerRegistry.players.size

        if(playerSize >= WaitingRoomManager.MAX_PLAYERS) {
            // start
        }

        if(playerSize >= WaitingRoomManager.MIN_PLAYERS_TO_START) {
            // start countdown
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