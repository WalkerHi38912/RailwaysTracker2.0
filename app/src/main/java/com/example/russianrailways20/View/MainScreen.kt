package com.example.russianrailways20.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.russianrailways20.Model.DB.MainDB
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.StationApiInterface
import com.example.russianrailways20.Model.TrainApiInterface
import com.example.russianrailways20.Model.TrainViewModelFactory
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavController, trainViewModel: TrainViewModel){


    val trainData by trainViewModel.trainData.collectAsState()
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ){
        Image(painter = painterResource(R.drawable.back_ground), contentDescription = null, modifier = Modifier
            .fillMaxSize()
            .blur(10.dp), contentScale = ContentScale.FillBounds
        )
        Column {
            Spacer(Modifier.height(54.dp))
            MainMenu(trainViewModel, navController)
            Spacer(Modifier.height(8.dp))
            PreviousTravelsScreen(trainViewModel)
            Spacer(Modifier.height(8.dp))
            DisplayTrainInfo(trains = trainData)
        }
    }
}