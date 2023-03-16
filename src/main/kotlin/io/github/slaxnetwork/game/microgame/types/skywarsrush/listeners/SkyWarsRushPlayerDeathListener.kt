package io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners

import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMicroGame
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class SkyWarsRushPlayerDeathListener(
    private val skyWars: SkyWarsRushMicroGame
) : Listener {
    private val playerRegistry get() = skyWars.kotcPlayerRegistry

    @EventHandler
    fun onPlayerDeath(ev: PlayerDeathEvent) {
        val kotcPlayer = playerRegistry.players[ev.player.uniqueId]
            ?: return

        Bukkit.broadcastMessage("${kotcPlayer.bukkitPlayer?.name} has died while in game.")

        skyWars.microGamePlayerRegistry.findByUUID(ev.player.uniqueId)?.dead = true
    }
}