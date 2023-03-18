package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.config.types.game.ConfigSkyWarsMapModel
import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import org.bukkit.Material

class SkyWarsRushMap(
    id: String,
    private val mapConfig: ConfigSkyWarsMapModel
): MicroGameMap(id, mapConfig) {
    val chestDistances: ConfigSkyWarsMapModel.ChestDistance
        get() = mapConfig.chestDistance

    val chestTypes: List<Material>
        get() = mapConfig.chestTypes
}