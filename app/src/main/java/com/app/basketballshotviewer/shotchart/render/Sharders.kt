package com.app.basketballshotviewer.shotchart.render

import android.opengl.GLES30

data class ShaderProgram(
    val id: Int,
    val uMvp: Int,
    val uAlpha: Int,
    val aPosLoc: Int,
    val aColLoc: Int
)

object Shaders {
    fun unlitVertexColor(): ShaderProgram {
        val vs = """
            #version 300 es
            in vec3 aPos;
            in vec4 aColor;
            uniform mat4 uMvp;
            out vec4 vColor;
            void main(){
                vColor = aColor;
                gl_Position = uMvp * vec4(aPos, 1.0);
            }
        """.trimIndent()

        val fs = """
            #version 300 es
            precision mediump float;
            in vec4 vColor;
            uniform float uAlpha;
            out vec4 o;
            void main(){
                o = vec4(vColor.rgb, vColor.a * uAlpha);
            }
        """.trimIndent()

        val pid = GLUtils.link(vs, fs, attribBindings = listOf(0 to "aPos", 1 to "aColor"))
        val uMvp = GLES30.glGetUniformLocation(pid, "uMvp")
        val uAlpha = GLES30.glGetUniformLocation(pid, "uAlpha")
        return ShaderProgram(pid, uMvp, uAlpha, aPosLoc = 0, aColLoc = 1)
    }
}
