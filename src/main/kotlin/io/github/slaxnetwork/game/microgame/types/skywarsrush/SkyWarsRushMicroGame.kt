package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayerRegistry
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayerRegistryHolder
import io.github.slaxnetwork.game.microgame.team.KOTCTeamUtils
import io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners.SkyWarsRushPopulateChestListener
import io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners.SkyWarsRushPlayerDeathListener
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler

class SkyWarsRushMicroGame(
    override val map: SkyWarsRushMap,
    scheduler: BukkitScheduler,
    kotcPlayerRegistry: KOTCPlayerRegistry
) : MicroGame(type = MicroGameType.SKYWARS_RUSH, scheduler, kotcPlayerRegistry, preGameTimer = 1),
    MicroGamePlayerRegistryHolder<SkyWarsRushPlayer>
{
    override val microGamePlayerRegistry = MicroGamePlayerRegistry<SkyWarsRushPlayer>()

    override fun initialize() {
        for(kotcPlayer in kotcPlayers) {
            microGamePlayerRegistry.add(SkyWarsRushPlayer(kotcPlayer))
        }

        KOTCTeamUtils.randomlyAssignToTeam(gamePlayers)
    }

    override fun startPreGame() { }

    override fun tickPreGameTimer() { }

    override fun startGame() {
        for(swPlayer in connectedGamePlayers) {
            val bukkitPlayer = swPlayer.bukkitPlayer
                ?: continue

            val spawnPoint = map.teamSpawnPoints[swPlayer.team]
                ?.firstOrNull()
                ?: continue

            bukkitPlayer.teleport(spawnPoint.toCenterLocation())
        }
    }

    override fun endGame() { }

    override fun initializeListeners(pluginManager: PluginManager) {
        registerListeners(
            setOf(
                SkyWarsRushPlayerDeathListener(this),
                SkyWarsRushPopulateChestListener(this)
            ),
            pluginManager
        )
    }

    companion object {
        @Throws(IllegalStateException::class)
        fun create(
            map: MicroGameMap,
            scheduler: BukkitScheduler,
            kotcPlayerRegistry: KOTCPlayerRegistry
        ): SkyWarsRushMicroGame {
            if(map !is SkyWarsRushMap) {
                throw IllegalStateException("mapInstance must be a SkyWarsRushMap.")
            }

            return SkyWarsRushMicroGame(map, scheduler, kotcPlayerRegistry)
        }
    }
}