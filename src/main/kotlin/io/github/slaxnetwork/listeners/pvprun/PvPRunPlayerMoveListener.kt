package io.github.slaxnetwork.listeners.pvprun

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.MicroGameState
import io.github.slaxnetwork.game.microgame.types.pvprun.PvPRunMicroGame
import org.bukkit.Material
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerMoveEvent

class PvPRunPlayerMoveListener(
    private val pvpRun: PvPRunMicroGame
) : Listener {
    private val scheduler get() = pvpRun.scheduler

    @EventHandler
    fun onPlayerMove(ev: PlayerMoveEvent) {
        if(pvpRun.state != MicroGameState.IN_GAME || !ev.hasChangedBlock()) {
            return
        }

        val blockBelowPlayer = ev.player.location.toBlockLocation().clone()
            .subtract(0.0, 1.0, 0.0)
            .block

        if(blockBelowPlayer.type != Material.SAND) {
            return
        }

        val blockBelowBlock = blockBelowPlayer.location.clone()
            .subtract(0.0, 1.0, 0.0)
            .block

        val task = {
            blockBelowPlayer.type = Material.AIR
            blockBelowBlock.type = Material.AIR
        }
        scheduler.scheduleSyncDelayedTask(KOTCGame.get(), task, 20L)
    }
}