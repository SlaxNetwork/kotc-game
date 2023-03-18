package io.github.slaxnetwork.game.microgame.team

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer

object KOTCTeamUtils {
    private val validTeams: List<KOTCTeam>
        get() = KOTCTeam.values().filter { it.valid }

    fun randomlyAssignToTeam(gamePlayers: Collection<MicroGamePlayer>) {
        val teams = validTeams.toMutableSet()

        for(mgPlayer in gamePlayers) {
            val team = teams.shuffled()
                .firstOrNull()
                ?: throw NullPointerException("no valid team was able to be assigned.")

            mgPlayer.team = team
            KOTCLogger.info(team.toString())
            teams.remove(team)
        }
    }

    fun evenlyAssignToTeam(gamePlayers: Collection<MicroGamePlayer>, teams: Set<KOTCTeam>) {

    }
}