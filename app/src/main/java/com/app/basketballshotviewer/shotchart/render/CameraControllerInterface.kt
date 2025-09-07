package com.app.basketballshotviewer.shotchart.render

interface CameraControllerInterface {
    /** Set world-space target to orbit around. */
    fun setTarget(x: Float, y: Float, z: Float)

    /** Hard-set spherical orbit (degrees for angles). */
    fun setOrbit(radius: Float, azimuthDeg: Float, elevationDeg: Float)

    /** Drag gesture deltas (in pixels) to orbit camera. */
    fun orbitBy(dxPx: Float, dyPx: Float)

    /** Pinch scale (>1 zoom in, <1 zoom out). */
    fun zoomBy(scaleFactor: Float)

    /** Optional: call once per frame with dt seconds to apply inertia/damping. */
    fun tickInertia(dtSec: Float)

    /** LookAt vectors for building the view matrix. */
    fun eye(): FloatArray
    fun center(): FloatArray
    fun up(): FloatArray

    /** Optional presets (e.g., seat positions). */
    fun snapTo(azimuthDeg: Float, elevationDeg: Float, radius: Float)
}