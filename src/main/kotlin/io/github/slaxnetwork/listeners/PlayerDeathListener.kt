package io.github.slaxnetwork.listeners

import io.github.slaxnetwork.config.injectConfig
import io.github.slaxnetwork.config.types.SoundsConfig
import io.github.slaxnetwork.events.crown.KOTCCrownHolderDeathEvent
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.mm
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.minimessage.tag.resolver.Placeholder
import net.kyori.adventure.title.Title
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class PlayerDeathListener(
    private val kotcPlayerRegistry: KOTCPlayerRegistry,
    private val gameManager: GameManager
) : Listener {
    private val soundsConfig by injectConfig<SoundsConfig>()

    @EventHandler
    fun onPlayerDeath(ev: PlayerDeathEvent) {
        val microGame = gameManager.currentMicroGame
            ?: return

        val victim = ev.player
        val killer = ev.player.killer
            ?: return

        val gamePlayerKiller = microGame.findGamePlayerByUUID(killer.uniqueId)
            ?: return
        val gamePlayerVictim = microGame.findGamePlayerByUUID(victim.uniqueId)
            ?: return

        val subTitle = mm.deserialize(
            "<white><icon:emoji_skull> <red><killer_name>",
            Placeholder.unparsed("killer_name", killer.name)
        )
        victim.showTitle(Title.title(Component.empty(), subTitle))

        microGame.deathHandler.handleDeath(gamePlayerVictim, gamePlayerKiller)
    }

    @EventHandler
    fun onCrownHolderDeath(ev: PlayerDeathEvent) {
        val victim = ev.player
        val killer = ev.player.killer

        val kotcPlayerVictim = kotcPlayerRegistry.findByUUID(victim.uniqueId)
            ?: return

        if(!kotcPlayerVictim.crownHolder) {
            return
        }

        if(killer != null) {
            val kotcPlayerKiller = kotcPlayerRegistry.findByUUID(killer.uniqueId)
                ?: return

            Bukkit.getPluginManager().callEvent(KOTCCrownHolderDeathEvent(
                victim = kotcPlayerVictim,
                killer = kotcPlayerKiller
            ))
            return
        }

        Bukkit.getPluginManager().callEvent(KOTCCrownHolderDeathEvent(
            victim = kotcPlayerVictim,
            killer = null
        ))
    }
}