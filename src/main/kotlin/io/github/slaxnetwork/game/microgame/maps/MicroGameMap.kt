package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.utils.toBukkitLocation
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.configuration.ConfigurationSection

abstract class MicroGameMap(
    val id: String,
    private val mapSection: ConfigurationSection
) {
    val center: Location = mapSection.getConfigurationSection("center")
        ?.toBukkitLocation()
        ?: throw IllegalArgumentException("Map $id doesn't have a center point set in config.")

    val spawnPoints: List<MapSpawnPoint> = getSpawnPointsFromConfig()

    val world: World
        get() = center.world

    open fun initialize() { }

    open fun delete() { }

    fun getTeamSpawnPoints(team: KOTCTeam): List<MapSpawnPoint> {
        return spawnPoints.filter { it.team == team }
    }

    /**
     * Set the map world border.
     */
    fun setupWorldBorder() {
        val borderSection = mapSection.getConfigurationSection("border")
            ?: throw NullPointerException("section border for $id isn't set.")

        // all of these values are doubles, just iterate through it
        // because we're lazy, this is awful though so don't do this much.
        setOf("radius", "damage", "damage_buffer").forEach {
            if(!borderSection.isDouble(it)) {
                throw IllegalArgumentException("border $it for $id isn't set.")
            }
        }

        val radius = borderSection.getDouble("radius")
        val damage = borderSection.getDouble("damage")
        val damageBuffer = mapSection.getDouble("damage_buffer")

        val worldBorder = center.world.worldBorder

        worldBorder.center = center
        worldBorder.size = radius
        worldBorder.damageAmount = damage
        worldBorder.damageBuffer = damageBuffer
    }

    /**
     * Border radius from the configuration
     * @return the border radius.
     */
    private fun getBorderRadiusFromConfig(): Double {
        if (!mapSection.isDouble("border_radius")) {
            throw IllegalArgumentException("Border radius of map $id isn't set.")
        }

        return mapSection.getDouble("border_radius")
    }

    /**
     * All valid spawn points from the configuration.
     * @return spawn locations.
     */
    private fun getSpawnPointsFromConfig(): List<MapSpawnPoint> {
        fun getSpawnPointLocations(section: ConfigurationSection): List<Location> {
            return section.getKeys(false)
                .mapNotNull { section.getConfigurationSection(it)?.toBukkitLocation(world.name) }
        }

        val spawnPointsSection = mapSection.getConfigurationSection("spawn_points")
            ?: return emptyList()

        val spawnPoints = mutableListOf<MapSpawnPoint>()

        if(spawnPointsSection.isSet("teams")) run {
            val teamsSpawnPointSection = spawnPointsSection.getConfigurationSection("teams")
                ?: return@run

            for(teamId in teamsSpawnPointSection.getKeys(false)) {
                val kotcTeam = try {
                    KOTCTeam.valueOf(teamId.uppercase())
                } catch(ex: IllegalArgumentException) {
                    ex.printStackTrace()
                    continue
                }

                val teamSpawnPointSection = teamsSpawnPointSection.getConfigurationSection(teamId)
                    ?: return@run

                getSpawnPointLocations(teamSpawnPointSection)
                    .forEach { spawnPoints.add(MapSpawnPoint(it, kotcTeam)) }
            }
        }


        if(spawnPointsSection.isSet("all")) run {
            val allSpawnPointsSection = spawnPointsSection.getConfigurationSection("all")
                ?: return@run

            getSpawnPointLocations(allSpawnPointsSection)
                .forEach { spawnPoints.add(MapSpawnPoint(it, KOTCTeam.NONE)) }
        }

        if(spawnPoints.isEmpty()) {
            throw IllegalStateException("no spawnpoints have been made for map $id.")
        }

        return spawnPoints
    }
}
