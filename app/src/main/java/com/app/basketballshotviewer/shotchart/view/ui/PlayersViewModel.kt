package com.app.basketballshotviewer.shotchart.view.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.basketballshotviewer.shotchart.data.repo.PlayersRepository
import com.app.basketballshotviewer.shotchart.domain.model.Player
import kotlinx.coroutines.launch

class PlayersViewModel(
    private val repo: PlayersRepository
) : ViewModel() {

    private val _players = MutableLiveData<List<Player>>()

    fun getTeam(teamId: String) {
        viewModelScope.launch {
            val list = repo.getPlayers(teamId)
            _players.value = list
        }
    }
}
