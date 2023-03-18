/**
 * Please rewrite this entire file one day.
 */
package io.github.slaxnetwork.config

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.config.types.Config
import io.github.slaxnetwork.config.types.SoundsConfig
import io.github.slaxnetwork.config.types.WaitingRoomConfig
import io.github.slaxnetwork.config.types.game.SkyWarsRushConfig
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.decodeFromStream
import kotlinx.serialization.serializerOrNull
import java.io.File
import java.util.*
import kotlin.properties.ReadOnlyProperty

private val RESOURCES = setOf(
    Pair("config.json", Config::class),
    Pair("sounds.json", SoundsConfig::class),
    Pair("waiting_room.json", WaitingRoomConfig::class),
    Pair("skywars.json", SkyWarsRushConfig::class)
)

private val configurationContainer = mutableMapOf<Any, Any>()
val CONFIGURATION_CONTAINER: Map<Any, Any>
    get() = Collections.unmodifiableMap(configurationContainer)

/**
 * Load all configurations in resources.
 * @param plugin Instance to [KOTCGame].
 */
@OptIn(ExperimentalSerializationApi::class)
fun loadInjectableResources(plugin: KOTCGame) {
    for((resourcePath, kClass) in RESOURCES) {
        plugin.saveResource(resourcePath, true)

        if(!resourcePath.endsWith(".json", true)) {
            continue
        }

        val serializer = serializerOrNull(kClass.javaObjectType)
            ?: continue

        val inputStream = File(plugin.dataFolder, resourcePath).inputStream()

        configurationContainer[kClass] = Json.decodeFromStream(serializer, inputStream)

        inputStream.close()
    }
}

/**
 * Inject a configuration by its type
 * @param Config Type of Configuration to inject.
 */
inline fun <reified Config> injectConfig() = ReadOnlyProperty<Any?, Config> { _, _ ->
    val conf = CONFIGURATION_CONTAINER[Config::class] as Config
        ?: throw NullPointerException("no configuration ${Config::class.simpleName} was found in the configuration container.")
    conf
}