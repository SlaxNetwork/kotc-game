package io.github.slaxnetwork.game.microgame.types.skywarsrush.loottable

data class SkyWarsRushLootTable(
    val spawn: Drops,
    val middle: Drops,
    val center: Drops
) {
    data class Drops(
        val drops: List<SkyWarsRushDrop>,
        val sorted: Boolean = true
    )
}
