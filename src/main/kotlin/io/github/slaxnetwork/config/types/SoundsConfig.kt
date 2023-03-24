package io.github.slaxnetwork.config.types

import io.github.slaxnetwork.bukkitcommon.config.model.ConfigSoundModel
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SoundsConfig(
    val game: Game
) {
    @Serializable
    data class Game(
        @SerialName("player_death")
        val playerDeath: ConfigSoundModel
    )
}
