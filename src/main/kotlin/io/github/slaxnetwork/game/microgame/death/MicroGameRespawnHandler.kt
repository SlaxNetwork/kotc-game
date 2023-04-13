package io.github.slaxnetwork.game.microgame.death

import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

interface MicroGameRespawnHandler {
    fun respawn(player: MicroGamePlayer)
}