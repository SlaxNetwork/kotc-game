package io.github.slaxnetwork.game.microgame.death

import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

interface MicroGameRespawnHandler {
    val microGame: MicroGame<*>

    fun respawn(player: MicroGamePlayer)
}