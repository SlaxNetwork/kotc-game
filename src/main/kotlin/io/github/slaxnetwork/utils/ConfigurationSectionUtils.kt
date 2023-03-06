package io.github.slaxnetwork.utils

import org.bukkit.Bukkit
import org.bukkit.Location
import org.bukkit.configuration.ConfigurationSection

/**
 * Convert a [ConfigurationSection] into a [Location]
 * @return [Location] or else null if an exception is thrown.
 */
fun ConfigurationSection.toBukkitLocation(): Location? {
    return try {
        val location = Location(
            Bukkit.getWorld(getString("world") ?: "world"),
            getDouble("x"),
            getDouble("y"),
            getDouble("z")
        )

        if(isSet("yaw") && isSet("pitch")) {
            location.yaw = getDouble("yaw").toFloat()
            location.pitch = getDouble("pitch").toFloat()
        }

        location
    } catch(ex: Exception) {
        null
    }
}