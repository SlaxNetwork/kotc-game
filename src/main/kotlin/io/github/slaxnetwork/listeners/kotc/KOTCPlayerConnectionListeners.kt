package io.github.slaxnetwork.listeners.kotc

import io.github.slaxnetwork.events.KOTCPlayerDisconnectEvent
import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class KOTCPlayerConnectionListeners : Listener {
    @EventHandler
    fun onKOTCPlayerReconnect(ev: KOTCPlayerReconnectEvent) {

    }

    @EventHandler
    fun onKOTCPlayerDisconnect(ev: KOTCPlayerDisconnectEvent) {
        
    }
}