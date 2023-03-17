package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayerRegistry
import io.github.slaxnetwork.game.microgame.player.MicroGamePlayerRegistryHolder
import io.github.slaxnetwork.game.microgame.team.KOTCTeamUtils
import io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners.SkyWarsRushPopulateChestListener
import io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners.SkyWarsRushPlayerDeathListener
import io.github.slaxnetwork.game.microgame.types.skywarsrush.loottable.SkyWarsRushLootTableLoader
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler
import java.io.File

class SkyWarsRushMicroGame(
    override val map: SkyWarsRushMap,
    scheduler: BukkitScheduler,
    playerRegistry: KOTCPlayerRegistry,
    private val lootTableConf: YamlConfiguration
) : MicroGame(type = MicroGameType.SKYWARS_RUSH, scheduler, playerRegistry, preGameTimer = 1),
    MicroGamePlayerRegistryHolder<SkyWarsRushPlayer>
{
    override val microGamePlayerRegistry = MicroGamePlayerRegistry<SkyWarsRushPlayer>()

    private val lootTable = SkyWarsRushLootTableLoader.loadFromConfig(lootTableConf)

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
        for(swPlayer in gamePlayers.filter { it.connected }) {
            val bukkitPlayer = swPlayer.bukkitPlayer
                ?: continue

            val spawnPoint = map.getTeamSpawnPoints(swPlayer.team)
                .first()

            bukkitPlayer.teleport(spawnPoint.location.toCenterLocation())
        }
    }

    override fun endGame() {
    }

    override fun initializeListeners(pluginManager: PluginManager) {
        registerListeners(
            setOf(
                SkyWarsRushPlayerDeathListener(this),
                SkyWarsRushPopulateChestListener(this, lootTable)
            ),
            pluginManager
        )
    }

    companion object {
        @Throws(IllegalStateException::class)
        fun create(
            map: MicroGameMap,
            scheduler: BukkitScheduler,
            kotcPlayerRegistry: KOTCPlayerRegistry,
            lootTableConfFile: File
        ): SkyWarsRushMicroGame {
            if(map !is SkyWarsRushMap) {
                throw IllegalStateException("mapInstance must be a SkyWarsRushMap.")
            }

            return SkyWarsRushMicroGame(map, scheduler, kotcPlayerRegistry, YamlConfiguration.loadConfiguration(lootTableConfFile))
        }
    }
}