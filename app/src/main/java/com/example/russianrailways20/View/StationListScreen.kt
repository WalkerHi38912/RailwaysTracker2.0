package com.example.russianrailways20.View

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.russianrailways20.Model.DB.MainDB
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.StationApiInterface
import com.example.russianrailways20.Model.TrainApiInterface
import com.example.russianrailways20.Model.TrainViewModelFactory
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel

@Composable
fun StationsListScreen(navController: NavController, field: String) {
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
    val stationResponse by trainViewModel.stationsData.collectAsState()

    var searchQuery by remember { mutableStateOf("") }
    val filteredStations = stationResponse?.stations
        ?.filterNotNull()
        ?.filter { station ->
            station.title.contains(searchQuery, ignoreCase = true)
        } ?: emptyList()

    Box(
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(R.drawable.back_ground),
            contentDescription = null,
            modifier = Modifier
                .fillMaxSize()
                .blur(10.dp),
            contentScale = ContentScale.FillBounds
        )
        if (stationResponse == null) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            Column {
                Spacer(Modifier.height(54.dp))
                TextField(
                    value = searchQuery,
                    onValueChange = { searchQuery = it },
                    label = { Text("Поиск", fontWeight = FontWeight.Thin, fontSize = 20.sp) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .background(color = Color.Transparent)
                        .border(
                            width = 1.dp,
                            color = Color.White,
                            shape = RoundedCornerShape(15.dp)
                        ),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = Color.Transparent,
                        unfocusedContainerColor = Color.Transparent,
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedLabelColor = Color.White,
                        unfocusedLabelColor = Color.White,
                        focusedTextColor = Color.White,
                        unfocusedTextColor = Color.LightGray
                    )
                )
                LazyColumn {
                    items(filteredStations) { station ->
                        Text(
                            text = station.title,
                            color = Color.White,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 16.sp,
                            style = TextStyle(textDecoration = TextDecoration.Underline),
                            modifier = Modifier
                                .padding(4.dp)
                                .clip(RoundedCornerShape(15.dp))
                                .fillMaxWidth()
                                .background(color = Color.Transparent.copy(alpha = 0.4f))
                                .clickable {
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "selectedStationTitle_$field",
                                        station.title
                                    )
                                    navController.previousBackStackEntry?.savedStateHandle?.set(
                                        "selectedStationCode_$field",
                                        station.code
                                    )
                                    navController.popBackStack()
                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}