package com.example.russianrailways20.View

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.russianrailways20.Model.Segment
import com.example.russianrailways20.Model.TrainResponse
import java.time.OffsetDateTime
import java.time.format.DateTimeFormatter

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