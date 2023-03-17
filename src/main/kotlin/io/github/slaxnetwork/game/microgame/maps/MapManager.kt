package io.github.slaxnetwork.game.microgame.maps

import io.github.slaxnetwork.KOTCLogger
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.microgame.types.skywarsrush.SkyWarsRushMap
import org.bukkit.configuration.ConfigurationSection
import java.util.*

class MapManager(
    private val gamesSection: ConfigurationSection
) {
    private val _maps = mutableMapOf<MicroGameType, MutableSet<String>>()
    val maps: Map<MicroGameType, Set<String>>
        get() = Collections.unmodifiableMap(_maps)

    init {
        _maps.putAll(MicroGameType.values().map { Pair(it, mutableSetOf()) })
    }

    /**
     * Initialize all game maps into memory without loading their instances.
     */
    @Throws(NullPointerException::class)
    fun initialize() {
        for(microGameId in gamesSection.getKeys(false)) {
            val microGame = try {
               MicroGameType.valueOf(microGameId.uppercase())
            }  catch(ex: IllegalArgumentException) {
               ex.printStackTrace()
               continue
            }
            val microGameSection = gamesSection.getConfigurationSection(microGameId)
                ?: throw NullPointerException("$microGameId doesn't exist in games section.")

            if(_maps[microGame] == null) {
                _maps[microGame] = mutableSetOf()
            }

            val mapsSection = microGameSection.getConfigurationSection("maps")
                ?: throw NullPointerException("maps doesn't exist on $microGameId games section.")

            for(mapId in mapsSection.getKeys(false)) {
                val enabled = mapsSection.getConfigurationSection(mapId)
                    ?.getBoolean("enabled")
                    ?: false

                if(!enabled) {
                    KOTCLogger.info("skipping $mapId as it's marked as disabled.")
                    continue
                }

                _maps[microGame]
                    ?.add(mapId)
            }
        }
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
        val mapSection = gamesSection.getConfigurationSection(microGameType.configId)
            ?.getConfigurationSection("maps")
            ?.getConfigurationSection(mapId)
            ?: throw NullPointerException("${microGameType.configId}.maps.$mapId failed to return a ConfigurationSection.")

        return when(microGameType) {
            MicroGameType.SKYWARS_RUSH -> SkyWarsRushMap(mapId, mapSection)

            else -> throw IllegalArgumentException("$microGameType is an invalid micro game type.")
        }
    }

    fun getRandomMapId(microGameType: MicroGameType): String? {
        return maps[microGameType]
            ?.shuffled()
            ?.firstOrNull()
    }
}