package io.github.slaxnetwork.game.microgame.death.impl

import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.death.MicroGameDeathHandler
import io.github.slaxnetwork.game.microgame.death.MicroGameTimedRespawnHandler
import io.github.slaxnetwork.game.microgame.death.RespawnableMicroGame
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

class DefaultDeathHandlerImpl(override val microGame: MicroGame<*>) : MicroGameDeathHandler {
    private val map get() = microGame.map

    override fun handleDeath(victim: MicroGamePlayer, killer: MicroGamePlayer) {
        val bukkitPlayer = victim.bukkitPlayer
            ?: return

        // player can respawn.
        if(microGame is RespawnableMicroGame) {
            val respawnHandler = microGame.respawnHandler
            // not timed.
            if(respawnHandler !is MicroGameTimedRespawnHandler) {
                respawnHandler.respawn(victim)
                return
            }

            // tp to death spawn then start timer.
            bukkitPlayer.teleport(map.spectatorSpawnPoint)
            respawnHandler.startRespawnTimer(victim)
            return
        }

        // teleport to death spawn point.
        bukkitPlayer.teleport(map.spectatorSpawnPoint)
    }
}