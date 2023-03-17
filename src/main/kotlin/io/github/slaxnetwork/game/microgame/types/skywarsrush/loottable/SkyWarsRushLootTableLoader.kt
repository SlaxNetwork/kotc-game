package io.github.slaxnetwork.game.microgame.types.skywarsrush.loottable

import org.bukkit.Material
import org.bukkit.configuration.Configuration

object SkyWarsRushLootTableLoader {
    private val CHEST_TYPES = listOf("spawn", "middle", "center")

    fun loadFromConfig(lootTableConfig: Configuration): SkyWarsRushLootTable {
        val chestsSection = lootTableConfig.getConfigurationSection("chests")
            ?: throw NullPointerException()

        val tableDrops = mutableListOf<SkyWarsRushLootTable.Drops>()

        for(chestType in CHEST_TYPES) {
            val chestSection = chestsSection.getConfigurationSection(chestType)
                ?: continue

            val sorted = chestSection.getBoolean("sorted")
            val dropsSection = chestSection.getConfigurationSection("drops")
                ?: continue

            val drops = mutableListOf<SkyWarsRushDrop>()

            for(dropId in dropsSection.getKeys(false)) {
                val dropSection = dropsSection.getConfigurationSection(dropId)
                    ?: continue

                val drop = SkyWarsRushDrop(
                    Material.valueOf(dropSection.getString("material") ?: "STONE"),
                    dropSection.getInt("amount"),
                    dropSection.getDouble("chance")
                )
                drops.add(drop)
            }

            tableDrops.add(SkyWarsRushLootTable.Drops(
                drops,
                sorted
            ))
        }

        return SkyWarsRushLootTable(
            spawn = tableDrops[0],
            middle = tableDrops[1],
            center = tableDrops[2]
        )
    }
}