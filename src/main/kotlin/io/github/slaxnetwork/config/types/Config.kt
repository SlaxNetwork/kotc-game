package io.github.slaxnetwork.config.types

import kotlinx.serialization.Serializable

@Serializable
data class Config(
    val logging: Logging
) {
    @Serializable
    data class Logging(
        val debug: Boolean
    )
}