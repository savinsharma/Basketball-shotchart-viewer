package com.app.basketballshotviewer.shotchart.render

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet

/*
* Court view Surface
*/

class GLCourtView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {
    private val renderer = GLCourtRenderer()
    init {
        setEGLContextClientVersion(3)
        setRenderer(renderer)
        renderMode = RENDERMODE_CONTINUOUSLY
        preserveEGLContextOnPause = true
    }
    fun renderer(): GLCourtRenderer = renderer
}