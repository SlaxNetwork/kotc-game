package io.github.slaxnetwork.game.microgame.death

import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

interface MicroGameDeathHandler {
    fun handleDeath(victim: MicroGamePlayer, killer: MicroGamePlayer)
}