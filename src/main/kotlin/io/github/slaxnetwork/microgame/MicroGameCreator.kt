package io.github.slaxnetwork.microgame

import io.github.slaxnetwork.microgame.maps.MicroGameMap
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.event.Listener
import org.bukkit.scheduler.BukkitScheduler

/**
 * Used for companion objects that create [MicroGame] instances.
 */
interface MicroGameCreator <T : MicroGame> {
    /**
     * Create a [MicroGame] instance.
     * @param scheduler [BukkitScheduler] to be used.
     * @param map [MicroGameMap] that will be played.
     * @param playerRegistry the [KOTCPlayerRegistry].
     * @return the [MicroGame] instance.
     */
    fun create(
        scheduler: BukkitScheduler,
        map: MicroGameMap,
        playerRegistry: KOTCPlayerRegistry
    ): T
}