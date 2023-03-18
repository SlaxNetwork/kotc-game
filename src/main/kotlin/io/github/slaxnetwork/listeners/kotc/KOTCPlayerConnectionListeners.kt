package io.github.slaxnetwork.listeners.kotc

import io.github.slaxnetwork.bukkitcore.minimessage.tags.ProfileTags
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.events.KOTCPlayerDisconnectEvent
import io.github.slaxnetwork.events.KOTCPlayerReconnectEvent
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.mm
import io.github.slaxnetwork.player.KOTCPlayer
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener

class KOTCPlayerConnectionListeners(
    private val gameManager: GameManager,
    private val playerRegistry: KOTCPlayerRegistry,
    private val profileRegistry: ProfileRegistry
) : Listener {
    @EventHandler
    fun onKOTCPlayerReconnect(ev: KOTCPlayerReconnectEvent) {
    }

    @EventHandler
    fun onKOTCPlayerReconnectMidGame(ev: KOTCPlayerReconnectEvent) {
        if(!gameManager.isRunningMicroGame) {
            return
        }

        val bukkitPlayer = ev.kotcPlayer.bukkitPlayer
            ?: return
        val profile = profileRegistry.profiles[bukkitPlayer.uniqueId]
            ?: return

        bukkitPlayer.sendMessage(mm.deserialize(
            "<icon:symbol_warning> <text>",
            ProfileTags.translateText("test.message", profile) // warn about joining mid-match and not being able to join the micro game.
        ))
    }

    @EventHandler
    fun onKOTCPlayerDisconnect(ev: KOTCPlayerDisconnectEvent) {
        handleCrownHolderDisconnect(ev.kotcPlayer)
    }

    private fun handleCrownHolderDisconnect(kotcPlayer: KOTCPlayer) {
        if(!kotcPlayer.crownHolder) {
            return
        }
        kotcPlayer.crownHolder = false

        val bukkitPlayers = playerRegistry.players
            .mapNotNull { it.bukkitPlayer }

        for(bukkitPlayer in bukkitPlayers) {
            val profile = profileRegistry.profiles[bukkitPlayer.uniqueId]
                ?: return

            val message = mm.deserialize(
                "<icon:symbol_warning> <text>",
                ProfileTags.translateText(
                    id =  if(gameManager.isRunningMicroGame) "game.crown_holder.disconnect.match" else "game.crown_holder.disconnect.waiting",
                    profile = profile
                )
            )

            bukkitPlayer.sendMessage(message)
        }
    }
}