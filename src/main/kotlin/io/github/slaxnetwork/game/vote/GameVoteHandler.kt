package io.github.slaxnetwork.game.vote

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.events.vote.GameVoteConcludeEvent
import io.github.slaxnetwork.events.vote.GameVoteStartedEvent
import io.github.slaxnetwork.game.microgame.MicroGameType
import org.bukkit.Bukkit
import org.bukkit.scheduler.BukkitScheduler
import java.util.UUID

class GameVoteHandler(private val scheduler: BukkitScheduler) {
    private val previouslyPlayed = mutableSetOf<MicroGameType>()

    private val votes = mutableSetOf<GameVote>()

    val gameVotePool = mutableListOf<MicroGameType>()

    var hasActiveVote: Boolean = false
        private set

    fun startGameVote() {
        hasActiveVote = true

        val games = MicroGameType.values()
            .filter { !previouslyPlayed.contains(it) }
            .shuffled()
            .take(3)

        gameVotePool.addAll(games)
        KOTCLogger.debug("game-vote", "Added games $games to the vote pool.")

        Bukkit.getPluginManager().callEvent(GameVoteStartedEvent())

        startGameVoteTimer()
    }

    fun submitVote(uuid: UUID, type: MicroGameType): Boolean {
        return votes.add(GameVote(uuid, type))
    }

    private fun startGameVoteTimer() {
        val task = {
            this.endGameVote()
        }
        // 30s.
        scheduler.scheduleSyncDelayedTask(KOTCGame.get(), task, 20L * 30)
    }

    fun endGameVote() {
        hasActiveVote = false

        val winner = votes.groupingBy { it.game }
            .eachCount()
            .entries.maxByOrNull { it.value }

        var game: MicroGameType? = null

        if(winner != null) {
            KOTCLogger.debug("game-vote", "Standard game vote concluded with ${winner.key} winning.")

            game = winner.key
            val voteAmount = winner.value

            Bukkit.getPluginManager().callEvent(GameVoteConcludeEvent(
                game,
                voteAmount
            ))
        // no winner was selected, pick random?
        } else {
            KOTCLogger.debug("game-vote", "No winner was able to be selected, selecting a random game.")

            game = gameVotePool.randomOrNull()
                ?: throw NullPointerException("no game was able to be selected since none are in the list.")

            Bukkit.getPluginManager().callEvent(GameVoteConcludeEvent(
                game,
                0
            ))
        }

        previouslyPlayed.add(game)
        votes.clear()
        gameVotePool.clear()
    }
}