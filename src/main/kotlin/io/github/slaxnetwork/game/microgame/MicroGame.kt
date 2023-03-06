package io.github.slaxnetwork.game.microgame

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import io.github.slaxnetwork.player.KOTCPlayer
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.event.HandlerList
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler
import java.util.function.Consumer

abstract class MicroGame(
    val type: MicroGameType,
    val map: MicroGameMap,
    val scheduler: BukkitScheduler,
    val playerRegistry: KOTCPlayerRegistry
) {
    /**
     * Current state of the [MicroGame].
     */
    var state: MicroGameState = MicroGameState.WAITING
        set(value) {
            when(value) {
                MicroGameState.IN_PRE_GAME -> startPreGame()
                MicroGameState.IN_GAME -> startGame()
                MicroGameState.ENDING -> endGame()
                else -> {}
            }

            field = value
        }

    var winner: KOTCPlayer? = null

    var preGameTimer: Int = PRE_GAME_TIMER_AMOUNT
        private set

    val players get() = playerRegistry.players.values

    private val gameListeners = mutableSetOf<Listener>()

    abstract fun startPreGame()

    fun startPreGameTimer() {
        scheduler.runTaskTimer(
            KOTCGame.get(),
            Consumer {
                tickPreGameTimer()

                if(preGameTimer-- == 0) {
                    it.cancel()
                    state = MicroGameState.IN_GAME
                }
            },
            20L, 20L
        )
    }

    abstract fun tickPreGameTimer()

    abstract fun startGame()

    abstract fun endGame()

    abstract fun initializeListeners(pluginManager: PluginManager)

    protected fun registerListeners(
        listeners: Set<Listener>,
        pluginManager: PluginManager
    ) {
        gameListeners.addAll(listeners)

        for(listener in gameListeners) {
            pluginManager.registerEvents(listener, KOTCGame.get())
        }
    }

    fun destroyListeners() {
        for(listener in gameListeners) {
            HandlerList.unregisterAll(listener)
        }
        gameListeners.clear()
    }

    companion object {
        // in seconds.
        const val PRE_GAME_TIMER_AMOUNT = 25
    }
}