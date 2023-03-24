package io.github.slaxnetwork.waitingroom

import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.config.injectConfig
import io.github.slaxnetwork.config.types.WaitingRoomConfig
import org.bukkit.Location
import org.bukkit.entity.Player

class WaitingRoomManager(
    private val scoreboardManager: ScoreboardManager
) {
    private val waitingRoomConfig by injectConfig<WaitingRoomConfig>()

    private val spawnPoint: Location
        get() = waitingRoomConfig.spawnPoint.toBukkitLocation(waitingRoomConfig.worldName)
            ?: throw NullPointerException()

    fun teleport(player: Player) {
        player.teleport(spawnPoint)
    }

    fun preventNonAuthorizedConnections() {

    }

    fun startGameVote() {
    }

    fun setWorldBorder() {
        val (radius, damage, damageBuffer) = waitingRoomConfig.border

        val border = spawnPoint.world.worldBorder

        border.center = spawnPoint
        border.size = radius
        border.damageAmount = damage
        border.damageBuffer = damageBuffer
    }

    companion object {
        const val MIN_PLAYERS_TO_START = 3
        const val MAX_PLAYERS = 12
    }
}