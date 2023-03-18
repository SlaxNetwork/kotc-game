package io.github.slaxnetwork.config.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Bukkit
import org.bukkit.Location

@Serializable
data class ConfigLocationModel(
    @SerialName("world")
    val worldName: String? = null,
    val x: Double,
    val y: Double,
    val z: Double,
    val yaw: Float? = null,
    val pitch: Float? = null
) {
    fun toBukkitLocation(worldName: String? = null): Location? {
        return try {
            val world = Bukkit.getWorld(
                worldName ?: (this.worldName ?: "world")
            )

            val location = Location(
                world,
                this.x,
                this.y,
                this.z
            )

            if(this.yaw != null) location.yaw = this.yaw
            if(this.pitch != null) location.pitch = this.pitch

            location
        } catch(ex: Exception) {
            ex.printStackTrace()

            null
        }
    }
}
