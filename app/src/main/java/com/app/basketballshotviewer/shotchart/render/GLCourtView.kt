package com.app.basketballshotviewer.shotchart.render

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import android.view.View


class GLCourtView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null
) : GLSurfaceView(context, attrs) {

    private var rendererImpl: GLCourtRenderer? = null
    private var rendererSet = false

    // Lifecycle buffering flags
    private var pendingResume = false
    private var pendingPause = false

    init {
        setEGLContextClientVersion(3)
        preserveEGLContextOnPause = true
    }

    override fun onAttachedToWindow() {
        super.onAttachedToWindow()
        if (!rendererSet) {
            rendererImpl = GLCourtRenderer()
            setRenderer(rendererImpl)
            renderMode = RENDERMODE_CONTINUOUSLY
            rendererSet = true
        }
        if (pendingResume) {
            super.onResume()
            pendingResume = false
        } else if (pendingPause) {
            super.onPause()
            pendingPause = false
        }
    }

    override fun onDetachedFromWindow() {
        if (rendererSet) {
            try {
                super.onPause()
            } catch (_: Throwable) {
            }
        }
        rendererImpl = null
        rendererSet = false
        super.onDetachedFromWindow()
    }

    fun onResumeSafe() {
        if (rendererSet) {
            super.onResume()
        } else {
            pendingResume = true
            pendingPause = false
            if (!isAttachedToWindow) addOnAttachStateChangeListener(object :
                OnAttachStateChangeListener {
                override fun onViewAttachedToWindow(v: View) {
                    removeOnAttachStateChangeListener(this)
                    onAttachedToWindow()
                }

                override fun onViewDetachedFromWindow(v: View) {}
            })
        }
    }

    fun onPauseSafe() {
        if (rendererSet) {
            super.onPause()
        } else {
            pendingPause = true
            pendingResume = false
        }
    }

    fun onTouchDrag(dx: Float, dy: Float) {
        rendererImpl?.onTouchDrag(dx, dy)
    }
}