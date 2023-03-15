package io.github.slaxnetwork.game.microgame.player

interface MicroGamePlayerRegistryHolder <Player : MicroGamePlayer> {
    val microGamePlayerRegistry: MicroGamePlayerRegistry<Player>

    val gamePlayers get() = microGamePlayerRegistry.players
}