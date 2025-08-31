package com.app.basketballshotviewer.shotchart.domain.usecase

import com.app.basketballshotviewer.shotchart.data.repo.ShotRepository
import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import kotlinx.coroutines.flow.Flow

class LiveShotsUseCase(private val repo: ShotRepository) {
    operator fun invoke(gameId: String): Flow<ShotDetails> = repo.observeLiveShots(gameId)
}
