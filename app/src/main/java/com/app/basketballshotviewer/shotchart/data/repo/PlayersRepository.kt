package com.app.basketballshotviewer.shotchart.data.repo

import com.app.basketballshotviewer.shotchart.domain.model.Player

interface PlayersRepository {
    suspend fun getPlayers(teamId: String): List<Player>
    suspend fun getPlayer(playerId: String): Player?
}