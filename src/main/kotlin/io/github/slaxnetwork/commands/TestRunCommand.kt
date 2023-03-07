package io.github.slaxnetwork.commands

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.game.microgame.MicroGameType
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class TestRunCommand(
    private val inst: KOTCGame
) : CommandExecutor {
    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        inst.gameManager.startMicroGame(MicroGameType.PVPRUN)

        inst.logger.info(inst.gameManager.isRunningMicroGame.toString())

        return true
    }
}