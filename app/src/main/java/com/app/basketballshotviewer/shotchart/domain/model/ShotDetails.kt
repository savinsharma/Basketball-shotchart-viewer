package com.app.basketballshotviewer.shotchart.domain.model

enum class ShotOutcome { MADE, MISSED, FOUL, BLOCKED }

data class ShotDetails(
    val sequenceId: Long,
    val timeStamp: Long,
    val playerId: String,
    val teamId: String,
    val xAxisMeters: Float,
    val yAxisMeters: Float,
    val zAxisMeters: Float,
    val velocity: Float,
    val outcome: ShotOutcome
)