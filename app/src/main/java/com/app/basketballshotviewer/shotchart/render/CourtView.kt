package com.app.basketballshotviewer.shotchart.render

import android.opengl.GLES30

class CourtView {
    private val program: ShaderProgram = Shaders.unlitVertexColor()
    private val vao = IntArray(1)
    private val vbo = IntArray(1)
    private val ebo = IntArray(1)
    private var indexCount = 0

    init {
        val width = 28.65f
        val depth = 15.24f
        val y = 0f
        val col = floatArrayOf(0.72f, 0.58f, 0.39f, 1f)
        fun v(x: Float, z: Float) = floatArrayOf(x, y, z, col[0], col[1], col[2], col[3])

        val verts = v(0f, 0f) + v(width, 0f) + v(width, depth) + v(0f, depth)
        val indices = intArrayOf(0,1,2, 0,2,3)
        indexCount = indices.size

        GLES30.glGenVertexArrays(1, vao, 0)
        GLES30.glGenBuffers(1, vbo, 0)
        GLES30.glGenBuffers(1, ebo, 0)

        GLES30.glBindVertexArray(vao[0])

        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbo[0])
        GLES30.glBufferData(GLES30.GL_ARRAY_BUFFER, verts.size*4, GLUtils.floatBuf(verts), GLES30.GL_STATIC_DRAW)

        GLES30.glBindBuffer(GLES30.GL_ELEMENT_ARRAY_BUFFER, ebo[0])
        GLES30.glBufferData(GLES30.GL_ELEMENT_ARRAY_BUFFER, indices.size*4, GLUtils.intBuf(indices), GLES30.GL_STATIC_DRAW)

        GLUtils.enablePosColor(program.aPosLoc, program.aColLoc, 7)

        GLES30.glBindVertexArray(0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
    }

    fun draw(viewProj: FloatArray, alpha: Float) {
        GLES30.glUseProgram(program.id)
        GLES30.glUniformMatrix4fv(program.uMvp, 1, false, viewProj, 0)
        GLES30.glUniform1f(program.uAlpha, alpha)
        GLES30.glBindVertexArray(vao[0])
        GLES30.glDrawElements(GLES30.GL_TRIANGLES, indexCount, GLES30.GL_UNSIGNED_INT, 0)
        GLES30.glBindVertexArray(0)
    }
}