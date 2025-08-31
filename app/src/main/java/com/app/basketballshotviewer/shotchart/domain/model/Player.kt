package com.app.basketballshotviewer.shotchart.domain.model

data class Player(
    val playerId: String,
    val teamId: String,
    val provider: String,
    val headshotUrl: String?,
    val personName: PlayerDetails?
)