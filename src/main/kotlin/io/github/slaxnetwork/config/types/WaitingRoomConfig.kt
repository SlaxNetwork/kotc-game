package io.github.slaxnetwork.config.types

import io.github.slaxnetwork.config.model.ConfigBorderModel
import io.github.slaxnetwork.config.model.ConfigLocationModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class WaitingRoomConfig(
    @SerialName("world")
    val worldName: String,

    @SerialName("spawn_point")
    val spawnPoint: ConfigLocationModel,

    val border: ConfigBorderModel
)