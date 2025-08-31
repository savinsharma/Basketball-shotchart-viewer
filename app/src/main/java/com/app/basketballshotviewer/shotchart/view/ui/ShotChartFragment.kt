import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.app.basketballshotviewer.shotchart.render.GLCourtView

class ShotChartFragment : Fragment() {
    private val viewModel: ShotChartViewModel by viewModels()
    private lateinit var glView: GLCourtView

    companion object {
        fun newInstance() = ShotChartFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, s: Bundle?): View {
        glView = GLCourtView(requireContext())

        return glView
    }
    override fun onResume() {
        super.onResume()
        glView.onResume()
        viewModel.start(gameId = "dummy-001")
    }
    override fun onPause() {
        super.onPause()
        glView.onPause()
    }
}
