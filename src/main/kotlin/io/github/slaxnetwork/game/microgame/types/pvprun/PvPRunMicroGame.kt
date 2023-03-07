package io.github.slaxnetwork.game.microgame.types.pvprun

import io.github.slaxnetwork.game.microgame.MicroGame
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import io.github.slaxnetwork.listeners.pvprun.PvPRunPlayerMoveListener
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.plugin.PluginManager
import org.bukkit.scheduler.BukkitScheduler

class PvPRunMicroGame(
    map: MicroGameMap,
    scheduler: BukkitScheduler,
    playerRegistry: KOTCPlayerRegistry
) : MicroGame(
    type = MicroGameType.PVPRUN,
    map, scheduler, playerRegistry,
    preGameTimer = 25
) {
    override fun startPreGame() {
        TODO("Not yet implemented")
    }

    override fun tickPreGameTimer() {
        TODO("Not yet implemented")
    }

    override fun startGame() {
        TODO("Not yet implemented")
    }

    override fun endGame() {
        TODO("Not yet implemented")
    }

    override fun initializeListeners(pluginManager: PluginManager) {
        registerListeners(
            setOf(
                PvPRunPlayerMoveListener(this)
            ),
            pluginManager
        )
    }
}