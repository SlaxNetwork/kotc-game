package io.github.slaxnetwork.listeners.skywarsrush

import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMicroGame
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class SkyWarsRushPlayerDeathListener(
    private val skyWars: SkyWarsRushMicroGame
) : Listener {
    private val playerRegistry get() = skyWars.playerRegistry

    @EventHandler
    fun onPlayerDeath(ev: PlayerDeathEvent) {
        val kotcPlayer = playerRegistry.players[ev.player.uniqueId]
            ?: return

        Bukkit.broadcastMessage("${kotcPlayer.bukkitPlayer?.name} has died while in game.")

        kotcPlayer.dead = true
    }
}