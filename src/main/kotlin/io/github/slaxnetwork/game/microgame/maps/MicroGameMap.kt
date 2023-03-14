package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

abstract class MicroGameMap(
    val id: String,
    private val mapSection: ConfigurationSection
) {
    val spawnPoints: Set<Location> = getSpawnPointsFromConfig()

    val center: Location = mapSection.getConfigurationSection("center")?.toBukkitLocation()
        ?: throw IllegalArgumentException("Map $id doesn't have a center point set in config.")

    val borderRadius: Double = getBorderRadiusFromConfig()

    open fun initialize() { }

    open fun delete() { }

    /**
     * Border radius from the configuration
     * @return the border radius.
     */
    private fun getBorderRadiusFromConfig(): Double {
        if (!mapSection.isDouble("border_radius")) {
            throw IllegalArgumentException("Border radius of map $id isn't set.")
        }

        return mapSection.getDouble("border_radius")
    }

    /**
     * All valid spawn points from the configuration.
     * @return spawn locations.
     */
    private fun getSpawnPointsFromConfig(): Set<Location> {
        val spawnPointsSection = mapSection.getConfigurationSection("spawnpoints")
            ?: return emptySet()

        val locations = mutableSetOf<Location>()

        for(spawnPointId in spawnPointsSection.getKeys(false)) {
            val spawnPointLocation = spawnPointsSection.getConfigurationSection(spawnPointId)
                ?.toBukkitLocation()
                ?: continue

            locations.add(spawnPointLocation)
        }

        return locations
    }
}
