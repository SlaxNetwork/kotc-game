package io.github.slaxnetwork.waitingroom

import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
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
}