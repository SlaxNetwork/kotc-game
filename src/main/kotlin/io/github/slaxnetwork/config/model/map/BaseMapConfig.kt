package io.github.slaxnetwork.config.model.map

import io.github.slaxnetwork.bukkitcore.utilities.config.model.ConfigBorderModel
import io.github.slaxnetwork.bukkitcore.utilities.config.model.ConfigLocationModel
import io.github.slaxnetwork.bukkitcore.utilities.config.model.map.ConfigMapMetaModel
import io.github.slaxnetwork.config.model.ConfigSpawnPointsModel
import kotlinx.serialization.SerialName

interface BaseMapConfig {
    @SerialName("world")
    val worldName: String

    val enabled: Boolean

    val meta: ConfigMapMetaModel

    val center: ConfigLocationModel

    @SerialName("spectator_spawn_point")
    val spectatorSpawnPoint: ConfigLocationModel

    val border: ConfigBorderModel

    @SerialName("spawn_points")
    val spawnPoints: ConfigSpawnPointsModel
}