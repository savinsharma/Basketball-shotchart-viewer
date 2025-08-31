package com.app.basketballshotviewer.shotchart.view.state

import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails

data class ShotRenderState(
    val pendingShots: List<ShotDetails> = emptyList()
) {
    companion object {
        val EMPTY = ShotRenderState()
    }
}