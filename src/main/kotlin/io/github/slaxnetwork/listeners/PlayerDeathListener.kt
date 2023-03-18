package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.config.injectConfig
import io.github.slaxnetwork.config.types.SoundsConfig
import io.github.slaxnetwork.events.crown.KOTCCrownHolderDeathEvent
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(
    private val kotcPlayerRegistry: KOTCPlayerRegistry
) : Listener {
    private val soundsConfig by injectConfig<SoundsConfig>()

    @EventHandler
    fun onPlayerDeath(ev: PlayerDeathEvent) {
        val victim = ev.player
        val killer = ev.player.killer

        val kotcPlayerVictim = kotcPlayerRegistry.findByUUID(victim.uniqueId)
            ?: return

        if(!kotcPlayerVictim.crownHolder) {
            return
        }

        if(killer != null) {
            val kotcPlayerKiller = kotcPlayerRegistry.findByUUID(killer.uniqueId)
                ?: return

            Bukkit.getPluginManager().callEvent(KOTCCrownHolderDeathEvent(
                victim = kotcPlayerVictim,
                killer = kotcPlayerKiller
            ))
            return
        }

        Bukkit.getPluginManager().callEvent(KOTCCrownHolderDeathEvent(
            victim = kotcPlayerVictim,
            killer = null
        ))
    }
}