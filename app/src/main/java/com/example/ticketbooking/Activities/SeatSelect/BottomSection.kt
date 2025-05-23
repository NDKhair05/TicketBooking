package com.example.ticketbooking.Activities.SeatSelect

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun BottomSection(
    seatCount: Int,
    seatHaveToSelected: Int,
    selectedSeats: String,
    totalPrice: Double,
    onConfirmClick: () -> Unit,
    modifier: Modifier
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formattedPrice = formatter.format(totalPrice)
    Column (
        modifier = modifier
            .fillMaxWidth()
            .height(180.dp)
            .background(colorResource(R.color.darkPurple))
            .padding(vertical = 16.dp)
    ) {
        Row (
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            LegendItem(text = "Available", color = colorResource(R.color.green))
            LegendItem(text = "Selected", color = colorResource(R.color.orange))
            LegendItem(text = "Unavailable", color = colorResource(R.color.grey))

        }
        Spacer(modifier = Modifier.height(8.dp))

        Row(modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 32.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                text = "$seatCount / $seatHaveToSelected Seat Selected",
                color = Color.White,
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp
                )
                Text(
                    text = if(selectedSeats.isBlank()) "-" else selectedSeats,
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }
            Text(
                text = formattedPrice,
                color = colorResource(R.color.orange),
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp
            )
        }
        GradientButton(onClick = onConfirmClick, "Confirm Seats")
    }
}