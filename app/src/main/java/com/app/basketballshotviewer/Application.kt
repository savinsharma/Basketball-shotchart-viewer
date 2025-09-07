package com.app.basketballshotviewer

import android.app.Application
import com.app.basketballshotviewer.shotchart.data.datasource.ShotDataSource
import com.app.basketballshotviewer.shotchart.data.repo.ShotRepository
import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import com.app.basketballshotviewer.shotchart.domain.usecase.LiveShotsUseCase
import kotlinx.coroutines.flow.Flow

class Application  : Application() {
    override fun onCreate() {
        super.onCreate()
        AppGraph.init()
    }
}

object AppGraph {
    lateinit var dataSource: ShotDataSource
    lateinit var shotRepository: ShotRepository
    lateinit var observeLiveShots: LiveShotsUseCase

    fun init() {
        dataSource = ShotDataSource()
        shotRepository = object : ShotRepository {
            override fun observeLiveShots(gameId: String): Flow<ShotDetails> = dataSource.observeLiveShots(gameId)
        }
        observeLiveShots = LiveShotsUseCase(shotRepository)
    }
}