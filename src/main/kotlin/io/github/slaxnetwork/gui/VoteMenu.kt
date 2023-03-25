package io.github.slaxnetwork.gui

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.kyouko.models.profile.Profile
import io.github.slaxnetwork.mm
import me.tech.mcchestui.GUIType
import me.tech.mcchestui.gui
import me.tech.mcchestui.item
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import org.bukkit.Material

class VoteMenu(
    private val profile: Profile,
    private val gameVoteHandler: GameVoteHandler
) {
    fun get() = gui(
        KOTCGame.get(),
        mm.deserialize("<aqua>diamonds"),
        GUIType.CHEST,
        3
    ) {
        for((index, game) in gameVoteHandler.gameVotePool.withIndex()) {
            val xAxis = setOf(
                0 + (3 * index),
                1 + (3 * index),
                2 + (3 * index)
            )

            for(y in 0..2) {
                for(x in xAxis) {
                    val testMaterial = if(index == 0) {
                        Material.GRASS_BLOCK
                    } else if(index == 1) {
                        Material.DIAMOND_SWORD
                    } else {
                        Material.APPLE
                    }

                    slot(x, y) {
                        item = item(testMaterial) {
                            name = mm.deserialize("<aqua><game_name>", Placeholder.unparsed("game_name", game.name))

                        }
                        onClick = {
                            gameVoteHandler.submitVote(it.uniqueId, game)

                            it.closeInventory()
                        }
                    }
                }
            }
        }
    }
}