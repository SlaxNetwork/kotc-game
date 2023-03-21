package io.github.slaxnetwork.game.microgame.death

import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

interface MicroGameDeathHandler {
    val microGame: MicroGame<*>

    fun handleDeath(victim: MicroGamePlayer, killer: MicroGamePlayer)
}