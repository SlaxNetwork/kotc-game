package io.github.slaxnetwork

import io.github.slaxnetwork.microgame.MicroGame
import io.github.slaxnetwork.microgame.MicroGameType
import io.github.slaxnetwork.microgame.maps.MapManager
import io.github.slaxnetwork.microgame.types.skywarsrush.SkyWarsRushMicroGame
import io.github.slaxnetwork.player.KOTCPlayer
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler

class GameManager(
    private val playerRegistry: KOTCPlayerRegistry,
    private val mapManager: MapManager,
    private val scheduler: BukkitScheduler,
    private val pluginManager: PluginManager
) {
    /**
     * Current KOTC round.
     */
    var round = 0
        private set

    /**
     * Current [MicroGame] instance being played.
     */
    private var currentMicroGame: MicroGame? = null
        private set(value) {
            // inc round once micro game ends.
            if(value != null) {
                round++
            }

            field = value
        }

    var hasStarted = false

    var gameState: GameState = GameState.IN_LOBBY
        private set

    val isRunningMicroGame: Boolean
        get() = currentMicroGame != null

    /**
     * Current player holding the crown.
     */
    val currentCrownHolder: KOTCPlayer?
        get() = playerRegistry.players.values.firstOrNull { it.crownHolder }

    /**
     * Start a [MicroGame].
     * @param microGameType Micro Game to start.
     * @param mapId Manually passed map id.
     */
    fun startMicroGame(microGameType: MicroGameType, mapId: String? = null) {
        if(currentMicroGame != null) {
            return
        }

        val selectedMapId = mapId ?: mapManager.getRandomMapId(microGameType)
            ?: throw NullPointerException("no map for $microGameType has been selected.")

        val mapInstance = mapManager.loadMapInstance(microGameType, selectedMapId)

        val microGameInstance = when(microGameType) {
            MicroGameType.SKYWARS_RUSH -> SkyWarsRushMicroGame.create(scheduler, mapInstance, playerRegistry)

            else -> throw IllegalStateException("$microGameType is not a supported micro game.")
        }

        currentMicroGame = microGameInstance

        microGameInstance.map.initialize()
        microGameInstance.initializeListeners(pluginManager)

        microGameInstance.startPreGame()
        gameState = GameState.IN_GAME

        if(currentCrownHolder == null) {
            randomlyAssignCrown()
        }
    }

    /**
     * End the currently running [MicroGame].
     */
    fun endMicroGame() {
        if(!isRunningMicroGame) {
            return
        }

        currentMicroGame?.let { game ->
            game.map.delete()
            game.endGame()
        }
        gameState = GameState.IN_LOBBY

        // end kotc.
        if(round == MAX_ROUNDS) {
            gameState = GameState.ENDING
        }
    }

    /**
     * Randomly assign the crown to a player.
     */
    private fun randomlyAssignCrown() {
        val kotcPlayer = playerRegistry.players.values
            .filter { it.connected && !it.crownHolder }
            .shuffled()
            .firstOrNull()

        kotcPlayer?.crownHolder = true
    }

    companion object {
        const val MAX_ROUNDS = 3
    }
}