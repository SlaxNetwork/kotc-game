package io.github.slaxnetwork.config.model

import io.github.slaxnetwork.bukkitcommon.config.model.ConfigLocationModel
import kotlinx.serialization.Serializable

@Serializable
data class ConfigSpawnPointsModel(
    val team: Map<String, List<ConfigLocationModel>>? = null,
    val all: List<ConfigLocationModel>? = null
)