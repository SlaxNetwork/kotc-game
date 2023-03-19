package io.github.slaxnetwork.game.microgame

import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer
import io.github.slaxnetwork.player.KOTCPlayer

interface MicroGameDeathHandler {
    val microGame: MicroGame

    fun handleDeath(
        killer: KOTCPlayer,
        victim: KOTCPlayer
    )
}