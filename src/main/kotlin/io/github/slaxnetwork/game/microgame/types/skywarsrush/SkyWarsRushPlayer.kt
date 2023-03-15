package io.github.slaxnetwork.game.microgame.types.skywarsrush

import io.github.slaxnetwork.game.microgame.player.MicroGamePlayer
import io.github.slaxnetwork.game.microgame.team.KOTCTeam
import io.github.slaxnetwork.player.KOTCPlayer

class SkyWarsRushPlayer(
    kotcPlayer: KOTCPlayer,
    var team: KOTCTeam
): MicroGamePlayer(kotcPlayer)