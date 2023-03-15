package io.github.slaxnetwork.player

import java.util.*

class KOTCPlayerRegistry {
    private val _players = mutableSetOf<KOTCPlayer>()
    val players: Map<UUID, KOTCPlayer>
        get() = Collections.unmodifiableMap(_players.associateBy { it.uuid })

    fun add(uuid: UUID): KOTCPlayer {
        val kotcPlayer = KOTCPlayer(uuid)
        _players.add(kotcPlayer)

        return kotcPlayer
    }

    fun remove(uuid: UUID) {
        _players.remove(players[uuid] ?: return)
    }
}