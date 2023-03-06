package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

abstract class MicroGameMap(
    val id: String,
    private val mapSection: ConfigurationSection
) {
    val spawnPoints: Set<Location> = getSpawnPointsFromConfig()

    open fun initialize() { }

    open fun delete() { }

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