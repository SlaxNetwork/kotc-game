package io.github.slaxnetwork.commands.debug

import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.bukkitcore.utilities.command.checkPermission
import io.github.slaxnetwork.mm
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugSwapScoreboardToggleCommand(
    private val profileRegistry: ProfileRegistry,
    private val scoreboardManager: ScoreboardManager
): CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if(!sender.checkPermission("core.admin", profileRegistry, mm)) {
            return true
        }

        if(sender !is Player) {
            return true
        }

        val fb = scoreboardManager.getFastBoard(sender)
            ?: return true

        if(args?.isEmpty() == true) {
            fb.switchBoard("voting")
            sender.sendMessage("Enabled")
        } else {
            fb.switchBoardToPrimary()
            sender.sendMessage("Disabled")
        }
        return true
    }
}