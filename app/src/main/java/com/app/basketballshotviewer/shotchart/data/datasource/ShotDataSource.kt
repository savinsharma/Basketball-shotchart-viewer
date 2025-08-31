package com.app.basketballshotviewer.shotchart.data.datasource

import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import com.app.basketballshotviewer.shotchart.domain.model.ShotOutcome
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.receiveAsFlow

class ShotDataSource{

    private val channel = Channel<ShotDetails>(capacity = 256)

    fun flow(gameId: String): Flow<ShotDetails> = channel.receiveAsFlow()

    suspend fun sendDemoShotData(timeStamp: Long, sequenceId: Long, playerId: String) {
        channel.send(
            ShotDetails(
                sequenceId = sequenceId,
                timeStamp = timeStamp,
                playerId = playerId,
                teamId = "xyz123",
                xAxisMeters = 6.5f,
                yAxisMeters = 6.5f,
                zAxisMeters = 12f,
                velocity = 15f,
                outcome = ShotOutcome.MADE
            )
        )
    }
}