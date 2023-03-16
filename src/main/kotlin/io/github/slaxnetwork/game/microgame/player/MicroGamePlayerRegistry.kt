package io.github.slaxnetwork.game.microgame.player

import java.util.*

class MicroGamePlayerRegistry <Player : MicroGamePlayer> {
    private val _players = mutableSetOf<Player>()

    val players: Set<Player>
        get() = Collections.unmodifiableSet(_players)

    private val mappedPlayers: Map<UUID, Player>
        get() = players.associateBy { it.kotcPlayer.uuid }

    fun add(player: Player): Player {
        _players.add(player)
        return player
    }

    fun findByUUID(uuid: UUID): Player? {
        return mappedPlayers[uuid]
    }
}