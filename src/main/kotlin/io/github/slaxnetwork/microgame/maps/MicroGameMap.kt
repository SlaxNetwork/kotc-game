package io.github.slaxnetwork.microgame.maps

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

interface MicroGameMap {
    val id: String

    val spawnPoints: Set<Location>

    val mapSection: ConfigurationSection

    fun initialize() { }

    fun delete() { }

    /**
     * All valid spawn points from the configuration.
     * @return spawn locations.
     */
    fun getSpawnPointsFromConfig(): Set<Location> {
        val spawnPointsSection = mapSection.getConfigurationSection("spawnpoints")
            ?: return emptySet()

        val locations = mutableSetOf<Location>()

        for(spawnPointId in spawnPointsSection.getKeys(false)) {
            val spawnPointSec = spawnPointsSection.getConfigurationSection(spawnPointId)
                ?: continue

            val location = Location.deserialize(mapOf(
                "world" to Bukkit.getWorld(spawnPointSec.getString("world") ?: "world"),
                "x" to spawnPointSec.getDouble("x"),
                "y" to spawnPointSec.getDouble("y"),
                "z" to spawnPointSec.getDouble("z"),
                "yaw" to spawnPointSec.getDouble("yaw").toFloat(),
                "pitch" to spawnPointSec.getDouble("pitch").toDouble()
            ))
            locations.add(location)
        }

        return locations
    }
}