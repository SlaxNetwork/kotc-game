package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

abstract class MicroGameMap(
    val id: String,
    private val mapSection: ConfigurationSection
) {
    val spawnPoints: Set<Location> = getSpawnPointsFromConfig()

    val center: Location = mapSection.getConfigurationSection("center")
        ?.toBukkitLocation()
        ?: throw IllegalArgumentException("Map $id doesn't have a center point set in config.")

    private val world: World
        get() = center.world

    open fun initialize() { }

    open fun delete() { }

    /**
     * Set the map world border.
     */
    fun setupWorldBorder() {
        val borderSection = mapSection.getConfigurationSection("border")
            ?: throw NullPointerException("section border for $id isn't set.")

        // all of these values are doubles, just iterate through it
        // because we're lazy, this is awful though so don't do this much.
        setOf("radius", "damage", "damage_buffer").forEach {
            if(!borderSection.isDouble(it)) {
                throw IllegalArgumentException("border $it for $id isn't set.")
            }
        }

        val radius = borderSection.getDouble("radius")
        val damage = borderSection.getDouble("damage")
        val damageBuffer = mapSection.getDouble("damage_buffer")

        val worldBorder = world.worldBorder

        worldBorder.center = center
        worldBorder.size = radius
        worldBorder.damageAmount = damage
        worldBorder.damageBuffer = damageBuffer
    }

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
