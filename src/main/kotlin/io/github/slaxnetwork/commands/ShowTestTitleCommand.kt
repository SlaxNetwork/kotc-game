package io.github.slaxnetwork.commands

import io.github.slaxnetwork.mm
import net.kyori.adventure.text.Component
import net.kyori.adventure.title.Title
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ShowTestTitleCommand : CommandExecutor {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if(!sender.hasPermission("core.admin")) {
            return true
        }

        val joined = args?.joinToString(" ") ?: ""
        val title = joined.substringAfter("title=").substringBefore(";")
        val subtitle = joined.substringAfter("subtitle=").substringBefore(";")

        val titleComp = if(title.equals("EMPTY", true)) Component.empty() else mm.deserialize(title)
        val subtitleComp = if(subtitle.equals("EMPTY", true)) Component.empty() else mm.deserialize(subtitle)

        sender.showTitle(Title.title(
            titleComp,
            subtitleComp
        ))

        return true
    }
}