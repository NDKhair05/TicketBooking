package com.example.ticketbooking.Activities.TicketHistory

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ticketbooking.Activities.Dashboard.YellowTitle
import com.example.ticketbooking.Activities.TicketDetail.TicketDetailContent
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.R
import com.example.ticketbooking.Utils.UserPreferences
import com.example.ticketbooking.ViewModel.FlightBookingViewModel

import java.text.SimpleDateFormat
import java.util.*

@Composable
fun TicketHistoryScreen(innerPaddingValues: PaddingValues) {
    val context = LocalContext.current
    val viewModel = FlightBookingViewModel()
    val userPreferences = remember { UserPreferences(context) }
    val userId = remember { userPreferences.getUser()?.uid }
    val bookings by viewModel.getUserBookings(userId ?: "").observeAsState(emptyList())


    var selectedStatus by remember { mutableStateOf("All") }
    var selectedDate by remember { mutableStateOf("") }
    var showDatePicker by remember { mutableStateOf(false) }

    val filteredBookings = bookings.filter { booking ->
        val statusMatch = selectedStatus == "All" || booking.status == selectedStatus
        val dateMatch = selectedDate.isBlank() || booking.flight.Date == selectedDate
        statusMatch && dateMatch
    }

        ConstraintLayout (modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkPurple2))
            .padding(innerPaddingValues)
            .padding(top = 36.dp, start = 16.dp, end = 16.dp, bottom = 30.dp)
            .navigationBarsPadding()
        ) {
            val ( headerTitle, worldImg, btnRow, ticketList) = createRefs()

            Text(
                text = "Ticket Booked",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.orange),
                modifier = Modifier
                    .padding(start = 8.dp)
                    .constrainAs(headerTitle) {
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                        top.linkTo(btnRow.bottom, margin = 20.dp)
                    }
            )

            Image(
                painter = painterResource(R.drawable.world),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(worldImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .constrainAs(btnRow) {
                        top.linkTo(worldImg.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                },
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
            Button(
                onClick = { showDatePicker = true },
                colors = ButtonDefaults.buttonColors(containerColor = Color.White)
            ) {
                Text(text = if (selectedDate.isBlank()) "Chọn ngày" else selectedDate, color = Color.Black)
            }

            DropdownMenuStatus(selectedStatus) { selectedStatus = it }
        }

            if (showDatePicker) {
                val calendar = Calendar.getInstance()
                android.app.DatePickerDialog(
                    context,
                    { _, year, month, dayOfMonth ->
                        selectedDate = "%02d/%02d/%d".format(dayOfMonth, month + 1, year)
                        showDatePicker = false
                    },
                    calendar.get(Calendar.YEAR),
                    calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH)
                ).show()
            }

            LazyColumn(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .constrainAs(ticketList) {
                        top.linkTo(headerTitle.bottom, margin = 30.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                items(filteredBookings) { booking ->
                    HorizontalDivider(
                        modifier = Modifier.padding(vertical = 8.dp),
                        thickness = 1.dp,
                        color = Color.Gray
                    )
                    TicketHistoryCard(booking)
                    Spacer(modifier = Modifier.height(12.dp))
                }
            }
        }


    }


@Composable
fun TicketHistoryCard(booking: FlightsBookingModel) {
    var showDetail by remember { mutableStateOf(false) }
    // Hiển thị đẹp mắt chi tiết từng vé
    val passengerList = booking.passengers
    passengerList.forEach { passenger ->
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .clickable { showDetail = !showDetail },
            elevation = CardDefaults.cardElevation(4.dp),
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text(text = passenger.fullName, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(4.dp))
                Text(text = "Flight: ${booking.flight.From} -> ${booking.flight.To}")
                Text(text = "Date: ${booking.flight.Date}")
                Text(text = "Status: ${booking.status}")
            }
        }
        if(showDetail) {
        TicketDetailContent(
            passengerName = passenger.fullName,
            seatNumber = passenger.seatNumber,
            booking = booking,
        )
        }
    }
}


@Composable
fun DropdownMenuStatus(selected: String, onSelect: (String) -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val statuses = listOf("All", "Pending", "Approved","Rejected")

    Box {
        Button(onClick = { expanded = true }, colors = ButtonDefaults.buttonColors(containerColor = Color.White)) {
            Text(text = selected, color = Color.Black)
        }

        DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
            statuses.forEach { status ->
                DropdownMenuItem(
                    text = { Text(status) },
                    onClick = {
                        onSelect(status)
                        expanded = false
                    }
                )
            }
        }
    }
}