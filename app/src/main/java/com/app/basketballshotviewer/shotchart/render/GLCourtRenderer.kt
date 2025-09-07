package com.app.basketballshotviewer.shotchart.render

import android.opengl.GLES30
import android.opengl.GLSurfaceView
import android.opengl.Matrix
import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10
import kotlin.random.Random

class GLCourtRenderer : GLSurfaceView.Renderer {
    private val projection = FloatArray(16)
    private val view = FloatArray(16)
    private val viewProj = FloatArray(16)
    private val camera = CameraController()

    private lateinit var court: CourtView
    private lateinit var animator: Animator

    override fun onSurfaceCreated(gl: GL10?, config: EGLConfig?) {
        GLES30.glClearColor(0.05f, 0.07f, 0.10f, 1f)
        GLES30.glEnable(GLES30.GL_DEPTH_TEST)
        GLES30.glEnable(GLES30.GL_BLEND)
        GLES30.glBlendFunc(GLES30.GL_SRC_ALPHA, GLES30.GL_ONE_MINUS_SRC_ALPHA)

        camera.setTarget(14.325f, 0f, 7.62f)
        court = CourtView()
        animator = Animator()

        RendererHelper.submit(
            ShotRenderState(
                pendingShots = listOf(
                    com.app.basketballshotviewer.shotchart.domain.model.ShotDetails(
                        sequenceId = 1L,
                        timeStamp = System.currentTimeMillis(),
                        playerId = Random.nextLong().toString(),
                        teamId = "team123",
                        xAxisMeters = 7.0f,
                        yAxisMeters = 5.0f,
                        zAxisMeters = 2.2f,
                        velocity = 8.5f,
                        outcome = com.app.basketballshotviewer.shotchart.domain.model.ShotOutcome.MADE
                    )
                )
            )
        )
    }

    override fun onSurfaceChanged(gl: GL10?, width: Int, height: Int) {
        GLES30.glViewport(0, 0, width, height)
        val aspect = width.toFloat() / height.toFloat()
        Matrix.perspectiveM(projection, 0, 45f, aspect, 0.1f, 150f)
        camera.setOrbit(radius = 22f, azimuthDeg = 35f, elevationDeg = 20f)
    }

    override fun onDrawFrame(gl: GL10?) {
        GLES30.glClear(GLES30.GL_COLOR_BUFFER_BIT or GLES30.GL_DEPTH_BUFFER_BIT)

        camera.tickInertia(1f / 60f)
        val eye = camera.eye();
        val ctr = camera.center();
        val up = camera.up()
        Matrix.setLookAtM(
            view,
            0,
            eye[0],
            eye[1],
            eye[2],
            ctr[0],
            ctr[1],
            ctr[2],
            up[0],
            up[1],
            up[2]
        )
        Matrix.multiplyMM(viewProj, 0, projection, 0, view, 0)

        court.draw(viewProj, 1f)

        val state: ShotRenderState = RendererHelper.consume()
        animator.syncWith(state)
        animator.draw(viewProj, System.nanoTime())
    }

    fun onTouchDrag(dx: Float, dy: Float) = camera.orbitBy(dx, dy)
    fun onPinch(scale: Float) = camera.zoomBy(scale)
}