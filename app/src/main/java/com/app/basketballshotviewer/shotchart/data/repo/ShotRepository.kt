package com.app.basketballshotviewer.shotchart.data.repo

import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import kotlinx.coroutines.flow.Flow

interface ShotRepository {
    fun observeLiveShots(gameId: String): Flow<ShotDetails>
    fun replayShots(gameId: String, fromMs: Long, toMs: Long): Flow<ShotDetails>
    fun sendDemoShotData(nowMs: Long, seq: Long, playerId: String)
}