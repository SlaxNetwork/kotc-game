package io.github.slaxnetwork.scoreboard

import io.github.slaxnetwork.bukkitcore.scoreboard.BoardComponent
import io.github.slaxnetwork.bukkitcore.scoreboard.BoardLine
import io.github.slaxnetwork.bukkitcore.scoreboard.SimpleScoreboard
import io.github.slaxnetwork.bukkitcore.utilities.scoreboard.boardLine
import io.github.slaxnetwork.kyouko.models.profile.Profile
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder

class WaitingRoomScoreboard(private val profile: Profile) : SimpleScoreboard {
    override val title = BoardComponent("<yellow><bold>KOTC")

    override val lines: List<BoardLine> = listOf(
        boardLine("players", 1) {
            text = "<rainbow><dig_down>"
            resolvers = Placeholder.unparsed("dig_down", profile.uuid.toString())
        }
    )
}