package io.github.slaxnetwork.player

import java.util.*

class KOTCPlayerRegistry {
    private val _players = mutableSetOf<KOTCPlayer>()
    val players: Set<KOTCPlayer>
        get() = Collections.unmodifiableSet(_players)

    fun add(uuid: UUID): KOTCPlayer {
        val kotcPlayer = KOTCPlayer(uuid)
        _players.add(kotcPlayer)

        return kotcPlayer
    }

    fun remove(uuid: UUID) {
        _players.remove(findByUUID(uuid) ?: return)
    }

    fun findByUUID(uuid: UUID): KOTCPlayer? {
        return players.firstOrNull { it.uuid == uuid }
    }
}