package com.example.russianrailways20.View

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.example.russianrailways20.ViewModel.TrainViewModel
import com.example.russianrailways20.ui.theme.RussianRailways20Theme

class MainActivity : ComponentActivity() {
    private lateinit var trainViewModel: TrainViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RussianRailways20Theme {

            }
        }
    }
}