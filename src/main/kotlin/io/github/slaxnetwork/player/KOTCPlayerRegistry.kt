package io.github.slaxnetwork.player

import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import java.util.*

class KOTCPlayerRegistry(
    private val profileRegistry: ProfileRegistry
) {
    private val _players = mutableSetOf<KOTCPlayer>()
    val players: Set<KOTCPlayer>
        get() = Collections.unmodifiableSet(_players)

    fun add(uuid: UUID): KOTCPlayer {
        val profile = profileRegistry.profiles[uuid]
            ?: throw NullPointerException("no profile found for $uuid")

        val kotcPlayer = KOTCPlayer(uuid, profile)
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