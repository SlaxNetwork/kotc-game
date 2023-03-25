package io.github.slaxnetwork.game

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
import io.github.slaxnetwork.config.types.Config
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.mm
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import net.kyori.adventure.bossbar.BossBar
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

class GameStartHandler(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager,
    private val waitingRoomManager: WaitingRoomManager,
    private val gameVoteHandler: GameVoteHandler,
    private val scheduler: BukkitScheduler
) {
    private val config by injectConfig<Config>()

    private var startGameCountdownTask: BukkitTask? = null

    // :D
    private var countdown: Int = -1

//    private val bossBarMap = mutableMapOf<UUID, BossBar>()
    private var bossBar: BossBar? = null

    private fun startGameCountdown() {
        if(startGameCountdownTask != null) {
            KOTCLogger.debug("game-start", "startGameCountdown task is not null.")
            return
        }

        KOTCLogger.debug("game-start", "Starting runnable.")

        startGameCountdownTask = scheduler.runTaskTimer(KOTCGame.get(), Runnable {
            updateBossBar()

            if(countdown-- == 0) {
                startGame()
                countdown = -1
            }
        }, 20L, 20L)
    }

    fun startSlowGameCountdown() {
        if(countdown == -1) {
            countdown = SLOW_COUNTDOWN
            startGameCountdown()
        } else {
            if(SLOW_COUNTDOWN > countdown) {
                return
            }
            countdown = SLOW_COUNTDOWN
            startGameCountdown()
        }
    }

    fun startFastGameCountdown() {
        if(countdown == -1) {
            countdown = FAST_COUNTDOWN
            startGameCountdown()
        } else {
            if(FAST_COUNTDOWN > countdown) {
                return
            }
            countdown = FAST_COUNTDOWN
            startGameCountdown()
        }
    }

    fun cancelGameCountdown() {
        startGameCountdownTask?.let {
            it.cancel()
            startGameCountdownTask = null
        }
        countdown = -1
        hideBossBar()
    }

    fun startGame() {
        KOTCLogger.debug("game-start", "Attempting to start the game.")
        startGameCountdownTask?.let {
            it.cancel()
            startGameCountdownTask = null
        }
        hideBossBar()

        waitingRoomManager.preventNonAuthorizedConnections()
        gameVoteHandler.startGameVote()
    }

    private fun updateBossBar() {
        val bukkitPlayers = kotcPlayerRegistry.players
            .mapNotNull { it.bukkitPlayer }

        // if we add text put this in the loop so we can translate it.
        val text = mm.deserialize(
            "<white><countdown> <gray>| <white><player_total><gray>/<white><max_players>",
            Placeholder.unparsed("countdown", countdown.toString()),
            Placeholder.unparsed("player_total", bukkitPlayers.size.toString()),
            Placeholder.unparsed("max_players", config.game.start.maximum.toString())
        )

        if(bossBar == null) {
            bossBar = BossBar.bossBar(
                text,
                1.0f,
                BossBar.Color.WHITE,
                BossBar.Overlay.PROGRESS
            )
        } else {
            bossBar?.name(text)
        }

        for(bukkitPlayer in bukkitPlayers) {
            bukkitPlayer.showBossBar(bossBar ?: continue)
        }
    }

    private fun hideBossBar() {
        val bukkitPlayers = kotcPlayerRegistry.players
            .mapNotNull { it.bukkitPlayer }

        for(bukkitPlayer in bukkitPlayers) {
            bukkitPlayer.hideBossBar(bossBar ?: continue)
        }
    }

    companion object {
        private const val SLOW_COUNTDOWN = 60 * 2
        private const val FAST_COUNTDOWN = 15
    }
}