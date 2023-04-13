package io.github.slaxnetwork.commands.debug

import io.github.slaxnetwork.bukkitcore.minimessage.tags.LanguageTags
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.mm
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugStartGameVoteCommand(
    private val profileRegistry: ProfileRegistry,
    private val gameVoteHandler: GameVoteHandler
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if(!sender.hasPermission("core.admin")) {
            val profile = if(sender is Player) profileRegistry.findByUUID(sender.uniqueId) else null

            sender.sendMessage(
                mm.deserialize(
                "<icon:symbol_warning> <text>",
                LanguageTags.translateText("core.invalid_permissions", profile)
            ))
            return true
        }

        gameVoteHandler.startGameVote()
        sender.sendMessage(mm.deserialize("<aqua>A game vote has started."))
        return true
    }
}