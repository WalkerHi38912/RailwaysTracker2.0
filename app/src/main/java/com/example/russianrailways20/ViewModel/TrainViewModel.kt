package com.example.russianrailways20.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.StationResponse
import com.example.russianrailways20.Model.TrainResponse
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class TrainViewModel(private val repository: Repository) : ViewModel(){
    val trainData: StateFlow<TrainResponse?> = repository.trainData
    val stationsData: StateFlow<StationResponse?> = repository.stationsData
    val dataLoaded: StateFlow<Boolean> = repository.dataLoaded

    init {
        fetchStations()
    }

    private fun fetchStations(){
        viewModelScope.launch {
            repository.getStationsList()
        }
    }

    fun fetchTrainData(from: String, to: String, date: String){
        viewModelScope.launch {
            repository.getTrainSchedule(from = from, to = to, date = date)
        }
    }
}