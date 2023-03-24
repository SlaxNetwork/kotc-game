package io.github.slaxnetwork.game

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.MicroGameState
import io.github.slaxnetwork.player.KOTCPlayer
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

class RubiesHandler(
    private val gameManager: GameManager,
    private val scheduler: BukkitScheduler
) {
    private var crownHolderRewardTask: BukkitTask? = null

    /**
     * Start the task to give the current crown holder
     * a certain amount of rubies every so often.
     */
    fun startRubiesRewardTask() {
        val runnable = Runnable {
            val currentCrownHolder = gameManager.currentCrownHolder
                ?: return@Runnable

            if(!gameManager.isRunningMicroGame) {
                return@Runnable
            }

            gameManager.currentMicroGame?.let { microGame ->
                if(microGame.state != MicroGameState.IN_GAME) {
                    return@let
                }

                addRubies(currentCrownHolder, 20)
            }

        }

        // TODO: 3/23/2023 temporary time period.
        crownHolderRewardTask = scheduler.runTaskTimer(KOTCGame.get(), runnable, 0L, 60L)
    }

    /**
     * End the task that gives the current crown holder rubies.
     */
    fun endRubiesRewardTask() {
        crownHolderRewardTask?.let {
            it.cancel()

            crownHolderRewardTask = null
        }
    }

    fun addRubies(kotcPlayer: KOTCPlayer, amount: Int) {
        kotcPlayer.addRubies(amount)
        // tmp
        kotcPlayer.bukkitPlayer?.sendMessage("You got $amount rubies!")
    }
}