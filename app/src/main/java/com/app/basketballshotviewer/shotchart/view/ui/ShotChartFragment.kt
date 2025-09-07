package com.app.basketballshotviewer.shotchart.view.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.basketballshotviewer.AppGraph
import com.app.basketballshotviewer.R
import com.app.basketballshotviewer.shotchart.render.GLCourtView


class ShotChartFragment : Fragment() {

    private val viewModel: ShotChartViewModel by viewModels {
        ShotChartVmFactory(AppGraph.observeLiveShots, AppGraph.dataSource)
    }

    private var glView: GLCourtView? = null
    private var lastX = 0f
    private var lastY = 0f

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        val root = inflater.inflate(R.layout.fragment_shot_chart, container, false)
        glView = root.findViewById(R.id.glCourtView)
        setupGestures()
        return root
    }

    override fun onResume() {
        super.onResume()
        glView?.onResumeSafe()
        viewModel.start("demo-001")
    }

    override fun onPause() {
        super.onPause()
        glView?.onPauseSafe()
        viewModel.stop()
    }

    private fun setupGestures() {
        glView?.setOnTouchListener { _, e ->
            when (e.actionMasked) {
                MotionEvent.ACTION_DOWN -> {
                    lastX = e.x; lastY = e.y; true
                }

                MotionEvent.ACTION_MOVE -> {
                    val dx = e.x - lastX;
                    val dy = e.y - lastY
                    glView?.onTouchDrag(dx, dy)
                    lastX = e.x; lastY = e.y
                    true
                }

                else -> false
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_shot_chart, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.action_demo_shot -> {
                viewModel.fireDemoShot()
                Toast.makeText(requireContext(), "Demo shot fired", Toast.LENGTH_SHORT).show()
                true
            }

            else -> super.onOptionsItemSelected(item)
        }
    }
}