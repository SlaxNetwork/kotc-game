package io.github.slaxnetwork.game.microgame.impl.skywarsrush.listeners

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
import io.github.slaxnetwork.config.model.skywars.ConfigSkyWarsLootTableModel
import io.github.slaxnetwork.config.types.game.ConfigSkyWarsMapModel
import io.github.slaxnetwork.config.types.game.SkyWarsRushConfig
import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.game.microgame.impl.skywarsrush.SkyWarsRushMap
import io.github.slaxnetwork.game.microgame.impl.skywarsrush.SkyWarsRushMicroGame
import io.github.slaxnetwork.game.microgame.impl.skywarsrush.SkyWarsRushPlayer
import io.github.slaxnetwork.utils.getRandomEmptySlot
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
    private val skyWarsConfig by injectConfig<SkyWarsRushConfig>()

    private val lootTable: ConfigSkyWarsLootTableModel
        get() = skyWarsConfig.lootTable

    private val chestDistance: ConfigSkyWarsMapModel.ChestDistance
        get() = skyWars.map.chestDistances

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
        if(
            ev.action != Action.RIGHT_CLICK_BLOCK
            || !map.chestTypes.contains(ev.clickedBlock?.type)
        ) {
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
        val containerInventory = (block.state as? InventoryHolder)
            ?.inventory
            ?: return

        val chestType = getChestType(swPlayer.team, block.location)
        val dropTable = chestType.toDropTable(lootTable)

        val drops = try {
            getDropsFromTable(dropTable)
        } catch(ex: IllegalArgumentException) {
            ex.printStackTrace()
            mutableListOf()
        }
        if(chestType == ChestType.SPAWN || chestType == ChestType.SPAWN_OTHER) {
            for(drop in drops) {
                if(drop.material != Material.WHITE_CONCRETE) {
                    continue
                }

                drop.material = teamToWoolMap[swPlayer.team] ?: Material.WHITE_CONCRETE
            }
        }

        // Mainly just for development.
        containerInventory.clear()

        for(drop in drops) {
            if(dropTable.sorted) {
                containerInventory.addItem(ItemStack(drop.material, drop.amount))
            } else {
                containerInventory.setItem(containerInventory.getRandomEmptySlot(), ItemStack(drop.material, drop.amount))
            }
        }

        invalidChests.add(block.location)
    }

    private fun getChestType(team: KOTCTeam, blockLocation: Location): ChestType {
        if(blockLocation.distance(map.center) <= chestDistance.center) {
            return ChestType.CENTER
        }

        for((kotcTeam, locations) in map.teamSpawnPoints) {
            val spawnPoint = locations.firstOrNull()
                ?: continue

            if(blockLocation.distance(spawnPoint) <= chestDistance.spawn) {
                return if(kotcTeam == team) {
                    ChestType.SPAWN
                } else {
                    ChestType.SPAWN_OTHER
                }
            }
        }

        return ChestType.MIDDLE
    }

    @Throws(IllegalArgumentException::class)
    private fun getDropsFromTable(
        dropTable: ConfigSkyWarsLootTableModel.DropTable
    ): MutableList<ConfigSkyWarsLootTableModel.Drop> {
        val (min, max) = dropTable.amount
        val dropAmount = (min..max).random()

        KOTCLogger.debug("sw-populate", "minimum = $min, maximum = $max, dropAmt = $dropAmount")

        if(dropTable.drops.size < dropAmount) {
            throw IllegalArgumentException("cannot populate with $dropAmount drops with a maximum drop count of ${dropTable.drops.size}")
        }

        val drops = mutableSetOf<ConfigSkyWarsLootTableModel.Drop>()

        do {
            val drop = dropTable.drops.filter { !drops.contains(it) }
                .randomOrNull()
                ?: break

            val randomNum = (0..100).random()
            if(randomNum <= drop.chance) {
                drops.add(drop)
            }
        } while(drops.size != dropAmount)

        return drops.toMutableList()
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
        SPAWN_OTHER,
        SPAWN;

        fun toDropTable(table: ConfigSkyWarsLootTableModel): ConfigSkyWarsLootTableModel.DropTable {
            return when(this) {
                SPAWN -> table.spawn
                SPAWN_OTHER -> table.spawnOther
                MIDDLE -> table.middle
                CENTER -> table.center
            }
        }
    }
}