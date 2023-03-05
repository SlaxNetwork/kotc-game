package io.github.slaxnetwork

import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import io.github.slaxnetwork.bukkitcore.BukkitCoreAPI
import io.github.slaxnetwork.commands.TestRunCommand
import io.github.slaxnetwork.listeners.PlayerDeathListener
import io.github.slaxnetwork.listeners.PlayerJoinListener
import io.github.slaxnetwork.listeners.PlayerQuitListener
import io.github.slaxnetwork.listeners.kotc.KOTCPlayerConnectionListeners
import io.github.slaxnetwork.listeners.kotc.KOTCPlayerCrownListeners
import io.github.slaxnetwork.microgame.maps.MapManager
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import net.kyori.adventure.text.minimessage.MiniMessage

class KOTCGame : SuspendingJavaPlugin() {
    lateinit var bukkitCore: BukkitCoreAPI
        private set

    lateinit var playerRegistry: KOTCPlayerRegistry
        private set

    lateinit var mapManager: MapManager
        private set

    lateinit var gameManager: GameManager
        private set

    lateinit var waitingRoomManager: WaitingRoomManager
        private set

    override suspend fun onEnableAsync() {
        saveDefaultConfig()

        bukkitCore = BukkitCoreAPI.get(server.servicesManager)
            ?: throw RuntimeException("bukkit-core was unable to be loaded.")

        mm = bukkitCore.getBaseMiniMessageBuilder()
            .build()

        playerRegistry = KOTCPlayerRegistry()

        mapManager = MapManager(config.getConfigurationSection("games") ?: throw NullPointerException("games section doesn't exist."))
        mapManager.initialize()

        gameManager = GameManager(playerRegistry, mapManager, server.scheduler, server.pluginManager)

        waitingRoomManager = WaitingRoomManager(playerRegistry, gameManager, bukkitCore.profileRegistry)

        getCommand("test")?.setExecutor(TestRunCommand(this))

        setOf(
            PlayerJoinListener(playerRegistry, waitingRoomManager),
            PlayerQuitListener(playerRegistry, gameManager),
            PlayerDeathListener(playerRegistry),

            KOTCPlayerConnectionListeners(),
            KOTCPlayerCrownListeners()
        ).forEach { server.pluginManager.registerEvents(it, this) }
    }

    companion object {
        /**
         * Singleton instance should not be used outside of
         * utilities that require the main instance of our plugin.
         *
         * Please do not randomly access data with this instance.
         */
        fun get(): KOTCGame {
            return getPlugin(KOTCGame::class.java)
        }
    }
}

/**
* Public [MiniMessage] instance.
*/
lateinit var mm: MiniMessage
    private set