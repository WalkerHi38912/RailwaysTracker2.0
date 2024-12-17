package com.example.russianrailways20.View

import android.icu.util.LocaleData
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
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
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
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
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
import com.example.russianrailways20.Model.StationInfo
import com.example.russianrailways20.Model.StationResponse
import com.example.russianrailways20.Model.TrainApiInterface
import com.example.russianrailways20.Model.TrainResponse
import com.example.russianrailways20.Model.TrainViewModelFactory
import com.example.russianrailways20.R
import com.example.russianrailways20.ViewModel.TrainViewModel
import com.example.russianrailways20.ui.theme.RussianRailways20Theme
import com.maxkeppeker.sheets.core.models.base.rememberSheetState
import com.maxkeppeler.sheets.calendar.CalendarDialog
import com.maxkeppeler.sheets.calendar.models.CalendarConfig
import com.maxkeppeler.sheets.calendar.models.CalendarSelection
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Month
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Locale

@ExperimentalMaterial3Api
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

@ExperimentalMaterial3Api
@Composable
fun NavigationGraph() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = "main") {
        composable("main") {
            MainScreen(navController)
        }
        composable(
            route = "stationList/{field}",
            arguments = listOf(navArgument("field") { type = NavType.StringType })
        ) { backStackEntry ->
            val field = backStackEntry.arguments?.getString("field") ?: "to" // Default "to"
            StationsListScreen(navController, field)
        }
    }
}

@ExperimentalMaterial3Api
@Composable
fun MainScreen(navController: NavController){
    val repository = Repository(TrainApi = TrainApiInterface.create(), StationApi = StationApiInterface.create())
    val factory = TrainViewModelFactory(repository)
    val trainViewModel: TrainViewModel = viewModel(factory = factory)
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.Red)
    ){
        Image(painter = painterResource(R.drawable.back_ground), contentDescription = null, modifier = Modifier
          .fillMaxSize()
          .blur(10.dp), contentScale = ContentScale.FillBounds)
        TrainScreen(trainViewModel, navController)
    }
}

@ExperimentalMaterial3Api
@Composable
fun TrainScreen(trainViewModel: TrainViewModel, navController: NavController){
    val trainData by trainViewModel.trainData.collectAsState()

    Column {
        Spacer(Modifier.height(54.dp))
        MainMenu(trainViewModel, navController)
        DisplayTrainInfo(trains = trainData)
    }
}

@ExperimentalMaterial3Api
@Composable
fun MainMenu(
    trainViewModel: TrainViewModel,
    navController: NavController
) {
    var textFrom by remember { mutableStateOf("") }
    var textTo by remember { mutableStateOf("") }
    var codeFrom by remember { mutableStateOf("") }
    var codeTo by remember { mutableStateOf("") }
    val calendarState = rememberSheetState()

    val apiSelectedDate = remember { mutableStateOf( LocalDate.now() ) }
    val dateDay = DateTimeFormatter.ofPattern("dd", Locale("ru", "RU"))
    val dateMonth = DateTimeFormatter.ofPattern("MMM", Locale("ru", "RU"))
    val dateDayOfWeek = DateTimeFormatter.ofPattern("EE", Locale("ru", "RU"))
    val viewDateDay by remember {
        derivedStateOf {
            apiSelectedDate.value.format(dateDay)
        }
    }
    val viewDateMonth by remember {
        derivedStateOf {
            apiSelectedDate.value.format(dateMonth)
        }
    }
    val viewDateDayOfWeek by remember {
        derivedStateOf {
            apiSelectedDate.value.format(dateDayOfWeek)
        }
    }

    val currentBackStackEntry = navController.currentBackStackEntry
    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationTitle_to")
        ?.observe(LocalLifecycleOwner.current) { title ->
            textTo = title ?: ""
        }

    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationCode_to")
        ?.observe(LocalLifecycleOwner.current) { code ->
            codeTo = code ?: ""
        }

    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationTitle_from")
        ?.observe(LocalLifecycleOwner.current) { title ->
            textFrom = title ?: ""
        }

    currentBackStackEntry?.savedStateHandle?.getLiveData<String>("selectedStationCode_from")
        ?.observe(LocalLifecycleOwner.current) { code ->
            codeFrom = code ?: ""
        }


    CalendarDialog(
        state = calendarState,
        config = CalendarConfig(

        ),
        selection = CalendarSelection.Date{ date ->
            apiSelectedDate.value = date
        }
    )


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
                                    var tmp = textFrom
                                    textFrom = textTo
                                    textTo = tmp
                                    tmp = codeFrom
                                    codeFrom = codeTo
                                    codeTo = tmp
                                }
                        )
                        Spacer(Modifier.width(8.dp))
                        Column(
                            modifier = Modifier
                                .padding(top = 8.dp, bottom = 8.dp, end = 16.dp)
                        ) {
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
                                        navController.navigate("stationList/to") // Переход для выбора "to"
                                    },
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Thin
                            )

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
                                        navController.navigate("stationList/from") // Переход для выбора "from"
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
                modifier = Modifier.padding(start = 56.dp, end = 16.dp)
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
            ) {
                Row{
                    Box(
                       modifier = Modifier.padding(8.dp)
                    ){
                        Row (
                            verticalAlignment = Alignment.CenterVertically
                        ){
                            Image(
                                painter = painterResource(id = R.drawable.baseline_event_24),
                                contentDescription = "Calendar",
                                modifier = Modifier
                                    .padding(start = 8.dp, end = 12.dp, top = 8.dp, bottom = 8.dp)
                                    .clickable {
                                        calendarState.show()
                                    }
                            )
                            Row {
                                Text(
                                    text = viewDateDay,
                                    color = Color.White,
                                    fontWeight = FontWeight.Thin,
                                    fontSize = 40.sp
                                )
                                Column(
                                    modifier = Modifier
                                        .padding(start = 4.dp),
                                    verticalArrangement = Arrangement.Top
                                ) {
                                    Text(
                                        text = viewDateMonth,
                                        color = Color.LightGray,
                                        fontSize = 15.sp
                                    )
                                    Text(
                                        text = viewDateDayOfWeek,
                                        color = Color.LightGray,
                                        fontSize = 12.sp,
                                        style = TextStyle(
                                            lineHeight = 14.sp
                                        )
                                    )
                                }
                            }
                        }
                    }
                    Button(
                        onClick = {
                            trainViewModel.fetchTrainData(
                                from = codeFrom,
                                to = codeTo,
                                date = apiSelectedDate.value.toString()
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
                            .fillMaxWidth()
                            .padding(start = 8.dp, end = 16.dp, top = 8.dp, bottom = 8.dp)
                    ) {
                        Log.e("ApiDate", "${apiSelectedDate.value}")
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
        shape = RoundedCornerShape(15.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent.copy(alpha = 0.4f))
    ){
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(text = "Электричка: ${segment.thread.number}", fontWeight = FontWeight.Bold, color = Color.White)
            Text(text = segment.thread.title, fontSize = 12.sp, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text(text = "${formatDateTime(segment.departure)} - ${formatDateTime(segment.arrival)}", fontSize = 36.sp, color = Color.White)
            Spacer(Modifier.height(8.dp))
            Text(text = "Остановки: ${segment.stops.replace(":", "")}", color = Color.White)
            Text(text = "В пути: ${segment.duration.toInt() / 60} мин.", color = Color.White)
        }
    }
}

fun formatDateTime(dateTimeString: String): String {
    val dateTime = OffsetDateTime.parse(dateTimeString)
    val formatter = DateTimeFormatter.ofPattern("HH:mm")
    return dateTime.format(formatter)
}

@Composable
fun StationsListScreen(navController: NavController, field: String) {
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

