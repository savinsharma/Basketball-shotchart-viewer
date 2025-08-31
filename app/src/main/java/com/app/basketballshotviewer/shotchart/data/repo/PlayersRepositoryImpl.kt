package com.app.basketballshotviewer.shotchart.data.repo

import com.app.basketballshotviewer.shotchart.data.datasource.PlayerDataSource
import com.app.basketballshotviewer.shotchart.domain.model.Player

class PlayersRepositoryImpl(
    private val playerDataSource: PlayerDataSource
) : PlayersRepository {
    override suspend fun getPlayers(teamId: String): List<Player> {
        return  playerDataSource.fetchPlayers(teamId)
    }
    override suspend fun getPlayer(playerId: String): Player? {
        return  playerDataSource.fetchPlayer(playerId)
    }
}
