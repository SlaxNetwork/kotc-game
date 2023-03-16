package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameType
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
    playerRegistry: KOTCPlayerRegistry
) : MicroGame(type = MicroGameType.SKYWARS_RUSH, scheduler, playerRegistry, preGameTimer = 1),
    MicroGamePlayerRegistryHolder<SkyWarsRushPlayer>
{
    override val microGamePlayerRegistry = MicroGamePlayerRegistry<SkyWarsRushPlayer>()

    override fun startPreGame() {
        for(kotcPlayer in kotcPlayers) {
            microGamePlayerRegistry.add(SkyWarsRushPlayer(kotcPlayer))
        }

        KOTCTeamUtils.randomlyAssignToTeam(microGamePlayerRegistry.players)
    }

    override fun tickPreGameTimer() {
        KOTCLogger.info("Ticked timer.")
    }

    override fun startGame() {
        for(swPlayer in gamePlayers) {
            val bukkitPlayer = swPlayer.bukkitPlayer
                ?: continue

            if(!swPlayer.connected) {
                continue
            }

            val spawnPoint = map.getTeamSpawnPoints(swPlayer.team)
                .first()

            bukkitPlayer.teleport(spawnPoint.location.toCenterLocation())
        }
    }

    override fun endGame() {
//        kotcPlayers.onEach { it.team = KOTCTeam.NONE }
    }

    override fun initializeListeners(pluginManager: PluginManager) {
        registerListeners(
            setOf(
                SkyWarsRushPlayerDeathListener(this),
                SkyWarsRushPopulateChestListener(this)
            ),
            pluginManager
        )
    }
}