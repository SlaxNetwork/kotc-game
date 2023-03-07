package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

abstract class MicroGameMap(
    val id: String,
    private val mapSection: ConfigurationSection
) {
    val spawnPoints: Set<Location> = getSpawnPointsFromConfig()

    val center: Location = mapSection.getConfigurationSection("center")?.toBukkitLocation() ?:
        throw IllegalArgumentException("Map $id doesn't have a center point set in config.")

    val borderRadius: Int = getBorderRadiusFromConfig()

    open fun initialize() { }

    open fun delete() { }

    private fun getBorderRadiusFromConfig(): Int {
        if (!mapSection.isSet("border_radius")) {
            throw IllegalArgumentException("Border radius of map $id isn't set.")
        }
        if (!mapSection.isInt("border_radius")) {
            throw IllegalArgumentException("Border radius of map $id isn't an integer.")
        }
        return mapSection.getInt("border_radius")
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
