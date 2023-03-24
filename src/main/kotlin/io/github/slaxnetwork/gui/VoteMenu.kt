package io.github.slaxnetwork.gui

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.kyouko.models.profile.Profile
import io.github.slaxnetwork.mm
import me.tech.chestuiplus.GUIType
import me.tech.chestuiplus.gui
import me.tech.chestuiplus.item
import net.kyori.adventure.text.Component
import org.bukkit.Material

class VoteMenu(
    private val profile: Profile,
    private val gameVoteHandler: GameVoteHandler
) {
    fun get() = gui(
        KOTCGame.get(),
        mm.deserialize("<aqua>diamonds"),
        GUIType.HOPPER
    ) {
        fill(0, 0, 4, 0) {
            item = item(Material.GRAY_STAINED_GLASS_PANE) {
                name = Component.empty()
            }
        }

        for((i, game) in gameVoteHandler.gameVotePool.withIndex()) {
            slot(i, 0) {
//                item = item(game.display)
            }
        }
    }
}