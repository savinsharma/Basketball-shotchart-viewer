package com.app.basketballshotviewer.home.view

import android.os.Bundle
import android.view.ViewGroup
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.viewinterop.AndroidView
import androidx.fragment.app.FragmentActivity
import androidx.fragment.app.FragmentContainerView
import androidx.fragment.app.commit
import com.app.basketballshotviewer.ui.theme.BasketballShotViewerTheme

class HomeView : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            BasketballShotViewerTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    ShotChartFragmentHost()
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    BasketballShotViewerTheme {
        Greeting("Android")
    }
}

@
@Composable
private fun ShotChartFragmentHost() {
    val activity = LocalContext.current as FragmentActivity

    AndroidView(
        factory = { ctx ->
            // Create a FragmentContainerView to host the fragment
            val container = FragmentContainerView(ctx).apply {
                id = ViewGroup.generateViewId()
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
            }

            // Attach the fragment once
            val tag = "shotchart"
            if (activity.supportFragmentManager.findFragmentByTag(tag) == null) {
                activity.supportFragmentManager.commit {
                    setReorderingAllowed(true)
                    replace(container.id, ShotChartFragment.newInstance(), tag)
                }
            }
            container
        }
    )
}