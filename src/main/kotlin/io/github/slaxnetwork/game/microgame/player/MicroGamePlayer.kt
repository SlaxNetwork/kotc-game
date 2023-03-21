package io.github.slaxnetwork.game.microgame.player

import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.player.KOTCPlayer
import org.bukkit.entity.Player
import java.util.UUID

/**
 * An abstract class to hold game-specific data that doesn't fit on the [KOTCPlayer].
 * @param kotcPlayer Reference to the [KOTCPlayer]
 */
open class MicroGamePlayer(
    val kotcPlayer: KOTCPlayer
) {
    val uuid: UUID
        get() = kotcPlayer.uuid

    val bukkitPlayer: Player?
        get() = kotcPlayer.bukkitPlayer

    val connected: Boolean
        get() = kotcPlayer.connected

    var dead: Boolean = false

    var spectating: Boolean = false

    var team: KOTCTeam = KOTCTeam.NONE
}