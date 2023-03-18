package io.github.slaxnetwork.config.model

import kotlinx.serialization.Serializable

@Serializable
data class ConfigSpawnPointsModel(
    val team: Map<String, List<ConfigLocationModel>>? = null,
    val all: List<ConfigLocationModel>? = null
)