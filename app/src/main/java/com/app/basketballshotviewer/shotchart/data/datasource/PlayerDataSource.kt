package com.app.basketballshotviewer.shotchart.data.datasource

import com.app.basketballshotviewer.shotchart.domain.model.Player

class PlayerDataSource {

    private val players = mutableListOf<Player>()

    suspend fun fetchPlayers(teamId: String): List<Player> {
        return players.filter { it.teamId == teamId }
    }

    suspend fun fetchPlayer(playerId: String): Player? {
        return players.firstOrNull { it.playerId == playerId }
    }
}