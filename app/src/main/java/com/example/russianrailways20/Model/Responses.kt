package com.example.russianrailways20.Model


//Station Response model
data class StationResponse(
    val stations: List<StationInfo?>
)

data class StationInfo(
    val title: String,
    val code: String
)
//


//Train Response model
data class TrainResponse(
    val search: Search,
    val segments: List<Segment>
)

data class Search(
    val from: Station,
    val to: Station,
    val data: String
)

data class Station(
    val type: String,
    val title: String
)

data class Segment(
    val thread: Thread,
    val stops: String,
    val duration: Double,
    val departure: String,
    val arrival: String,
    val start_date: String
)

data class Thread(
    val number: String,
    val title: String
)
//