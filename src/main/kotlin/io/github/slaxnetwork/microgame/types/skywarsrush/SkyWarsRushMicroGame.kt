package io.github.slaxnetwork.microgame.types.skywarsrush

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.listeners.skywarsrush.SkyWarsRushPlayerDeathListener
import io.github.slaxnetwork.microgame.*
import io.github.slaxnetwork.microgame.maps.MicroGameMap
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler

class SkyWarsRushMicroGame(
    map: SkyWarsRushMap,
    scheduler: BukkitScheduler,
    playerRegistry: KOTCPlayerRegistry
) : MicroGame(
    type = MicroGameType.SKYWARS_RUSH,
    map, scheduler, playerRegistry
) {
    override var gameListenerInstances = mutableSetOf<Listener>()

    override fun startPreGame() {
        startPreGameTimer()
    }

    override fun tickPreGameTimer() {
        Bukkit.getLogger().info("Ticked timer.")
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

                KOTCGame.get().logger.info(randomSpawnPoint.toString())

                player.teleport(randomSpawnPoint)
                takenSpawnPoints.add(randomSpawnPoint)
            }
    }

    override fun endGame() {
        this.destroyListeners()
    }

    override fun initializeListeners(pluginManager: PluginManager) {
        val plugin = KOTCGame.get()

        gameListenerInstances.addAll(setOf(
            SkyWarsRushPlayerDeathListener(this)
        ))

        gameListenerInstances.forEach { pluginManager.registerEvents(it, plugin) }
    }

    companion object : MicroGameCreator<SkyWarsRushMicroGame> {
        override fun create(
            scheduler: BukkitScheduler,
            map: MicroGameMap,
            playerRegistry: KOTCPlayerRegistry
        ): SkyWarsRushMicroGame {
            if (map !is SkyWarsRushMap) {
                throw IllegalArgumentException("${map.id} is not a SkyWarsRushMap.")
            }

            return SkyWarsRushMicroGame(map, scheduler, playerRegistry)
        }
    }
}