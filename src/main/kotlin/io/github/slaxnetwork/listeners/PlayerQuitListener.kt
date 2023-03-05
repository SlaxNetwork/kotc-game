package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.GameManager
import io.github.slaxnetwork.events.KOTCPlayerDisconnectEvent
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class PlayerQuitListener(
    private val playerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager
) : Listener {
    @EventHandler
    fun onPlayerQuit(ev: PlayerQuitEvent) {
        val uuid = ev.player.uniqueId

        val kotcPlayer = playerRegistry.players[uuid]
            ?: return

        if(!gameManager.hasStarted) {
            playerRegistry.remove(uuid)

            return
        }

        kotcPlayer.connected = false

        Bukkit.getPluginManager().callEvent(KOTCPlayerDisconnectEvent(
            kotcPlayer
        ))

//        kotcPlayer.connected = false
//        kotcPlayer.dead = true
    }
}