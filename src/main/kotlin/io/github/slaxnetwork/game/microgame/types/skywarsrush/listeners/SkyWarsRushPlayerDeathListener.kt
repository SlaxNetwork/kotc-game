package io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners

import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMicroGame
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class SkyWarsRushPlayerDeathListener(
    private val skyWars: SkyWarsRushMicroGame
) : Listener {
    private val kotcPlayers get() = skyWars.kotcPlayers

    @EventHandler
    fun onPlayerDeath(ev: PlayerDeathEvent) {
        val gamePlayer = skyWars.microGamePlayerRegistry.findByUUID(ev.player.uniqueId)
            ?: return

        gamePlayer.dead = true
    }
}