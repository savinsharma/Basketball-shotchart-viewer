package com.app.basketballshotviewer.shotchart.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class GLCourtRenderer  : GLSurfaceView.Renderer  {
    private val camera = CameraController()

    override fun onDrawFrame(p0: GL10?) {
        val state: ShotRenderState = RendererHelper.consume()
        //TODO: draw court and animate the arc
    }
    override fun onSurfaceChanged(p0: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        camera.setValues(radius = 10f, azimuthDeg = 50f, elevationDeg = 15f)
        //TODO: changes on camera angles
    }
    override fun onSurfaceCreated(p0: GL10?, p1: EGLConfig?) {
        GLES30.glClearColor(0.05f, 0.07f, 0.10f, 1f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
    }
}