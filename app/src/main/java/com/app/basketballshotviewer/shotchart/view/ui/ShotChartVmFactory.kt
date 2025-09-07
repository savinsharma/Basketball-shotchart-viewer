package com.app.basketballshotviewer.shotchart.view.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.app.basketballshotviewer.shotchart.data.datasource.ShotDataSource
import com.app.basketballshotviewer.shotchart.domain.usecase.LiveShotsUseCase

class ShotChartVmFactory(
    private val observeLiveShots: LiveShotsUseCase,
    private val shotDataSource: ShotDataSource
) : ViewModelProvider.Factory {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(c: Class<T>): T {
        if (c.isAssignableFrom(ShotChartViewModel::class.java)) {
            return ShotChartViewModel(observeLiveShots, shotDataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class $c")
    }
}