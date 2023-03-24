package io.github.slaxnetwork.scoreboard

import io.github.slaxnetwork.bukkitcore.scoreboard.*
import io.github.slaxnetwork.bukkitcore.utils.boardLine
import io.github.slaxnetwork.kyouko.models.profile.Profile

class TestScoreboard(private val profile: Profile) : SimpleScoreboard {
    override val title = BoardComponent("<red>Hello, World!")

    override val lines = listOf(
        boardLine("test", 1) {
            text = "<rainbow>AAAA"
        }
    )
}