package io.github.slaxnetwork.waitingroom

import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import io.github.slaxnetwork.mm
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

class WaitingRoomManager(
    private val playerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager,
    private val profileRegistry: ProfileRegistry
) {
    private val gameHasStarted get() = gameManager.hasGameStarted

    fun teleport() {
    }

    fun joinServer(player: Player) {
        val kotcPlayer = playerRegistry.players[player.uniqueId]

        if(gameHasStarted) {
            if(kotcPlayer == null) {
                player.kick(mm.deserialize("<red>Game in session."))
                return
            }

            kotcPlayer.connected = true

            Bukkit.getPluginManager().callEvent(KOTCPlayerReconnectEvent(
                kotcPlayer
            ))
            return
        }

        playerRegistry.add(player.uniqueId)
        teleport()
    }

    fun leaveServer(uuid: UUID) {
    }

    companion object {
        const val MIN_PLAYERS_TO_START = 3
        const val MAX_PLAYERS = 12
    }
}