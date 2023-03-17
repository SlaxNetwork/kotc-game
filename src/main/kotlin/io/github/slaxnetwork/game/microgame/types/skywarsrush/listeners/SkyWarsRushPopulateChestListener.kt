package io.github.slaxnetwork.game.microgame.types.skywarsrush.listeners

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMap
import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMicroGame
import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushPlayer
import io.github.slaxnetwork.game.microgame.types.skywarsrush.loottable.SkyWarsRushLootTable
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
    private val skyWars: SkyWarsRushMicroGame,
    private val lootTable: SkyWarsRushLootTable
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

    private val teamToWoolMap = mapOf(
        KOTCTeam.RED to Material.RED_CONCRETE,
        KOTCTeam.CYAN to Material.CYAN_CONCRETE,
        KOTCTeam.GRAY to Material.GRAY_CONCRETE,
        KOTCTeam.DARK_BLUE to Material.BLUE_CONCRETE,
        KOTCTeam.GREEN to Material.GREEN_CONCRETE,
        KOTCTeam.LIGHT_BLUE to Material.LIGHT_BLUE_CONCRETE,
        KOTCTeam.LIME to Material.LIME_CONCRETE,
        KOTCTeam.MAGENTA to Material.MAGENTA_CONCRETE,
        KOTCTeam.ORANGE to Material.ORANGE_CONCRETE,
        KOTCTeam.PINK to Material.PINK_CONCRETE,
        KOTCTeam.PURPLE to Material.PURPLE_CONCRETE,
        KOTCTeam.YELLOW to Material.YELLOW_CONCRETE,
        KOTCTeam.NONE to Material.WHITE_CONCRETE
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

        val swPlayer = skyWars.microGamePlayerRegistry.findByUUID(ev.player.uniqueId)
            ?: return

        populateChest(swPlayer, block)
    }

    private fun populateChest(swPlayer: SkyWarsRushPlayer, block: Block) {
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
            ChestType.SPAWN -> containerItems[ChestType.SPAWN]?.forEach {
                KOTCLogger.info(swPlayer.team.toString())
                if(it.type == Material.WHITE_CONCRETE) {
                    containerInventory.addItem(ItemStack(teamToWoolMap[swPlayer.team] ?: Material.WHITE_CONCRETE, 64))
                } else {
                    containerInventory.addItem(it)
                }
            }
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