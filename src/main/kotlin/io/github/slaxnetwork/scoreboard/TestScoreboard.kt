package io.github.slaxnetwork.scoreboard

import io.github.slaxnetwork.bukkitcore.scoreboard.SimpleScoreboard
import io.github.slaxnetwork.bukkitcore.utilities.scoreboard.boardComponent
import io.github.slaxnetwork.bukkitcore.utilities.scoreboard.boardLine
import io.github.slaxnetwork.kyouko.models.profile.Profile

class TestScoreboard(private val profile: Profile) : SimpleScoreboard {
    override val title = boardComponent { text = "<red>Hello, World!" }

    override val lines = listOf(
        boardLine("test", 1) {
            text = "<rainbow>AAAA"
        }
    )
}