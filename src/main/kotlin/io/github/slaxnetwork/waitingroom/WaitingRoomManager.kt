package io.github.slaxnetwork.waitingroom

import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.config.injectConfig
import io.github.slaxnetwork.config.types.WaitingRoomConfig
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.*

class WaitingRoomManager(
    private val playerRegistry: KOTCPlayerRegistry,
    private val profileRegistry: ProfileRegistry
) {
    private val waitingRoomConfig by injectConfig<WaitingRoomConfig>()

    private val spawnPoint: Location
        get() = waitingRoomConfig.spawnPoint.toBukkitLocation(waitingRoomConfig.worldName)
            ?: throw NullPointerException()

    fun teleport(player: Player) {
        player.teleport(spawnPoint)
    }

    fun joinServer(player: Player) {
        val kotcPlayer = playerRegistry.findByUUID(player.uniqueId)

//        if(gameHasStarted) {
//            if(kotcPlayer == null) {
//                player.kick(mm.deserialize("<red>Game in session."))
//                return
//            }
//
//            kotcPlayer.connected = true
//
//            Bukkit.getPluginManager().callEvent(KOTCPlayerReconnectEvent(
//                kotcPlayer
//            ))
//            return
//        }

        playerRegistry.add(player.uniqueId)
        teleport(player)
    }

    fun leaveServer(uuid: UUID) {
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