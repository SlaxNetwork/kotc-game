package io.github.slaxnetwork

object KOTCLogger {
    private val logger by lazyOf(KOTCGame.get().logger)

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