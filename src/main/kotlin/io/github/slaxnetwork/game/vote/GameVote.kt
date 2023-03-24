package io.github.slaxnetwork.game.vote

import io.github.slaxnetwork.game.microgame.MicroGameType
import java.util.UUID

data class GameVote(
    val voter: UUID,
    val game: MicroGameType
)