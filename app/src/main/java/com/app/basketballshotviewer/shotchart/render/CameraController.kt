package com.app.basketballshotviewer.shotchart.render

/*
* To Handle all the camera related controller
*/
class CameraController() {
    private var r = 20f
    private var az = 30f // azimuthDeg, horizontal angle around the target point, measured in degrees.
    private var el = 15f

    fun setValues(radius: Float, azimuthDeg: Float, elevationDeg: Float) {
        r = radius
        az = azimuthDeg
        el = elevationDeg
    }

    fun zoomBy(value: Float) {

    }

    fun fanEyeView(): FloatArray {
        return FloatArray(10)
    }

}