package io.github.slaxnetwork

import com.comphenix.protocol.ProtocolLibrary
import com.github.shynixn.mccoroutine.bukkit.SuspendingJavaPlugin
import io.github.slaxnetwork.bukkitcore.BukkitCoreAPI
import io.github.slaxnetwork.commands.debug.DebugEndGameCommand
import io.github.slaxnetwork.commands.debug.ShowTestTitleCommand
import io.github.slaxnetwork.commands.debug.TestRunCommand
import io.github.slaxnetwork.config.loadInjectableResources
import io.github.slaxnetwork.game.GameManager
import io.github.slaxnetwork.game.GameVoteHandler
import io.github.slaxnetwork.game.microgame.maps.MapManager
import io.github.slaxnetwork.listeners.PlayerDeathListener
import io.github.slaxnetwork.listeners.PlayerJoinListener
import io.github.slaxnetwork.listeners.PlayerQuitListener
import io.github.slaxnetwork.listeners.kotc.KOTCPlayerConnectionListeners
import io.github.slaxnetwork.listeners.kotc.KOTCPlayerCrownListeners
import io.github.slaxnetwork.player.KOTCPlayerRegistry
import io.github.slaxnetwork.waitingroom.WaitingRoomManager
import net.kyori.adventure.text.minimessage.MiniMessage

class KOTCGame : SuspendingJavaPlugin() {
    lateinit var bukkitCore: BukkitCoreAPI
        private set

    lateinit var kotcPlayerRegistry: KOTCPlayerRegistry
        private set

    lateinit var mapManager: MapManager
        private set

    lateinit var gameManager: GameManager
        private set

    lateinit var waitingRoomManager: WaitingRoomManager
        private set

    lateinit var gameVoteHandler: GameVoteHandler
        private set

    override suspend fun onLoadAsync() {
        loadInjectableResources(this)
    }

    override suspend fun onEnableAsync() {
        bukkitCore = BukkitCoreAPI.get(server.servicesManager)
            ?: throw RuntimeException("bukkit-core was unable to be loaded.")

        mm = bukkitCore.getBaseMiniMessageBuilder()
            .build()

        kotcPlayerRegistry = KOTCPlayerRegistry(bukkitCore.profileRegistry)

        mapManager = MapManager()
        mapManager.initialize()

        waitingRoomManager = WaitingRoomManager()

        gameManager = GameManager(kotcPlayerRegistry, waitingRoomManager, mapManager, server.scheduler, server.pluginManager)
        gameVoteHandler = GameVoteHandler(gameManager, server.scheduler)

        registerCommands()
        registerListeners()
    }

    override suspend fun onDisableAsync() { }

    private fun registerCommands() {
        // non-suspending commands.
        getCommand("test")?.setExecutor(TestRunCommand(this))
        getCommand("endgame")?.setExecutor(DebugEndGameCommand(gameManager, bukkitCore.profileRegistry))
        getCommand("showtesttitle")?.setExecutor(ShowTestTitleCommand())
    }

    private fun registerListeners() {
        // non-suspending listeners.
        setOf(
            PlayerJoinListener(kotcPlayerRegistry, gameManager, waitingRoomManager),
            PlayerQuitListener(kotcPlayerRegistry, gameManager),
            PlayerDeathListener(kotcPlayerRegistry, gameManager),

            KOTCPlayerConnectionListeners(gameManager, kotcPlayerRegistry),
            KOTCPlayerCrownListeners(gameManager.rubiesHandler)
        ).forEach { server.pluginManager.registerEvents(it, this) }
    }

    companion object {
        /**
         * Instance linking to the ProtocolLib [com.comphenix.protocol.ProtocolManager].
         *
         * Use this instance after [SuspendingJavaPlugin.onLoad] has completed.
         */
        val protocolManager get() = ProtocolLibrary.getProtocolManager()

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