package io.github.slaxnetwork.commands.player

import io.github.slaxnetwork.bukkitcore.minimessage.tags.LanguageTags
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.gui.VoteMenu
import io.github.slaxnetwork.mm
import me.tech.chestuiplus.openGUI
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class VoteCommand(
    private val gameManager: GameManager,
    private val gameVoteHandler: GameVoteHandler,
    private val profileRegistry: ProfileRegistry
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>): Boolean {
        if(sender !is Player) {
            return true
        }

        val profile = profileRegistry.findByUUID(sender.uniqueId)
            ?: return true

        if(gameManager.isRunningMicroGame) {
            sender.sendMessage(mm.deserialize(
                "<icon:symbol_warning> <text>",
                LanguageTags.translateText("test.message", profile) // TODO: 3/22/2023 add message saying you can't vote during a active game.
            ))
            return true
        }

        if(!gameVoteHandler.hasActiveVote) {
            sender.sendMessage(mm.deserialize(
                "<icon:symbol_warning> <text>",
                LanguageTags.translateText("test.message", profile) // TODO: 3/22/2023 add message saying there's no active vote
            ))
            return true
        }

        sender.openGUI(VoteMenu(profile, gameVoteHandler).get())
        return true
    }
}