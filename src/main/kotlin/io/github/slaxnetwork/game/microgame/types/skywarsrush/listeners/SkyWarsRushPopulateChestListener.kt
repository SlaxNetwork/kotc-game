package io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners

import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMap
import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMicroGame
import org.bukkit.Location
import org.bukkit.Material
import org.bukkit.block.Block
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.Action
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerInteractEvent
import org.bukkit.inventory.InventoryHolder
import org.bukkit.inventory.ItemStack

class SkyWarsRushPopulateChestListener(
    private val skyWars: SkyWarsRushMicroGame
) : Listener {
    // TODO: 3/14/2023 add loot table stuff
    private val containerItems = mapOf(
        ChestType.SPAWN to setOf(
            ItemStack(Material.STONE_SWORD, 1),
            ItemStack(Material.CHAINMAIL_LEGGINGS, 1),
            ItemStack(Material.WHITE_CONCRETE, 64)
        ),

        ChestType.MIDDLE to setOf(
            ItemStack(Material.DIRT)
        ),

        ChestType.CENTER to setOf(
            ItemStack(Material.DIAMOND_AXE)
        )
    )

    private val map: SkyWarsRushMap
        get() = skyWars.map

    private val invalidChests = mutableSetOf<Location>()

    @EventHandler
    fun onPlayerOpenChest(ev: PlayerInteractEvent) {
        if(ev.action != Action.RIGHT_CLICK_BLOCK || ev.clickedBlock?.type != Material.BARREL) {
            return
        }
        val block = ev.clickedBlock
            ?: return

        if(invalidChests.contains(block.location)) {
            return
        }

        populateChest(block)
    }

    private fun populateChest(block: Block) {
        fun determineChestType(): ChestType {
            val blockLoc = block.location

            if(blockLoc.distance(map.center) <= map.chestDistances.center) {
                return ChestType.CENTER
            // 12 iters.
            } else if(map.spawnPoints.any { blockLoc.distance(it.location) < map.chestDistances.spawn }) {
                return ChestType.SPAWN
            }

            // it's probably a middle chest otherwise.
            return ChestType.MIDDLE
        }

        val containerInventory = (block.state as? InventoryHolder)?.inventory
            ?: return

        when(determineChestType()) {
            // TODO: 3/14/2023 cleanup
            ChestType.SPAWN -> containerItems[ChestType.SPAWN]?.forEach { containerInventory.addItem(it) }
            ChestType.MIDDLE -> containerItems[ChestType.MIDDLE]?.forEach { containerInventory.addItem(it) }
            ChestType.CENTER -> containerItems[ChestType.CENTER]?.forEach { containerInventory.addItem(it) }
        }

        invalidChests.add(block.location)
    }

    @EventHandler
    fun onBlockPlace(ev: BlockPlaceEvent) {
        if(ev.block.type != Material.BARREL) {
            return
        }

        invalidChests.add(ev.block.location)
    }

    @EventHandler
    fun onBlockBreak(ev: BlockBreakEvent) {
        if(ev.block.type != Material.BARREL) {
            return
        }

        if(!invalidChests.contains(ev.block.location)) {
            return
        }
        invalidChests.add(ev.block.location)
    }

    private enum class ChestType {
        CENTER,
        MIDDLE,
        SPAWN
    }
}