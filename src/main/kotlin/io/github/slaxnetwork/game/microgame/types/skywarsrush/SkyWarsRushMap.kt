package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.game.microgame.maps.MicroGameMap
import org.bukkit.configuration.ConfigurationSection

class SkyWarsRushMap(
    id: String,
    mapSection: ConfigurationSection
): MicroGameMap(id, mapSection) {
    override fun initialize() {

    }
}