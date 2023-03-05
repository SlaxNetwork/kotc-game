package io.github.slaxnetwork.listeners.kotc

import io.github.slaxnetwork.events.crown.KOTCCrownHolderDeathEvent
import io.github.slaxnetwork.events.crown.KOTCPlayerCrownLostEvent
import io.github.slaxnetwork.events.crown.KOTCPlayerCrownObtainedEvent
import io.github.slaxnetwork.mm
import org.bukkit.Bukkit
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.inventory.ItemStack

class KOTCPlayerCrownListeners : Listener {
    @EventHandler
    fun onCrownObtained(ev: KOTCPlayerCrownObtainedEvent) {
        val bukkitPlayer = ev.kotcPlayer.bukkitPlayer
            ?: return

        bukkitPlayer.inventory.helmet = ItemStack(Material.GOLD_BLOCK)
        bukkitPlayer.sendMessage(mm.deserialize("<gold>You have obtained the crown."))
    }

    @EventHandler
    fun onCrownLost(ev: KOTCPlayerCrownLostEvent) {
        val bukkitPlayer = ev.kotcPlayer.bukkitPlayer
            ?: return

        bukkitPlayer.inventory.helmet = null
        bukkitPlayer.sendMessage(mm.deserialize("<red>You have lost the crown."))
    }

    @EventHandler
    fun onCrownHolderDeath(ev: KOTCCrownHolderDeathEvent) {
        ev.victim.crownHolder = false

        if(ev.killer != null) {
            ev.killer.crownHolder = true
        } else {
            Bukkit.broadcast(mm.deserialize("<red>Nobody has taken the crown."))
        }
    }
}