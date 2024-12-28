package com.example.russianrailways20.ViewModel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.russianrailways20.Model.DB.PrevTripsEntity
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.StationResponse
import com.example.russianrailways20.Model.TrainResponse
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

class TrainViewModel(private val repository: Repository) : ViewModel(){
    //Api
    val trainData: StateFlow<TrainResponse?> = repository.trainData
    val stationsData: StateFlow<StationResponse?> = repository.stationsData
    //DB
    private val _savedTrips = MutableStateFlow<List<PrevTripsEntity>>(emptyList())
    val savedTrips: StateFlow<List<PrevTripsEntity>> = _savedTrips

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> = _loadingState

    private val _errorState = MutableStateFlow<String?>(null)
    val errorState: StateFlow<String?> = _errorState

    //Data
    private val _textFrom = MutableStateFlow("")
    val textFrom: StateFlow<String> = _textFrom

    private val _textTo = MutableStateFlow("")
    val textTo: StateFlow<String> = _textTo

    private val _codeFrom = MutableStateFlow("")
    val codeFrom: StateFlow<String> = _codeFrom

    private val _codeTo = MutableStateFlow("")
    val codeTo: StateFlow<String> = _codeTo

    fun updateTextFrom(value: String) {
        _textFrom.value = value
    }

    fun updateTextTo(value: String) {
        _textTo.value = value
    }

    fun updateCodeFrom(value: String) {
        _codeFrom.value = value
    }

    fun updateCodeTo(value: String) {
        _codeTo.value = value
    }

    fun swapData(){
        var tmp = _textFrom.value
        _textFrom.value = _textTo.value
        _textTo.value = tmp
        tmp = _codeFrom.value
        _codeFrom.value = _codeTo.value
        _codeTo.value = tmp
    }

    init {
        fetchStations()
        fetchSavedTrips()
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

    private fun fetchSavedTrips() {
        viewModelScope.launch {
            repository.getAllItems()
                .catch { e -> _errorState.value = e.message }
                .collect { trips ->
                    _savedTrips.value = trips
                }
        }
    }

    fun saveTrip(trip: PrevTripsEntity) {
        viewModelScope.launch {
            try {
                repository.insertTrip(trip)
                repository.checkAndHandleMaxItems(trip)
                fetchSavedTrips()
            } catch (e: Exception) {
                _errorState.value = e.message
            }
        }
    }

}