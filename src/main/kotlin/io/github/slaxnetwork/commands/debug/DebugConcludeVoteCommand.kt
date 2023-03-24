package io.github.slaxnetwork.commands.debug

import io.github.slaxnetwork.bukkitcore.minimessage.tags.LanguageTags
import io.github.slaxnetwork.bukkitcore.minimessage.tags.ProfileTags
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.game.vote.GameVoteHandler
import io.github.slaxnetwork.mm
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugConcludeVoteCommand(
    private val profileRegistry: ProfileRegistry,
    private val gameVoteHandler: GameVoteHandler
) : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val profile = if(sender is Player) profileRegistry.findByUUID(sender.uniqueId) else null
        if(!sender.hasPermission("core.admin")) {
            sender.sendMessage(
                mm.deserialize(
                "<icon:symbol_warning> <text>",
                LanguageTags.translateText("core.invalid_permissions", profile)
            ))
            return true
        }

        gameVoteHandler.submitVote((sender as Player).uniqueId, MicroGameType.SKYWARS_RUSH)
        gameVoteHandler.endGameVote()

        sender.sendMessage(mm.deserialize("<aqua>Ended game vote and submitted a vote as SkyWars Rush."))
        return true
    }
}