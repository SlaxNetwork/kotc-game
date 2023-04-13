package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
import io.github.slaxnetwork.config.types.game.SkyWarsRushConfig
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.impl.skywarsrush.SkyWarsRushMap
import java.util.*

class MapManager {
    private val skyWarsRushConfig by injectConfig<SkyWarsRushConfig>()

    private val _maps = mutableMapOf<MicroGameType, MutableSet<String>>()
    val maps: Map<MicroGameType, Set<String>>
        get() = Collections.unmodifiableMap(_maps)

    /**
     * Initialize all game maps into memory without loading their instances.
     */
    @Throws(NullPointerException::class)
    fun initialize() {
        for(microGameType in MicroGameType.values()) {
            if(!_maps.containsKey(microGameType)) {
                _maps[microGameType] = mutableSetOf()
            }
        }

        _maps[MicroGameType.SKYWARS_RUSH]?.addAll(
            skyWarsRushConfig.maps.filterValues { it.enabled }
                .keys
        )
    }

    /**
     * Load an instance of a [MicroGameMap]
     * @param microGameType [MicroGameType] to use.
     * @param mapId map to load an instance of.
     */
    @Throws(NullPointerException::class, IllegalArgumentException::class)
    fun loadMapInstance(
        microGameType: MicroGameType,
        mapId: String
    ): MicroGameMap {
        // TODO: 3/17/2023 throw exception properly
        return when(microGameType) {
            MicroGameType.SKYWARS_RUSH -> SkyWarsRushMap(mapId, skyWarsRushConfig.maps[mapId] ?: throw NullPointerException())

            else -> throw IllegalArgumentException("$microGameType is an invalid micro game type.")
        }
    }

    fun getRandomMapId(microGameType: MicroGameType): String? {
        return maps[microGameType]
            ?.randomOrNull()
    }
}