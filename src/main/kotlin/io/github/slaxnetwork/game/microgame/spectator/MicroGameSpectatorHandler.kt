package io.github.slaxnetwork.game.microgame.spectator

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

abstract class MicroGameSpectatorHandler(
    private val microGame: MicroGame<*>
) {
    protected val protocolManager
        get() = KOTCGame.protocolManager

    private val connectedGamePlayers
        get() = microGame.connectedGamePlayers

    abstract fun setIsSpectating(gamePlayer: MicroGamePlayer)

    abstract fun setIsNotSpectating(gamePlayer: MicroGamePlayer)

    protected fun hideFromNonSpectators(gamePlayer: MicroGamePlayer) { }

    protected fun revealToNonSpectators(gamePlayer: MicroGamePlayer) { }
}