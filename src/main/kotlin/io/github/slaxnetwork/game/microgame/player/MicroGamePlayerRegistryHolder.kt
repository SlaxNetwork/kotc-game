package io.github.slaxnetwork.game.microgame.player

import io.github.slaxnetwork.game.microgame.MicroGame
import java.util.UUID

/**
 * A [MicroGame] that holds [MicroGamePlayer].
 */
interface MicroGamePlayerRegistryHolder <Player : MicroGamePlayer> {
    val microGamePlayerRegistry: MicroGamePlayerRegistry<Player>

    val gamePlayers: Set<Player>
        get() = microGamePlayerRegistry.players

    val connectedGamePlayers: Set<Player>
        get() = gamePlayers.filter { it.connected }.toSet()

    fun findGamePlayerByUUID(uuid: UUID): Player? {
        return gamePlayers.firstOrNull { it.kotcPlayer.uuid == uuid }
    }
}