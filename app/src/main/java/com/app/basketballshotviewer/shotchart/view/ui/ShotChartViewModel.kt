package com.app.basketballshotviewer.shotchart.view.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.basketballshotviewer.shotchart.data.datasource.ShotDataSource
import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import com.app.basketballshotviewer.shotchart.domain.usecase.LiveShotsUseCase
import com.app.basketballshotviewer.shotchart.render.RendererHelper
import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import kotlinx.coroutines.Job
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.random.Random


class ShotChartViewModel(
    private val observeLiveShots: LiveShotsUseCase,
    private val dataSource: ShotDataSource
) : ViewModel() {

    private val buffer = Channel<ShotDetails>(256)
    private var streamJob: Job? = null
    private var frameJob: Job? = null

    fun start(gameId: String) {
        if (streamJob?.isActive == true) return
        streamJob = viewModelScope.launch {
            observeLiveShots(gameId).collect { buffer.trySend(it) }
        }
        frameJob = viewModelScope.launch {
            val shotList = ArrayList<ShotDetails>(64)
            while (isActive) {
                shotList.clear()
                val first = buffer.receiveCatching().getOrNull()
                if (first != null) {
                    shotList += first
                    // Non-blocking drain of extras (burst safe)
                    var e = buffer.tryReceive().getOrNull()
                    while (e != null) {
                        shotList += e; e = buffer.tryReceive().getOrNull()
                    }
                }
                if (shotList.isNotEmpty()) {
                    println("Drain -- $shotList")
                    RendererHelper.submit(ShotRenderState(shotList.toList()))
                }
                delay(8)
            }
        }
    }

    fun stop() {
        streamJob?.cancel(); frameJob?.cancel()
    }

    fun fireDemoShot() {
        viewModelScope.launch {
            dataSource.sendDemoShotData(
                timeStamp = System.currentTimeMillis(),
                sequenceId = Random.nextLong(),
                playerId = Random.nextLong().toString()
            )
        }
    }
}