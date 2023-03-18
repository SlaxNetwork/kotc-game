/**
 * Please rewrite this entire file one day.
 */
package io.github.slaxnetwork.config

import io.github.slaxnetwork.KOTCGame
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
    Pair("waiting_room.json", WaitingRoomConfig::class),
    Pair("skywars.json", SkyWarsRushConfig::class)
)

private val configurationContainer = mutableMapOf<Any, Any>()
val CONFIGURATION_CONTAINER: Map<Any, Any>
    get() = Collections.unmodifiableMap(configurationContainer)

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

inline fun <reified T> injectConfig() = ReadOnlyProperty<Any?, T> { _, _ ->
    CONFIGURATION_CONTAINER[T::class] as T
}