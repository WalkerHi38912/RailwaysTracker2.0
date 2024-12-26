package com.example.russianrailways20.Model.DB

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class PrevTripsEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int? = null,
    val stationFromName: String,
    val stationFromCode: String,
    val stationToName: String,
    val stationToCode: String,
)
