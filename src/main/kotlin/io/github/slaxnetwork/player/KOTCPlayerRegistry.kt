package io.github.slaxnetwork.player

import io.github.slaxnetwork.bukkitcore.minimessage.tags.LanguageTags
import io.github.slaxnetwork.bukkitcore.profile.ProfileRegistry
import io.github.slaxnetwork.mm
import net.kyori.adventure.text.minimessage.tag.resolver.TagResolver
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

    // TODO: 3/22/2023 Move these out of the KOTCRegistry and possibly to something in bukkit-core.
    fun broadcastLocalizedMessage(message: String, id: String) {
        for(kotcPlayer in players) {
            val bukkitPlayer = kotcPlayer.bukkitPlayer
                ?: continue

            val resolver = LanguageTags.translateText(id, kotcPlayer.profile)

            bukkitPlayer.sendMessage(mm.deserialize(message, resolver))
        }
    }

    fun broadcastLocalizedMessage(message: String, id: String, vararg tagResolvers: TagResolver) {
        for(kotcPlayer in players) {
            val bukkitPlayer = kotcPlayer.bukkitPlayer
                ?: continue

            val resolvers = TagResolver.resolver(
                LanguageTags.translateText(id, kotcPlayer.profile),
                *tagResolvers
            )

            bukkitPlayer.sendMessage(mm.deserialize(message, resolvers))
        }
    }

//    fun broadcastMessage(message: String, tagResolver: TagResolver) {
//        mm.deserialize("A", ProfileTags.coloredName("A"))
//    }
//    fun deserialize(input: String, vararg tagResolvers: TagResolver?): Component {
//        return deserialize(input, TagResolver.resolver(*tagResolvers))
//    }
}