package io.github.slaxnetwork.game.microgame.spectator

import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

/**
 * Default implementation of how to handle spectators in a [MicroGame].
 */
class DefaultMicroGameSpectatorImpl(
    microGame: MicroGame<*>
) : MicroGameSpectatorHandler(microGame) {
    override fun setIsSpectating(gamePlayer: MicroGamePlayer) {
        hideFromNonSpectators(gamePlayer)
    }

    override fun setIsNotSpectating(gamePlayer: MicroGamePlayer) {
        revealToNonSpectators(gamePlayer)
    }
}