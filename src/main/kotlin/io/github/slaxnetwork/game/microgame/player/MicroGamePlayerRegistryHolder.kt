package io.github.slaxnetwork.game.microgame.player

interface MicroGamePlayerRegistryHolder <Player : MicroGamePlayer> {
    val microGamePlayerRegistry: MicroGamePlayerRegistry<Player>

    val gamePlayers: Set<Player>
        get() = microGamePlayerRegistry.players
}