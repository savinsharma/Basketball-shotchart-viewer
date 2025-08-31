package com.app.basketballshotviewer.shotchart.data.core


import com.app.basketballshotviewer.shotchart.domain.model.ShotDetails
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("api/path")
    suspend fun getLiveShots(@Query("gameId") gameId: String): List<ShotDetails>
}
