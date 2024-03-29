package io.github.slaxnetwork

import io.github.slaxnetwork.bukkitcore.utilities.config.injectConfig
import io.github.slaxnetwork.config.types.Config
import java.util.logging.Logger

object KOTCLogger {
    val config by injectConfig<Config>()

    private val logger: Logger
        get() = KOTCGame.get().logger

    fun info(message: String) {
        logger.info(message)
    }

    fun warning(message: String) {
        logger.warning(message)
    }

    fun error(message: String) {
        logger.severe(message)
    }

    fun debug(prefix: String, message: String) {
        if(config.debug.logging) {
            logger.info("[debug-$prefix] $message")
        }
    }
}