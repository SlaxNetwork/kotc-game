package io.github.slaxnetwork.config.model

import kotlinx.serialization.Serializable
import net.kyori.adventure.key.Key as KyoriKey
import net.kyori.adventure.sound.Sound

@Serializable
data class ConfigSoundModel(
    val key: Key,
    val source: Sound.Source,
    val volume: Float,
    val pitch: Float
) {
    @Serializable
    data class Key(
        val namespace: String? = null,
        val value: String
    )

    fun toSound(): Sound {
        val key = KyoriKey.key(
            this.key.namespace ?: KyoriKey.MINECRAFT_NAMESPACE,
            this.key.value
        )
        return Sound.sound(key, source, volume, pitch)
    }
}
