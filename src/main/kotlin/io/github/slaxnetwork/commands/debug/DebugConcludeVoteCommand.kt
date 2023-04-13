package io.github.slaxnetwork.commands.debug

import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.bukkitcore.utilities.command.checkPermission
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
        if(!sender.checkPermission("core.admin", profileRegistry, mm)) {
            return true
        }

        gameVoteHandler.submitVote((sender as Player).uniqueId, MicroGameType.SKYWARS_RUSH)
        gameVoteHandler.endGameVote()

        sender.sendMessage(mm.deserialize("<aqua>Ended game vote and submitted a vote as SkyWars Rush."))
        return true
    }
}