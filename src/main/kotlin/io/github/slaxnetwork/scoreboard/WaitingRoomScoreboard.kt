package io.github.slaxnetwork.scoreboard

import io.github.slaxnetwork.bukkitcore.scoreboard.BoardComponent
import io.github.slaxnetwork.bukkitcore.scoreboard.BoardLine
import io.github.slaxnetwork.bukkitcore.scoreboard.SimpleScoreboard
import io.github.slaxnetwork.kyouko.models.profile.Profile

class WaitingRoomScoreboard(private val profile: Profile) : SimpleScoreboard {
    override val title = BoardComponent("<red>Hello, World!")

    override val lines: List<BoardLine> = emptyList()
}