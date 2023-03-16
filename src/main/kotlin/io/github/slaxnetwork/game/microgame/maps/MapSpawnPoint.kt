package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import org.bukkit.Location

data class MapSpawnPoint(
    val location: Location,
    val team: KOTCTeam = KOTCTeam.NONE
)