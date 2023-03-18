package io.github.slaxnetwork.game.microgame.team

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

object KOTCTeamUtils {
    private val validTeams: List<KOTCTeam>
        get() = KOTCTeam.values().filter { it.valid }

    fun randomlyAssignToTeam(gamePlayers: Collection<MicroGamePlayer>) {
        val teams = validTeams.toMutableSet()

        for(gamePlayer in gamePlayers) {
            val team = teams.shuffled()
                .firstOrNull()
                ?: throw NullPointerException("no valid team was able to be assigned.")

            gamePlayer.team = team
            teams.remove(team)

            KOTCLogger.debug("team", "Assigned ${gamePlayer.bukkitPlayer?.name} to team $team.")
        }
    }

    fun evenlyAssignToTeam(gamePlayers: Collection<MicroGamePlayer>, teams: Set<KOTCTeam>) {

    }
}