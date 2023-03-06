package io.github.slaxnetwork.game.microgame

enum class MicroGameState {
    /**
     * No [MicroGame] is currently running.
     */
    NOT_RUNNING,

    WAITING,
    IN_PRE_GAME,
    IN_GAME,
    ENDING
}