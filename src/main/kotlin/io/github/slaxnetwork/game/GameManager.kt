package io.github.slaxnetwork.game

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameState
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.maps.MapManager
import io.github.slaxnetwork.game.microgame.impl.skywarsrush.SkyWarsRushMicroGame
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.player.KOTCPlayer
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler
import org.bukkit.scheduler.BukkitTask

class GameManager(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val waitingRoomManager: WaitingRoomManager,
    private val mapManager: MapManager,
    private val scheduler: BukkitScheduler,
    private val pluginManager: PluginManager,
    private val gameVoteHandler: GameVoteHandler
) {
    val rubiesHandler = RubiesHandler(this, scheduler)

    /**
     * Current KOTC round.
     */
    var round = 0
        private set

    /**
     * Current [MicroGame] instance being played.
     */
    var currentMicroGame: MicroGame<*>? = null
        private set(value) {
            // inc round once micro game ends.
            if(value != null) {
                round++
            }

            field = value
        }

    /**
     * Whether the KOTC Game has started.
     */
    val hasGameStarted: Boolean
        get() = round != 0

    val microGameState: MicroGameState
        get() = currentMicroGame?.state ?: MicroGameState.NOT_RUNNING

    val isRunningMicroGame: Boolean
        get() = currentMicroGame != null

    /**
     * Current player holding the crown.
     */
    val currentCrownHolder: KOTCPlayer?
        get() = kotcPlayerRegistry.players.firstOrNull { it.crownHolder }

    private val kotcPlayers get() = kotcPlayerRegistry.players

    private val gameStartHandler = GameStartHandler(kotcPlayerRegistry, this, waitingRoomManager, gameVoteHandler, scheduler)

    fun startGameCountdown(slow: Boolean) {
        if(hasGameStarted) {
            return
        }

        if(slow) {
            gameStartHandler.startSlowGameCountdown()
        } else {
            gameStartHandler.startFastGameCountdown()
        }
    }

    fun cancelGameCountdown() {
        gameStartHandler.cancelGameCountdown()
    }

    fun startGame() {
        gameStartHandler.startGame()
    }

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

        val microGameInstance = try {
            when(microGameType) {
                MicroGameType.SKYWARS_RUSH -> SkyWarsRushMicroGame.create(mapInstance, scheduler, kotcPlayerRegistry)
                else -> throw IllegalStateException("$microGameType is not a supported micro game.")
            }
        } catch(ex: Exception) {
            ex.printStackTrace()
            return
        }

        currentMicroGame = microGameInstance

        // init map
        mapInstance.initializeSpawnPoints()
        mapInstance.initialize()
        mapInstance.setupWorldBorder()
        // init micro game
        microGameInstance.initialize()
        microGameInstance.initializeListeners(pluginManager)

        microGameInstance.state = MicroGameState.IN_PRE_GAME

        rubiesHandler.startRubiesRewardTask()

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

        rubiesHandler.endRubiesRewardTask()

        currentMicroGame?.let { game ->
            game.state = MicroGameState.ENDING

            game.map.delete()
            game.destroyListeners()

            currentMicroGame = null
        }

        waitingRoomManager.setWorldBorder()

//        kotcPlayers.mapNotNull { it.bukkitPlayer }
//            .forEach {
//
//            }

        // end kotc.
        if(round == MAX_ROUNDS) {
//            gameState = GameState.ENDING
        }
    }

    /**
     * Randomly assign the crown to a player.
     */
    private fun randomlyAssignCrown() {
        val kotcPlayer = kotcPlayerRegistry.players
            .filter { it.connected && !it.crownHolder }
            .randomOrNull()

        kotcPlayer?.crownHolder = true
    }

//    private class GameStartHandler(private val scheduler: BukkitScheduler) {
//        fun startGameCountdown() {
//
//        }
//    }

    companion object {
        const val MAX_ROUNDS = 3
    }
}
