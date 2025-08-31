package com.app.basketballshotviewer.shotchart.render

import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import java.util.concurrent.atomic.AtomicReference

object RendererHelper {
    private val stateRef = AtomicReference(ShotRenderState.EMPTY)
    internal fun consume(): ShotRenderState = stateRef.get()
    fun submit(state: ShotRenderState) {
        stateRef.set(state)
    }
}