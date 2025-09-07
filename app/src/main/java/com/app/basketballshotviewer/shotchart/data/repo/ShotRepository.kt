package com.app.basketballshotviewer.shotchart.data.repo

import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import kotlinx.coroutines.flow.Flow

interface ShotRepository {
    fun observeLiveShots(gameId: String): Flow<ShotDetails>
}