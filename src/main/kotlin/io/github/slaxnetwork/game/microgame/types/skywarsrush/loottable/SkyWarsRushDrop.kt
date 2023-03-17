package io.github.slaxnetwork.game.microgame.types.skywarsrush.loottable

import org.bukkit.Material

data class SkyWarsRushDrop(
    val material: Material,
    val amount: Int,
    val chance: Double
)
