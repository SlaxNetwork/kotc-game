package io.github.slaxnetwork.config.types

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val debug: Debug,
    val game: Game
) {
    @Serializable
    data class Debug(
        @SerialName("config_override")
        val configOverride: Boolean,
        val logging: Boolean
    )

    @Serializable
    data class Game(
        val start: Start
    ) {
        @Serializable
        data class Start(
            val minimum: Int,
            val maximum: Int
        )
    }
}