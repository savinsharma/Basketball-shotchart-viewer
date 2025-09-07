package com.app.basketballshotviewer.shotchart.render

import android.util.DisplayMetrics
import kotlin.math.cos
import kotlin.math.abs
import kotlin.math.sin

/*
* To Handle all the camera related controller
 * CameraOrbitController
 * - Orbits a target point using [radius, azimuthDeg, elevationDeg].
 * - DPI-aware drag sensitivity, pinch zoom, and inertial damping.
 * - Safe bounds for elevation/radius to avoid flipping the camera.
 *
 * World coordinate system (as used in your renderer):
 * - X: left->right along the court length (0..28.65m)
 * - Z: near->far across the width (0..15.24m)
 * - Y: up
*/
class CameraController(
    private val minRadius: Float = 8f,
    private val maxRadius: Float = 40f,
    private val minElevationDeg: Float = -10f,
    private val maxElevationDeg: Float = 80f
) : CameraControllerInterface {

    private var radius = 20f
    private var azDeg = 30f
    private var elDeg = 15f
    private val center = floatArrayOf(14.325f, 0f, 7.62f)

    private var velAz = 0f
    private var velEl = 0f
    private var velRad = 0f
    var damping = 0.90f

    override fun setTarget(x: Float, y: Float, z: Float) {
        center[0] = x
        center[1] = y
        center[2] = z
    }

    override fun setOrbit(radius: Float, azimuthDeg: Float, elevationDeg: Float) {
        this.radius = radius.coerceIn(minRadius, maxRadius)
        this.azDeg = wrapDeg(azimuthDeg)
        this.elDeg = elevationDeg.coerceIn(minElevationDeg, maxElevationDeg)
        velAz = 0f
        velEl = 0f
        velRad = 0f
    }

    override fun orbitBy(dx: Float, dy: Float) {
        val s = 0.18f
        val dAz = dx * s
        val dEl = dy * s
        azDeg = wrapDeg(azDeg + dAz)
        elDeg = (elDeg + dEl).coerceIn(minElevationDeg, maxElevationDeg)
        velAz = dAz; velEl = dEl
    }

    override fun zoomBy(scale: Float) {
        val desired = radius / scale
        val delta = (desired - radius) * 0.85f
        radius = (radius + delta).coerceIn(minRadius, maxRadius)
        velRad = delta
    }

    override fun tickInertia(dt: Float) {
        if (abs(velAz) < 1e-3f && abs(velEl) < 1e-3f && abs(velRad) < 1e-3f) return
        azDeg = wrapDeg(azDeg + velAz * dt * 60f)

        elDeg = (elDeg + velEl * dt * 60f).coerceIn(minElevationDeg, maxElevationDeg)
        radius = (radius + velRad * dt * 60f).coerceIn(minRadius, maxRadius)

        velAz *= damping
        velEl *= damping
        velRad *= damping
    }

    override fun eye(): FloatArray {
        val az = Math.toRadians(azDeg.toDouble())
        val el = Math.toRadians(elDeg.toDouble())
        val ex = (center[0] + radius * cos(el) * cos(az)).toFloat()
        val ez = (center[2] + radius * cos(el) * sin(az)).toFloat()
        val ey = (center[1] + radius * sin(el)).toFloat()
        return floatArrayOf(ex, ey, ez)
    }

    override fun center(): FloatArray = center.copyOf()

    override fun up(): FloatArray = floatArrayOf(0f, 1f, 0f)

    override fun snapTo(azimuthDeg: Float, elevationDeg: Float, radius: Float) {
        setOrbit(radius, azimuthDeg, elevationDeg)
    }

    private fun wrapDeg(d: Float): Float {
        var o = d
        while (o < 0f)
            o += 360f
        while (o >= 360f)
            o -= 360f
        return o
    }
}