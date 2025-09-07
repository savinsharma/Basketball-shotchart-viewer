package com.app.basketballshotviewer.shotchart.data.datasource

import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import com.app.basketballshotviewer.shotchart.domain.model.ShotOutcome
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlin.random.Random

class ShotDataSource {
    private val _shots = MutableSharedFlow<ShotDetails>(
        replay = 0, extraBufferCapacity = 64, onBufferOverflow = BufferOverflow.DROP_OLDEST
    )

    val shots: Flow<ShotDetails> = _shots.asSharedFlow()

    fun observeLiveShots(gameId: String): Flow<ShotDetails> = shots

    suspend fun sendDemoShotData(timeStamp: Long, sequenceId: Long, playerId: String) {
        val made = Random.nextBoolean()
        _shots.emit(
            ShotDetails(
                sequenceId = sequenceId,
                timeStamp = timeStamp,
                playerId = playerId,
                teamId = "xyz123",
                xAxisMeters = 6.5f,
                yAxisMeters = 6.5f,
                zAxisMeters = 12f,
                velocity = 15f,
                outcome = if (made) ShotOutcome.MADE else ShotOutcome.MISSED,
            )
        )
    }
}