package com.example.ticketbooking.Activities.Dashboard

import androidx.compose.material3.DatePickerDialog
import android.content.Context
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import java.text.SimpleDateFormat
import android.icu.util.Calendar
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.R
import java.util.Locale

@Composable
fun DatePickerScreen(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val dateFormat = remember { SimpleDateFormat("dd / MM / yyyy", Locale.getDefault())  }

    val departureCalendar = remember { Calendar.getInstance() }
    val returnCalendar = remember { Calendar.getInstance().apply { add(Calendar.DAY_OF_YEAR, 1) } }

    var departureDate by remember { mutableStateOf(dateFormat.format(departureCalendar.time)) }
    var returnDate by remember { mutableStateOf(dateFormat.format(returnCalendar.time)) }

    Row {
        DatePickerItem(
            modifier,
            dateText = departureDate,
            onDateSelected = {selectedDate ->
                departureDate = selectedDate
            }, dateFormat = dateFormat,
            calendar = departureCalendar,
            context = context
        )

        Spacer(modifier = Modifier.width(16.dp))

        DatePickerItem(
            modifier,
            dateText = returnDate,
            onDateSelected = {selectedDate ->
                returnDate = selectedDate
            }, dateFormat = dateFormat,
            calendar = returnCalendar,
            context = context
        )
    }
}

@Composable
fun DatePickerItem(
    modifier: Modifier = Modifier,
    dateText: String,
    onDateSelected: (String) -> Unit,
    dateFormat: SimpleDateFormat,
    calendar: Calendar,
    context: Context
) {
    Row (
        modifier = modifier
            .height(60.dp)
            .padding(top=8.dp)
            .background(
                color = colorResource(R.color.lightPurple),
                shape = RoundedCornerShape(8.dp)
            )
            .clickable {
                showDatePickerDialog(context, calendar, dateFormat, onDateSelected)
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Image(
            painter = painterResource(R.drawable.calendar_ic),
            contentDescription = null,
            modifier = Modifier
                .padding(start = 8.dp)
                .size(24.dp)
        )

        Text(
            text = dateText,
            modifier = Modifier
                .padding(horizontal = 5.dp)
                .fillMaxWidth(),
            color = Color.Black,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            fontSize = 12.sp
        )
    }
}

fun showDatePickerDialog(
    context: Context,
    calendar: Calendar,
    dateFormat: SimpleDateFormat,
    onDateSelected:(String) -> Unit
) {
    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day  = calendar.get(Calendar.DAY_OF_MONTH)

    android.app.DatePickerDialog(context, {_, selectedYear, selectedMonth, selectedDay ->
        calendar.set(selectedYear, selectedMonth, selectedDay)
        val formatedDate = dateFormat.format(calendar.time)
        onDateSelected(formatedDate)
    }, year, month, day).show()
}