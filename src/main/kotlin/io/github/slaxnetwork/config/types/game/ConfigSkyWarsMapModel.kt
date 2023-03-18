package io.github.slaxnetwork.config.types.game

import io.github.slaxnetwork.config.model.ConfigBorderModel
import io.github.slaxnetwork.config.model.ConfigLocationModel
import io.github.slaxnetwork.config.model.ConfigSpawnPointsModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Material

@Serializable
data class ConfigSkyWarsMapModel(
    @SerialName("world")
    val worldName: String,

    val enabled: Boolean,

    val meta: Meta,

    val center: ConfigLocationModel,

    val border: ConfigBorderModel,

    @SerialName("chest_distance")
    val chestDistance: ChestDistance,

    @SerialName("chest_types")
    val chestTypes: List<Material>,

    @SerialName("spawn_points")
    val spawnPoints: ConfigSpawnPointsModel
) {
    @Serializable
    data class Meta(
        val name: String,
        val contributors: List<String>
    )

    @Serializable
    data class ChestDistance(
        val spawn: Double,
        val center: Double
    )
}