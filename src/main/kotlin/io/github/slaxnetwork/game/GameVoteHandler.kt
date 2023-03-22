package io.github.slaxnetwork.game

import io.github.slaxnetwork.game.microgame.MicroGameType
import org.bukkit.scheduler.BukkitScheduler
import java.util.UUID

class GameVoteHandler(
    private val gameManager: GameManager,
    private val scheduler: BukkitScheduler
) {
    private val votes = mutableMapOf<MicroGameType, Set<UUID>>()

    fun startGameVote() {

    }

    fun submitVote(uuid: UUID, type: MicroGameType) {

    }

    private fun startGameVoteTimer() {

    }

    fun endGameVote() {

    }
}