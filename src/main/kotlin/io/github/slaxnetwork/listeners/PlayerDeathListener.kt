package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.events.crown.KOTCCrownHolderDeathEvent
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(
    private val playerRegistry: KOTCPlayerRegistry
) : Listener {
    @EventHandler
    fun onPlayerDeath(ev: PlayerDeathEvent) {
        val victim = ev.player
        val killer = ev.player.killer

        val kotcPlayerVictim = playerRegistry.players[victim.uniqueId]
            ?: return

        if(!kotcPlayerVictim.crownHolder) {
            return
        }

        if(killer != null) {
            val kotcPlayerKiller = playerRegistry.players[killer.uniqueId]
                ?: return

            Bukkit.getPluginManager().callEvent(KOTCCrownHolderDeathEvent(
                victim = kotcPlayerVictim,
                killer = kotcPlayerKiller
            ))
        }

        Bukkit.getPluginManager().callEvent(KOTCCrownHolderDeathEvent(
            victim = kotcPlayerVictim,
            killer = null
        ))
    }
}