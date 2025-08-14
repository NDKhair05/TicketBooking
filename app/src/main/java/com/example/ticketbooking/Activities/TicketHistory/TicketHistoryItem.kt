package com.example.ticketbooking.Activities.TicketHistory

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TicketHistoryItem(
    booking: FlightsBookingModel,
    onClick: () -> Unit
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(booking.totalPrice)

    Card(
        shape = RoundedCornerShape(16.dp),
        backgroundColor = colorResource(R.color.lightPurple),
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = "${booking.flight.From} → ${booking.flight.To}",
                fontWeight = FontWeight.Bold,
                fontSize = 18.sp,
                color = Color.Black
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Ngày: ${booking.flight.Date} • ${booking.flight.DepartureTime}",
                color = Color.DarkGray
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Hành khách: ${booking.passengers.size}",
                color = Color.DarkGray
            )
            Spacer(Modifier.height(4.dp))
            Text(
                text = "Tổng tiền: $formattedPrice",
                color = Color.Black,
                fontWeight = FontWeight.Bold
            )
        }
    }
}