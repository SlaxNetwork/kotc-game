package io.github.slaxnetwork.config.types.game

import io.github.slaxnetwork.config.model.skywars.ConfigSkyWarsLootTableModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SkyWarsRushConfig(
    @SerialName("loot_table")
    val lootTable: ConfigSkyWarsLootTableModel,
    val maps: Map<String, ConfigSkyWarsMapModel>
)