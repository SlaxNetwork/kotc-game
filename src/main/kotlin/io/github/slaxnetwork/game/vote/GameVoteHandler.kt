package io.github.slaxnetwork.game.vote

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.events.vote.GameVoteConcludeEvent
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

        startGameVoteTimer()
    }

    fun submitVote(uuid: UUID, type: MicroGameType) {
        votes.add(GameVote(uuid, type))
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

        if(winner == null) {
            KOTCLogger.debug("game-vote", "Game vote concluded with no winner.")
            throw NullPointerException("no game winner was decided upon when the game vote ended.")
        }

        val (game, voteAmount) = winner

        Bukkit.getPluginManager().callEvent(GameVoteConcludeEvent(
            game,
            voteAmount
        ))

        previouslyPlayed.add(game)
        votes.clear()
        gameVotePool.clear()
    }

    private data class Vote(
        val uuid: UUID,
        val game: MicroGameType
    )
}