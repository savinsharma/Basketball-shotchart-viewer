package com.app.basketballshotviewer.shotchart.render

import android.opengl.GLES30
import android.util.Log
import java.nio.*

object GLUtils {
    private const val TAG = "GLUtils"

    private fun compile(type: Int, src: String): Int {
        val id = GLES30.glCreateShader(type)
        GLES30.glShaderSource(id, src)
        GLES30.glCompileShader(id)
        val ok = IntArray(1)
        GLES30.glGetShaderiv(id, GLES30.GL_COMPILE_STATUS, ok, 0)
        if (ok[0] == 0) {
            GLES30.glDeleteShader(id)
            throw RuntimeException("Shader compile error")
        }
        return id
    }

    fun link(vs: String, fs: String, attribBindings: List<Pair<Int, String>> = emptyList()): Int {
        val v = compile(GLES30.GL_VERTEX_SHADER, vs)
        val f = compile(GLES30.GL_FRAGMENT_SHADER, fs)
        val p = GLES30.glCreateProgram()
        GLES30.glAttachShader(p, v)
        GLES30.glAttachShader(p, f)
        for ((loc, name) in attribBindings) GLES30.glBindAttribLocation(p, loc, name)
        GLES30.glLinkProgram(p)
        val ok = IntArray(1); GLES30.glGetProgramiv(p, GLES30.GL_LINK_STATUS, ok, 0)
        GLES30.glDeleteShader(v); GLES30.glDeleteShader(f)
        if (ok[0] == 0) {
            GLES30.glDeleteProgram(p)
            throw RuntimeException("Program link error")
        }
        return p
    }

    fun floatBuf(a: FloatArray): FloatBuffer =
        ByteBuffer.allocateDirect(a.size * 4).order(ByteOrder.nativeOrder()).asFloatBuffer()
            .apply { put(a).position(0) }

    fun intBuf(a: IntArray): IntBuffer =
        ByteBuffer.allocateDirect(a.size * 4).order(ByteOrder.nativeOrder()).asIntBuffer()
            .apply { put(a).position(0) }

    fun uploadDynamicVbo(data: FloatArray): Int {
        val vbos = IntArray(1)
        GLES30.glGenBuffers(1, vbos, 0)
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, vbos[0])
        GLES30.glBufferData(
            GLES30.GL_ARRAY_BUFFER,
            data.size * 4,
            floatBuf(data),
            GLES30.GL_DYNAMIC_DRAW
        )
        GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        return vbos[0]
    }

    fun deleteBuffer(id: Int) {
        if (id != 0) {
            val tmp = intArrayOf(id)
            GLES30.glDeleteBuffers(1, tmp, 0)
        }
    }

    fun enablePosColor(posLoc: Int, colLoc: Int, strideFloats: Int = 7) {
        val strideBytes = strideFloats * 4
        GLES30.glEnableVertexAttribArray(posLoc)
        GLES30.glVertexAttribPointer(posLoc, 3, GLES30.GL_FLOAT, false, strideBytes, 0)
        GLES30.glEnableVertexAttribArray(colLoc)
        GLES30.glVertexAttribPointer(colLoc, 4, GLES30.GL_FLOAT, false, strideBytes, 3 * 4)
    }

    fun disablePosColor(posLoc: Int, colLoc: Int) {
        GLES30.glDisableVertexAttribArray(posLoc)
        GLES30.glDisableVertexAttribArray(colLoc)
    }
}
