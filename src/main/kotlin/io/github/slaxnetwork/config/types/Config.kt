package io.github.slaxnetwork.config.types

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val logging: Logging,
    val game: Game
) {
    @Serializable
    data class Logging(
        val debug: Boolean
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