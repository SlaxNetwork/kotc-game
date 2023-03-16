package io.github.slaxnetwork.game.microgame.player

import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.player.KOTCPlayer
import org.bukkit.entity.Player

abstract class MicroGamePlayer(
    val kotcPlayer: KOTCPlayer
) {
    val bukkitPlayer: Player?
        get() = kotcPlayer.bukkitPlayer

    val connected: Boolean
        get() = kotcPlayer.connected

    var dead: Boolean = false

    var team: KOTCTeam = KOTCTeam.NONE
}