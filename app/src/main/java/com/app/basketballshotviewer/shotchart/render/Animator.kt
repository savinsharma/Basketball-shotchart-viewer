package com.app.basketballshotviewer.shotchart.render

import android.opengl.GLES30
import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import com.app.basketballshotviewer.shotchart.domain.model.ShotOutcome
import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import kotlin.math.cos
import kotlin.math.hypot
import kotlin.math.max
import kotlin.math.min
import kotlin.math.sin

/**
 * Animator – converts ShotDetails into a parametric arc that
 * flies from shooter (x0,z0) toward the correct rim, with a nice apex.
 *
 * Court coordinate system:
 *   X: 0 → 28.65 (width), Z: 0 → 15.24 (depth), Y up.
 * Rims (centers): LEFT  (0f,    7.62f), RIGHT (28.65f, 7.62f)
 */
class Animator(
    private val maxArcs: Int = 64,
    private val stepsPerArc: Int = 64,
    private val msPerSegment: Long = 16L,
    private val holdMsAfterDraw: Long = 900L
) {
    private val arcs = ArrayList<Trajectory>(maxArcs)
    private var program: ShaderProgram? = null

    fun syncWith(state: ShotRenderState) {
        if (state.pendingShots.isEmpty()) return
        state.pendingShots.forEach { add(it) }
    }

    fun draw(viewProj: FloatArray, nowNs: Long) {
        if (arcs.isEmpty()) return
        val prog = program ?: Shaders.unlitVertexColor().also { program = it }

        // Make arcs pop and never Z-fight with the court
        GLES30.glLineWidth(3f)
        val depthState = IntArray(1).also { GLES30.glGetIntegerv(GLES30.GL_DEPTH_TEST, it, 0) }
        GLES30.glDisable(GLES30.GL_DEPTH_TEST)

        GLES30.glUseProgram(prog.id)
        GLES30.glUniformMatrix4fv(prog.uMvp, 1, false, viewProj, 0)

        for (i in arcs.indices.reversed()) {
            val t = arcs[i]
            val alpha = t.alpha(nowNs)
            if (alpha <= 0f) {
                t.dispose(); arcs.removeAt(i); continue
            }

            GLES30.glUniform1f(prog.uAlpha, alpha)
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, t.vbo)
            GLUtils.enablePosColor(prog.aPosLoc, prog.aColLoc, STRIDE)
            val count = t.visibleVertexCount(nowNs)
            if (count > 1) GLES30.glDrawArrays(GLES30.GL_LINE_STRIP, 0, count)
            GLUtils.disablePosColor(prog.aPosLoc, prog.aColLoc)
            GLES30.glBindBuffer(GLES30.GL_ARRAY_BUFFER, 0)
        }

        if (depthState[0] == 1) GLES30.glEnable(GLES30.GL_DEPTH_TEST) else GLES30.glDisable(GLES30.GL_DEPTH_TEST)
    }

    private fun add(s: ShotDetails) {
        if (arcs.size >= maxArcs) arcs.removeAt(0).dispose()
        arcs.add(Trajectory.fromShot(s, stepsPerArc, msPerSegment, holdMsAfterDraw))
    }

    private class Trajectory(
        val vbo: Int,
        private val vertexCount: Int,
        private val createdNs: Long,
        private val segmentNs: Long,
        private val holdNs: Long
    ) {
        fun visibleVertexCount(nowNs: Long): Int {
            val elapsed = (nowNs - createdNs).coerceAtLeast(0)
            val seg = (elapsed / segmentNs).toInt()
            return min(vertexCount, seg + 1).coerceAtLeast(1)
        }

        fun alpha(nowNs: Long): Float {
            val grow = segmentNs * vertexCount.toLong()
            val elapsed = (nowNs - createdNs).coerceAtLeast(0)
            return when {
                elapsed <= grow -> 1f
                elapsed <= grow + holdNs -> 1f - ((elapsed - grow).toFloat() / holdNs.toFloat()).coerceIn(
                    0f,
                    1f
                )

                else -> 0f
            }
        }

        fun dispose() = GLUtils.deleteBuffer(vbo)

        companion object {
            private const val STRIDE = 7 // xyz rgba

            // Court constants
            private const val COURT_W = 28.65f
            private const val COURT_D = 15.24f
            private val LEFT_RIM = floatArrayOf(0f, 7.62f)
            private val RIGHT_RIM = floatArrayOf(COURT_W, 7.62f)
            private const val RIM_HEIGHT = 3.048f // 10 ft in meters
            private const val ARC_HEAD_ALPHA = 1.0f
            private const val ARC_TAIL_ALPHA = 0.35f

            /**
             * Build a nice-looking parametric arc:
             *   Horizontal path: linear from (x0,z0) -> rim
             *   Vertical: quadratic y(t) with apex above both release & rim
             */
            fun fromShot(
                s: ShotDetails,
                steps: Int,
                msPerSegment: Long,
                holdMs: Long
            ): Trajectory {
                // Clamp start inside court
                val x0 = s.xAxisMeters.coerceIn(0f, COURT_W)
                val z0 = s.yAxisMeters.coerceIn(0f, COURT_D)
                val y0 = max(0.0f, s.zAxisMeters)

                // Choose rim by side (nearer rim)
                val rim = if (x0 < COURT_W * 0.5f) LEFT_RIM else RIGHT_RIM
                val x1 = rim[0]
                val z1 = rim[1]
                val y1 = RIM_HEIGHT

                // Horizontal distance and direction
                val dx = x1 - x0
                val dz = z1 - z0
                val horizDist = hypot(dx.toDouble(), dz.toDouble()).toFloat().coerceAtLeast(0.001f)
                val dirX = dx / horizDist
                val dirZ = dz / horizDist

                // Travel length (slightly overshoot rim so the arc doesn’t “stop” at front iron)
                val travel = (horizDist * 1.05f).coerceIn(6f, COURT_W) // keep sane

                // Apex height (higher of release + margin or rim + margin)
                val apex = max(max(y0, y1) + 0.9f, 3.2f) // ~3.2m baseline apex

                // Build vertices along t in [0..1]
                val verts = FloatArray(steps * STRIDE)
                var t = 0f
                val dt = 1f / (steps - 1).coerceAtLeast(1)

                val made = (s.outcome == ShotOutcome.MADE)
                val (rMade, gMade, bMade) = floatArrayOf(0f, 1f, 0f) // green
                val (rMiss, gMiss, bMiss) = floatArrayOf(1f, 0f, 0f) // red

                // Quadratic vertical curve y(t) using apex control.
                // y(t) = (1 - u) * y0 + u * y1 + h * 4 * u * (1 - u), u in [0..1]
                // where h = (apex - ((y0 + y1) * 0.5f))
                val h = apex - ((y0 + y1) * 0.5f)

                for (i in 0 until steps) {
                    val u = t // normalized progress along path
                    val px = x0 + dirX * (travel * u)
                    val pz = z0 + dirZ * (travel * u)

                    val baseLine = (1f - u) * y0 + u * y1
                    val py = baseLine + h * 4f * u * (1f - u)

                    val along = i / (steps - 1f).coerceAtLeast(1f)
                    val a = ARC_TAIL_ALPHA + (ARC_HEAD_ALPHA - ARC_TAIL_ALPHA) * (1f - along)
                    val (r, g, b) = if (made) Triple(rMade, gMade, bMade) else Triple(
                        rMiss,
                        gMiss,
                        bMiss
                    )

                    val base = i * STRIDE
                    verts[base + 0] = px.coerceIn(0f, COURT_W)
                    verts[base + 1] = py
                    verts[base + 2] = pz.coerceIn(0f, COURT_D)
                    verts[base + 3] = r
                    verts[base + 4] = g
                    verts[base + 5] = b
                    verts[base + 6] = a

                    t = (t + dt).coerceAtMost(1f)
                }

                val vbo = GLUtils.uploadDynamicVbo(verts)
                val segNs = msPerSegment * 1_000_000L
                val holdNs = holdMs * 1_000_000L
                return Trajectory(vbo, steps, System.nanoTime(), segNs, holdNs)
            }
        }
    }

    companion object {
        private const val STRIDE = 7
    }
}