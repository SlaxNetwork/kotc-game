package io.github.slaxnetwork.game.microgame.death.impl

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.death.MicroGameRespawnHandler
import io.github.slaxnetwork.game.microgame.death.MicroGameTimedRespawnHandler
import io.github.slaxnetwork.game.microgame.death.RespawnableMicroGame
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer
import io.github.slaxnetwork.game.microgame.team.KOTCTeam

class DefaultRespawnHandlerImpl(override val microGame: MicroGame<*>) : MicroGameRespawnHandler {
    private val map get() = microGame.map

    override fun respawn(player: MicroGamePlayer) {
        val bukkitPlayer = player.bukkitPlayer
            ?: return

        val spawnPoint = if(player.team == KOTCTeam.NONE) {
            map.allSpawnPoints.randomOrNull()
        } else {
            map.teamSpawnPoints[player.team]?.randomOrNull()
        }

        if(spawnPoint == null) {
            KOTCLogger.debug("respawn-handler", "Unable to find a spawn point for ${bukkitPlayer.name} on team ${player.team}.")
            return
        }

        bukkitPlayer.teleport(spawnPoint)
    }
}