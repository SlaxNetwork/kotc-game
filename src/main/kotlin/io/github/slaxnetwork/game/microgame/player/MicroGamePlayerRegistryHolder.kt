package io.github.slaxnetwork.game.microgame.player

import io.github.slaxnetwork.game.microgame.MicroGame

/**
 * A [MicroGame] that holds [MicroGamePlayer].
 */
interface MicroGamePlayerRegistryHolder <Player : MicroGamePlayer> {
    val microGamePlayerRegistry: MicroGamePlayerRegistry<Player>

    val gamePlayers: Set<Player>
        get() = microGamePlayerRegistry.players

    val connectedGamePlayers: Set<Player>
        get() = gamePlayers.filter { it.connected }.toSet()
}