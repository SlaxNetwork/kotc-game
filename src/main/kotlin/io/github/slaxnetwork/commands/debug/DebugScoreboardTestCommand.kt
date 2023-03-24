package io.github.slaxnetwork.commands.debug

import io.github.slaxnetwork.KOTCGame
import io.github.slaxnetwork.bukkitcore.minimessage.tags.LanguageTags
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.bukkitcore.scoreboard.BoardLine
import io.github.slaxnetwork.bukkitcore.scoreboard.ScoreboardManager
import io.github.slaxnetwork.game.microgame.MicroGameType
import io.github.slaxnetwork.mm
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DebugScoreboardTestCommand(private val profileRegistry: ProfileRegistry) : CommandExecutor {
    private val sbMan get() = ScoreboardManager.get(KOTCGame.get().server.servicesManager)!!

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val profile = if (sender is Player) profileRegistry.findByUUID(sender.uniqueId) else null
        if (!sender.hasPermission("core.admin")) {
            sender.sendMessage(
                mm.deserialize(
                    "<icon:symbol_warning> <text>",
                    LanguageTags.translateText("core.invalid_permissions", profile)
                )
            )
            return true
        }

        if(args == null || args.isEmpty() || sender !is Player) {
            return true
        }
//        sbMan.create(sender)

        val a = args[0]
        var c = 0
        if(a.equals("test-1", true)) {
            for(i in 0..2000) {
//                sbMan.setLine(sender, 0, "test-$i")
            }
        }
        if(a.equals("test-1-comp", true)) {
            for(i in 0..750) {
//                sbMan.setLine(sender, 0, BoardLine("test-$i", TagResolver.empty()))
            }
        }

//        gameVoteHandler.submitVote((sender as Player).uniqueId, MicroGameType.SKYWARS_RUSH)
//        gameVoteHandler.endGameVote()

//        sender.sendMessage(mm.deserialize("<aqua>Ended game vote and submitted a vote as SkyWars Rush."))
        return true
    }
}