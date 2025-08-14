package com.example.ticketbooking.Activities.TicketDetail

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.Dimension
import com.example.ticketbooking.Activities.SeatSelect.TicketDetailHeader
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.R
import com.example.ticketbooking.ViewModel.FlightBookingViewModel
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TicketDetailScreen(
    booking: FlightsBookingModel,
    onBackClick: () -> Unit,
    onConfirmBookingClick: () -> Unit,
) {
    val context = LocalContext.current
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(booking.flight.TotalPrice)
    var showDialog by remember { mutableStateOf(false) }


    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkPurple2))
    ) {
            ConstraintLayout(modifier = Modifier
                .fillMaxSize()
                .background(colorResource(R.color.darkPurple2))
            ) {
                val (topSection, ticketList, confirmBtn) = createRefs()
                val guideline = createGuidelineFromTop(0.15f)

                TicketDetailHeader(onBackClick = onBackClick, modifier = Modifier.constrainAs(topSection) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                })

                LazyColumn(
                    modifier = Modifier
                        .constrainAs(ticketList) {
                            top.linkTo(guideline)
                            bottom.linkTo(confirmBtn.top)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                            height = Dimension.fillToConstraints
                        }
                        .fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 16.dp)
                ) {
                    items(booking.passengers.size) { index ->
                        val passenger = booking.passengers[index]
                        TicketDetailContent(
                            passengerName = passenger.fullName,
                            seatNumber = passenger.seatNumber,
                            booking = booking,
                        )
                    }
                }
                Box (
                    modifier = Modifier.constrainAs(confirmBtn) {
                        bottom.linkTo(parent.bottom, margin = 24.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }) {
                    GradientButton(
                        onClick = { showDialog = true },
                        text = "Confirm Booking")
                }

                if (showDialog) {
                    AlertDialog(
                        onDismissRequest = { showDialog = false },
                        title = { Text(text = "Confirm Flight") },
                        text = { Text(text = "Total Price: $formattedPrice\nWould you like to proceed with this booking?") },
                        confirmButton = {
                            TextButton(onClick = {
                                onConfirmBookingClick()
                                showDialog = false
                                }
                            ) {
                                Text("Confirm")
                            }
                        },
                        dismissButton = {
                            TextButton(onClick = { showDialog = false }) {
                                Text("Cancel")
                            }
                        }
                    )
                }
            }


    }
}