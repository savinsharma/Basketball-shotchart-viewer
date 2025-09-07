package com.app.basketballshotviewer.shotchart.render

import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import java.util.concurrent.atomic.AtomicReference

object RendererHelper {
    private val latest = AtomicReference(ShotRenderState())

    fun submit(state: ShotRenderState) {
        // Replace with latest snapshot; renderer consumes and clears
        latest.set(state)
    }

    fun consume(): ShotRenderState {
        return latest.getAndSet(ShotRenderState())
    }
}