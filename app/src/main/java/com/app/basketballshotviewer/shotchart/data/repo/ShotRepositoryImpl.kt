package com.app.basketballshotviewer.shotchart.data.repo

import com.app.basketballshotviewer.shotchart.data.datasource.ShotDataSource
import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class ShotRepositoryImpl(
    private val shotDataSource: ShotDataSource
) : ShotRepository {
    override fun observeLiveShots(gameId: String): Flow<ShotDetails> = shotDataSource.flow(gameId)
    override fun replayShots(gameId: String, fromMs: Long, toMs: Long): Flow<ShotDetails> = shotDataSource.flow(gameId)
    override fun sendDemoShotData(timeStamp: Long, sequenceId: Long, playerId: String) {
        GlobalScope.launch {
            shotDataSource.sendDemoShotData(timeStamp, sequenceId, playerId);
        }
    }
}
