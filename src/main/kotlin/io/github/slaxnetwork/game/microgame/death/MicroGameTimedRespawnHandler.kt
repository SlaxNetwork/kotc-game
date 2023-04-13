package io.github.slaxnetwork.game.microgame.death

import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

interface MicroGameTimedRespawnHandler : MicroGameRespawnHandler {
    fun startRespawnTimer(player: MicroGamePlayer)
}