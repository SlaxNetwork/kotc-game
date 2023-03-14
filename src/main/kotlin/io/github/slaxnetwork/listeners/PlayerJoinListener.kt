package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

class PlayerJoinListener(
    private val playerRegistry: KOTCPlayerRegistry,
    private val waitingRoomManager: WaitingRoomManager
) : Listener {
    @EventHandler
    fun onPlayerJoin(ev: PlayerJoinEvent) {
        val uuid = ev.player.uniqueId

        val kotcPlayer = playerRegistry.players[uuid]
        // reconnect.
        if(kotcPlayer != null) {
            kotcPlayer.connected = true
            return
        }

        playerRegistry.add(uuid)
    }
}