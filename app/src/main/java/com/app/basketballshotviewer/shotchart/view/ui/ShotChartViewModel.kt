import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.basketballshotviewer.shotchart.domain.usecase.LiveShotsUseCase
import com.app.basketballshotviewer.shotchart.render.RendererHelper
import com.app.basketballshotviewer.shotchart.view.state.ShotRenderState
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class ShotChartViewModel(
    private val observeLiveShots: LiveShotsUseCase
) : ViewModel() {
    fun start(gameId: String) {
        viewModelScope.launch {
            observeLiveShots(gameId).collectLatest { shot ->
                RendererHelper.submit(ShotRenderState(pendingShots = listOf(shot)))
            }
        }
    }
}
