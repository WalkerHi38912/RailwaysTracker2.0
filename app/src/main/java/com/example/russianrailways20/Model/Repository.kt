package com.example.russianrailways20.Model

import androidx.lifecycle.LiveData
import com.example.russianrailways20.Model.DB.DAO
import com.example.russianrailways20.Model.DB.PrevTripsEntity
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class Repository (private val TrainApi: TrainApiInterface, private val StationApi: StationApiInterface, private val Dao: DAO){

    // Работа с API
    private val _trainData = MutableStateFlow<TrainResponse?>(null)
    val trainData: StateFlow<TrainResponse?> = _trainData

    private val _stationsData = MutableStateFlow<StationResponse?>(null)
    val stationsData: StateFlow<StationResponse?> = _stationsData

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
        } catch (e: Exception) {
            // Обработка ошибок
        }
    }

    // Работа с DB
    suspend fun insertTrip(trip: PrevTripsEntity){
        Dao.insertItem(trip)
    }

    suspend fun deleteTrip(trip: PrevTripsEntity){
        Dao.deleteItem(trip)
    }

    fun getAllItems() : Flow<List<PrevTripsEntity>>{
        return Dao.getAllItems()
    }

    suspend fun countOfDBItems() : Int{
        return Dao.getItemCount()
    }

    suspend fun checkAndHandleMaxItems() {
        val count = Dao.getItemCount()
        if (count > 4) {
            Dao.deleteAllItems()
        }
    }

}
