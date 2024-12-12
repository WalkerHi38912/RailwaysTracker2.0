package com.example.russianrailways20.View

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.viewModelFactory
import androidx.navigation.NavController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.russianrailways20.Model.Repository
import com.example.russianrailways20.Model.Segment
import com.example.russianrailways20.Model.StationApiInterface
import com.example.russianrailways20.Model.StationResponse
import com.example.russianrailways20.Model.TrainApiInterface
import com.example.russianrailways20.Model.TrainResponse
import com.example.russianrailways20.Model.TrainViewModelFactory
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel
import com.example.russianrailways20.ui.theme.RussianRailways20Theme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RussianRailways20Theme {
                NavigationGraph()
            }
        }
    }
}

@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") { MainScreen(navController) }
        composable( route = "stationList") { StationsListScreen(navController) }
    }
}

@Composable
fun MainScreen(navController: NavController){
    val repository = Repository(TrainApi = TrainApiInterface.create(), StationApi = StationApiInterface.create())
    val factory = TrainViewModelFactory(repository)
    val trainViewModel: TrainViewModel = viewModel(factory = factory)
    Box(
        modifier = Modifier
            .fillMaxSize()
    ){
        Image(painter = painterResource(R.drawable.back_ground), contentDescription = null, modifier = Modifier.fillMaxSize().blur(10.dp), contentScale = ContentScale.FillBounds)
        TrainScreen(trainViewModel, navController)
    }
}

@Composable
fun TrainScreen(trainViewModel: TrainViewModel, navController: NavController){
    val trainData by trainViewModel.trainData.collectAsState()
    val stationData by trainViewModel.stationsData.collectAsState()
    Column {
        Spacer(Modifier.height(54.dp))
        MainMenu(stationData, trainData, trainViewModel, navController)
        DisplayTrainInfo(trains = trainData)
    }
}


@Composable
fun MainMenu(
    stations: StationResponse?,
    trains: TrainResponse?,
    trainViewModel: TrainViewModel,
    navController: NavController
) {
    var textFrom by remember { mutableStateOf("") }
    var textTo by remember { mutableStateOf("") }
    var codeFrom by remember { mutableStateOf("") }
    var codeTo by remember { mutableStateOf("") }


    Box(
        Modifier
            .padding(8.dp)
            .clip(RoundedCornerShape(15.dp))
            .background(color = Color.Transparent.copy(alpha = 0.4f))
    ) {
        Column {
            Box {
                Column {
                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Spacer(Modifier.width(8.dp))
                        Image(
                            painter = painterResource(id = R.drawable.arrows_lzobnua03qcd),
                            contentDescription = "",
                            modifier = Modifier
                                .padding(8.dp)
                                .clickable {
                                    val tmp = textFrom
                                    textFrom = textTo
                                    textTo = tmp
                                }
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
                        ) {
                            Log.e("FirstTF", "FirstTF data: $textTo")
                            Text(
                                text = "Куда: $textTo",
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(16.dp)
                                    .clickable {
                                        navController.navigate("stationList")
                                    },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Thin
                            )

                            Log.e("SecondTF", "ScondTF data: $textFrom")
                            Text(
                                text = "Откуда: $textFrom",
                                modifier = Modifier
                                    .padding(top = 8.dp, bottom = 8.dp)
                                    .fillMaxWidth()
                                    .border(
                                        width = 1.dp,
                                        color = Color.White,
                                        shape = RoundedCornerShape(15.dp)
                                    )
                                    .padding(16.dp)
                                    .clickable {
                                        navController.navigate("stationList")
                                    },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Thin
                            )
                        }
                    }
                }
            }
            HorizontalDivider(
                color = Color.White,
                thickness = 1.dp,
                modifier = Modifier.padding(start = 16.dp, end = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row() {
                    Button(
                        onClick = {
                            trainViewModel.fetchTrainData(
                                from = codeFrom,
                                to = codeTo,
                                date = "2024-12-09"
                            )
                        },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color.Red,
                            contentColor = Color.White,
                            disabledContainerColor = Color.Red,
                            disabledContentColor = Color.Black
                        ),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(8.dp)
                    ) {
                        Text(text = "Найти Поезд")
                    }
                }
            }
        }
    }
}


@Composable
fun DisplayTrainInfo(trains: TrainResponse?){
    trains?.let {
        LazyColumn(
            modifier = Modifier.fillMaxSize()
        ) {
            items(it.segments){ segment ->
                TrainInfoCard(segment = segment, modifier = if (segment == it.segments.last()) Modifier.padding(bottom = 24.dp, top = 16.dp, start = 8.dp, end = 8.dp) else Modifier.padding(top = 16.dp, start = 8.dp, end = 8.dp))
            }
        }
    }
}

@Composable
fun TrainInfoCard(segment: Segment, modifier: Modifier){
    Card (
        modifier = modifier
            .fillMaxWidth(),
        elevation = CardDefaults.cardElevation(8.dp),
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Электричка: ${segment.thread.number}", fontWeight = FontWeight.Bold)
            Text(text = segment.thread.title, fontSize = 12.sp)
            Spacer(Modifier.height(8.dp))
            Text(text = "${segment.departure} - ${segment.arrival}", fontSize = 36.sp)
            Spacer(Modifier.height(8.dp))
            Text(text = "Остановки: ${segment.stops.replace(":", "")}")
            Text(text = "В пути: ${segment.duration} мин.")
        }
    }
}

@Composable
fun StationsListScreen(navController: NavController) {
    val repository = Repository(TrainApi = TrainApiInterface.create(), StationApi = StationApiInterface.create())
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
            modifier = Modifier.fillMaxSize().blur(10.dp),
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
                        .border(width = 1.dp, color = Color.White, shape = RoundedCornerShape(15.dp)),
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

                                }
                                .padding(16.dp)
                        )
                    }
                }
            }
        }
    }
}
