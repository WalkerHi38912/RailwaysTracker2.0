package com.example.russianrailways20.Model

import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Repository (private val TrainApi: TrainApiInterface, private val StationApi: StationApiInterface){
    private val _trainData = MutableStateFlow<TrainResponse?>(null)
    val trainData: StateFlow<TrainResponse?> = _trainData

    private val _stationsData = MutableStateFlow<StationResponse?>(null)
    val stationsData: StateFlow<StationResponse?> = _stationsData

    private val _dataLoaded = MutableStateFlow(false)
    val dataLoaded: StateFlow<Boolean> = _dataLoaded

    suspend fun getTrainSchedule(from: String, to: String, date: String) {
        try {
            val response = TrainApi.getTrainSchedule(from = from, to = to, dateOfReq = date)
            _trainData.value = response
        } catch (e: Exception) {
            // Обработка ошибок
        }
    }

    suspend fun getStationsList() {
        try {
            val response = StationApi.getStationsList()
            _stationsData.value = response
            _dataLoaded.value = true  // Устанавливаем флаг, что данные получены
        } catch (e: Exception) {
            // Обработка ошибок
        }
    }
}
