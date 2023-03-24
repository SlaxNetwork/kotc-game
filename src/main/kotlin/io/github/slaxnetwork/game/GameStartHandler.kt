package io.github.slaxnetwork.game

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.MicroGameState
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask
import java.util.function.Consumer

class GameStartHandler(
    private val gameManager: GameManager,
    private val waitingRoomManager: WaitingRoomManager,
    private val gameVoteHandler: GameVoteHandler,
    private val scheduler: BukkitScheduler
) {
    private var startGameCountdownTask: BukkitTask? = null

    private var countdown = 30

    fun startGameCountdown() {
        if(startGameCountdownTask != null) {
            return
        }

        val runnable = Runnable {
            if(countdown-- != 0) {
                return@Runnable
            }

            startGame()
        }

        startGameCountdownTask = scheduler.runTaskTimer(KOTCGame.get(), runnable, 0L, 20L)
    }

    fun cancelGameCountdown() {
        startGameCountdownTask?.let {
            it.cancel()
            startGameCountdownTask = null
        }
    }

    fun startGame() {
        startGameCountdownTask?.let {
            it.cancel()
            startGameCountdownTask = null
        }

        waitingRoomManager.preventNonAuthorizedConnections()
        gameVoteHandler.startGameVote()
    }
}