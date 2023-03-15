package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import org.bukkit.configuration.ConfigurationSection

class SkyWarsRushMap(
    id: String,
    mapSection: ConfigurationSection
): MicroGameMap(id, mapSection) {
    val chestDistances = ChestDistance(
        spawn = mapSection.getDouble("chests.distance.spawn"),
        center = mapSection.getDouble("chests.distance.center")
    )

    override fun initialize() {
    }

    data class ChestDistance(
        val spawn: Double,
        val center: Double
    )
}