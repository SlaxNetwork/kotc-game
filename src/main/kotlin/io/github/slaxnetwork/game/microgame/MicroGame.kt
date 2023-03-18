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
    val scheduler: BukkitScheduler,
    val kotcPlayerRegistry: KOTCPlayerRegistry,

    private var preGameTimer: Int = 30
) {
    abstract val map: MicroGameMap

    /**
     * Current state of the [MicroGame].
     */
    var state: MicroGameState = MicroGameState.WAITING
        set(value) {
            when(value) {
                MicroGameState.IN_PRE_GAME -> {
                    startPreGame()
                    startPreGameTimer()
                }
                MicroGameState.IN_GAME -> startGame()
                MicroGameState.ENDING -> endGame()
                else -> {}
            }

            field = value
        }

    var winner: KOTCPlayer? = null

    val kotcPlayers get() = kotcPlayerRegistry.players.values

    private val gameListeners = mutableSetOf<Listener>()

    abstract fun startPreGame()

    /**
     * Start a timer that will tick down until
     * the game starts.
     */
    private fun startPreGameTimer() {
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

    /**
     * Method that will run every time the pre-game timer
     * is ticked.
     */
    abstract fun tickPreGameTimer()

    abstract fun startGame()

    abstract fun endGame()

    /**
     * Initialize all [Listener] related to a [MicroGame]
     */
    abstract fun initializeListeners(pluginManager: PluginManager)

    /**
     * Register the set of [Listener].
     * @param listeners Listeners to register.
     * @param pluginManager [PluginManager]
     */
    protected fun registerListeners(
        listeners: Set<Listener>,
        pluginManager: PluginManager
    ) {
        gameListeners.addAll(listeners)

        for(listener in gameListeners) {
            pluginManager.registerEvents(listener, KOTCGame.get())
        }
    }

    /**
     * Unregister all listeners assigned to a [MicroGame].
     */
    fun destroyListeners() {
        for(listener in gameListeners) {
            HandlerList.unregisterAll(listener)
        }
        gameListeners.clear()
    }
}