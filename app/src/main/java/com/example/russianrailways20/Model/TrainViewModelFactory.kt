package com.example.russianrailways20.Model

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.russianrailways20.ViewModel.TrainViewModel

class TrainViewModelFactory(private val repository: Repository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(TrainViewModel::class.java)){
            return TrainViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
