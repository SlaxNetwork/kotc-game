package io.github.slaxnetwork.microgame

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.microgame.maps.MicroGameMap
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
    var preGameTimer: Int = PRE_GAME_TIMER_AMOUNT
        private set

    val players get() = playerRegistry.players.values

    abstract val gameListenerInstances: Set<Listener>

    abstract fun startPreGame()

    fun startPreGameTimer() {
        scheduler.runTaskTimer(
            KOTCGame.get(),
            Consumer {
                tickPreGameTimer()

                if(preGameTimer-- == 0) {
                    it.cancel()
                    startGame()
                }
            },
            20L, 20L
        )
    }

    abstract fun tickPreGameTimer()

    abstract fun startGame()

    abstract fun endGame()

    abstract fun initializeListeners(pluginManager: PluginManager)

    fun destroyListeners() {
        gameListenerInstances.forEach { HandlerList.unregisterAll(it) }
    }

    companion object {
        // in seconds.
        const val PRE_GAME_TIMER_AMOUNT = 25
    }
}