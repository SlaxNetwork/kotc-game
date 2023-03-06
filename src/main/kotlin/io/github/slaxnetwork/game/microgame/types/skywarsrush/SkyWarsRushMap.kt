package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

class SkyWarsRushMap(
    override val id: String,
    override val mapSection: ConfigurationSection
): MicroGameMap {
    override val spawnPoints: Set<Location> = getSpawnPointsFromConfig()

    override fun initialize() { }
}