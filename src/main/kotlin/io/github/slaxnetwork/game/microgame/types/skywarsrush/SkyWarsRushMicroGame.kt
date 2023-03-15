package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayerRegistry
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayerRegistryHolder
import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.listeners.skywarsrush.SkyWarsRushPopulateChestListener
import io.github.slaxnetwork.listeners.skywarsrush.SkyWarsRushPlayerDeathListener
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
        val teams = KOTCTeam.values()
            .filter { it.valid }
            .toMutableSet()

        for(swPlayer in gamePlayers) {
            val team = teams.shuffled()
                .firstOrNull()
                ?: KOTCTeam.NONE

            swPlayer.team = team
            teams.remove(team)
        }
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

            val spawnPoint = map.spawnPoints[swPlayer.team.ordinal - 1]
            bukkitPlayer.teleport(spawnPoint)
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