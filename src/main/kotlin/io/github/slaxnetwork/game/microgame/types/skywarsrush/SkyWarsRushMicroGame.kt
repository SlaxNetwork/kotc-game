package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import io.github.slaxnetwork.listeners.skywarsrush.SkyWarsRushPlayerDeathListener
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.Location
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler

class SkyWarsRushMicroGame(
    map: MicroGameMap,
    scheduler: BukkitScheduler,
    playerRegistry: KOTCPlayerRegistry
) : MicroGame(
    type = MicroGameType.SKYWARS_RUSH,
    map, scheduler, playerRegistry,
    preGameTimer = 30
) {
    override fun startPreGame() { }

    override fun tickPreGameTimer() {
        KOTCLogger.info("Ticked timer.")
    }

    override fun startGame() {
        val takenSpawnPoints = mutableSetOf<Location>()

        players.filter { it.connected }
            .mapNotNull { it.bukkitPlayer }
            .forEach { player ->
                val randomSpawnPoint = map.spawnPoints
                    .filter { !takenSpawnPoints.contains(it) }
                    .shuffled()
                    .firstOrNull()
                    ?: throw NullPointerException("no location found.")

                player.teleport(randomSpawnPoint)
                takenSpawnPoints.add(randomSpawnPoint)
            }
    }

    override fun endGame() {
    }

    override fun initializeListeners(pluginManager: PluginManager) {
        registerListeners(
            setOf(
                SkyWarsRushPlayerDeathListener(this)
            ),
            pluginManager
        )
    }
}