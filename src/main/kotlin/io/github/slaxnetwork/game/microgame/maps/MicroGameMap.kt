package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.config.model.map.BaseMapConfig
import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import org.bukkit.Location
import org.bukkit.World
import java.util.*

abstract class MicroGameMap(
    val id: String,
    private val mapConfig: BaseMapConfig
) {
    val center: Location = mapConfig.center
        .toBukkitLocation()
        ?: throw IllegalArgumentException("Map $id doesn't have a center point set in config.")

    val deathSpawnPoint: Location = mapConfig.deathSpawnPoint
        .toBukkitLocation()
        ?: throw IllegalArgumentException("Map $id doesn't have a death spawn point set in config.")

    private val _teamSpawnPoints = mutableMapOf<KOTCTeam, MutableList<Location>>()
    val teamSpawnPoints: Map<KOTCTeam, List<Location>>
        get() = Collections.unmodifiableMap(_teamSpawnPoints)

    private val _allSpawnPoints = mutableListOf<Location>()
    val allSpawnPoints: List<Location>
        get() = Collections.unmodifiableList(_allSpawnPoints)

    val world: World
        get() = center.world

    open fun initialize() { }

    open fun delete() { }

    @Deprecated("use property accessor", ReplaceWith("teamSpawnPoints[team] ?: emptyList()"))
    fun getTeamSpawnPoints(team: KOTCTeam): List<Location> {
        return teamSpawnPoints[team] ?: emptyList()
    }

    /**
     * Set the map world border.
     */
    fun setupWorldBorder() {
        val (radius, damage, damageBuffer) = mapConfig.border

        val worldBorder = world.worldBorder

        worldBorder.center = center
        worldBorder.size = radius
        worldBorder.damageAmount = damage
        worldBorder.damageBuffer = damageBuffer
    }

    /**
     * All valid spawn points from the configuration.
     * @return spawn locations.
     */
    @Throws(IllegalStateException::class)
    fun initializeSpawnPoints() {
        val (teamSpawnPoints, allSpawnPoints) = mapConfig.spawnPoints

        if(teamSpawnPoints != null) {
            for((teamId, locationModels) in teamSpawnPoints) {
                val kotcTeam = try {
                    KOTCTeam.valueOf(teamId.uppercase())
                } catch(ex: IllegalArgumentException) {
                    ex.printStackTrace()
                    continue
                }

                if(_teamSpawnPoints[kotcTeam] == null) {
                    _teamSpawnPoints[kotcTeam] = mutableListOf()
                }

                _teamSpawnPoints[kotcTeam]?.let {
                    for(location in locationModels.mapNotNull { it.toBukkitLocation(world.name) }) {
                        it.add(location)
                    }
                }
            }
        }

        if(allSpawnPoints != null) {
            allSpawnPoints
                .mapNotNull { it.toBukkitLocation(world.name) }
                .forEach { _allSpawnPoints.add(it) }
        }
    }
}
