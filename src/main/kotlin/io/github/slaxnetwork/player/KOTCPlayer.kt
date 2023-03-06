package io.github.slaxnetwork.player

import io.github.slaxnetwork.events.KOTCPlayerDisconnectEvent
import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import io.github.slaxnetwork.events.crown.KOTCPlayerCrownLostEvent
import io.github.slaxnetwork.events.crown.KOTCPlayerCrownObtainedEvent
import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

data class KOTCPlayer(
    val uuid: UUID
) {
    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    var rubies = 0
        private set

    var crownHolder = false
        set(value) {
            Bukkit.getPluginManager().callEvent(
                if(value) KOTCPlayerCrownObtainedEvent(this)
                else KOTCPlayerCrownLostEvent(this)
            )
            field = value
        }

    var connected = true
        set(value) {
            Bukkit.getPluginManager().callEvent(
                if(value) KOTCPlayerReconnectEvent(this)
                else KOTCPlayerDisconnectEvent(this)
            )
            field = value
        }

    var dead = false

    fun addRubies(amount: Int) {
        rubies += amount
    }
}