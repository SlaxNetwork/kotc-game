package io.github.slaxnetwork.waitingroom

import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection
import org.bukkit.entity.Player
import java.util.*

class WaitingRoomManager(
    private val waitingRoomSection: ConfigurationSection,
    private val playerRegistry: KOTCPlayerRegistry,
    private val profileRegistry: ProfileRegistry
) {
    private val spawnPoint: Location = waitingRoomSection.getConfigurationSection("spawn_point")
        ?.toBukkitLocation(waitingRoomSection.getString("world") ?: "world")
        ?: throw IllegalArgumentException("waiting room doesn't have a spawn point set.")

    fun teleport() {
    }

    fun joinServer(player: Player) {
        val kotcPlayer = playerRegistry.players[player.uniqueId]

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
        teleport()
    }

    fun leaveServer(uuid: UUID) {
    }

    fun setWorldBorder() {
        val borderSection = waitingRoomSection.getConfigurationSection("border")
            ?: throw NullPointerException("section border for waiting_room isn't set.")

        if(!borderSection.isDouble("radius")) {
            throw IllegalArgumentException("border radius for waiting_room isn't set.")
        }
        val radius = borderSection.getDouble("radius")

        val worldBorder = spawnPoint.world.worldBorder

        worldBorder.center = spawnPoint
        worldBorder.size = radius
        worldBorder.damageAmount = 0.0
        worldBorder.damageBuffer = 1.0
    }

    companion object {
        const val MIN_PLAYERS_TO_START = 3
        const val MAX_PLAYERS = 12
    }
}