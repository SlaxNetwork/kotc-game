package io.github.slaxnetwork.config.types.game

import io.github.slaxnetwork.config.model.ConfigBorderModel
import io.github.slaxnetwork.config.model.ConfigLocationModel
import io.github.slaxnetwork.config.model.ConfigSpawnPointsModel
import io.github.slaxnetwork.config.model.map.BaseMapConfig
import io.github.slaxnetwork.config.model.map.ConfigMapMetaModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import org.bukkit.Material

@Serializable
data class ConfigSkyWarsMapModel(
    @SerialName("world")
    override val worldName: String,

    override val enabled: Boolean,

    override val meta: ConfigMapMetaModel,

    override val center: ConfigLocationModel,

    @SerialName("death_spawn_point")
    override val deathSpawnPoint: ConfigLocationModel,

    override val border: ConfigBorderModel,

    @SerialName("chest_distance")
    val chestDistance: ChestDistance,

    @SerialName("chest_types")
    val chestTypes: List<Material>,

    @SerialName("spawn_points")
    override val spawnPoints: ConfigSpawnPointsModel
): BaseMapConfig {
    @Serializable
    data class ChestDistance(
        val spawn: Double,
        val center: Double
    )
}