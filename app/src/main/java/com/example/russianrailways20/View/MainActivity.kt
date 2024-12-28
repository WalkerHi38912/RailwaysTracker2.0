package com.example.russianrailways20.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.russianrailways20.Model.DB.MainDB
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.StationApiInterface
import com.example.russianrailways20.Model.TrainApiInterface
import com.example.russianrailways20.Model.TrainViewModelFactory
import com.example.russianrailways20.ViewModel.TrainViewModel


@ExperimentalMaterial3Api
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current

            val dataBase = MainDB.createDB(context)
            val dao = dataBase.dao

            val repository = Repository(
                TrainApi = TrainApiInterface.create(),
                StationApi = StationApiInterface.create(),
                Dao = dao
            )

            val factory = TrainViewModelFactory(repository)
            val trainViewModel: TrainViewModel = viewModel(factory = factory)

            NavigationGraph(trainViewModel)
        }
    }
}






