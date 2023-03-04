package io.github.slaxnetwork.player

import org.bukkit.Bukkit
import org.bukkit.entity.Player
import java.util.UUID

data class KOTCPlayer(
    val uuid: UUID
) {
    val bukkitPlayer: Player?
        get() = Bukkit.getPlayer(uuid)

    var crownHolder = false

    var connected = true
    var dead = false
}