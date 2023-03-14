package io.github.slaxnetwork.commands

import io.github.slaxnetwork.KOTCGame
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class EndGameCommand : CommandExecutor {
    private val inst get() = KOTCGame.get()
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        inst.gameManager.endMicroGame()

        return true
    }
}