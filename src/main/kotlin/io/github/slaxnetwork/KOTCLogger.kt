package io.github.slaxnetwork

import java.util.logging.Logger

object KOTCLogger {
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
}