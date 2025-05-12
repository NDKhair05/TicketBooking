package com.example.ticketbooking.Activities.TicketDetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import coil.compose.AsyncImage
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.R
import java.text.NumberFormat
import java.util.Locale

@Composable
fun TicketDetailContent(
    passengerName: String,
    seatNumber: String,
    booking: FlightsBookingModel,
) {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val totalPrice = booking.flight.TotalPrice
    val formattedPrice = formatter.format(totalPrice)
    Column (modifier = Modifier
        .padding(24.dp)
        .background(color = colorResource(R.color.lightPurple),
            shape = RoundedCornerShape(20.dp)
        )
    ) {
        ConstraintLayout (
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp)
        ) {
            val (logo, arrivalTxt, lineImg, fromTxt, fromShortTxt, toTxt, toShortTxt) = createRefs()

            AsyncImage(
                model = booking.flight.AirlineLogo,
                contentDescription = null,
                modifier = Modifier
                    .size(200.dp, 50.dp)
                    .constrainAs(logo) {
                        top.linkTo(parent.top, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentScale = ContentScale.Fit
            )
            Text(
                text = "",
                fontSize = 12.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.darkPurple),
                modifier = Modifier.constrainAs(arrivalTxt) {
                    top.linkTo(logo.bottom, margin = 8.dp)
                    start.linkTo(logo.start)
                    end.linkTo(logo.end)
                }
            )

            Image(
                painter = painterResource(R.drawable.line_airple_blue),
                contentDescription = null,
                modifier = Modifier.constrainAs(lineImg) {
                    top.linkTo(arrivalTxt.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            Text(
                text ="from",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(fromTxt) {
                    top.linkTo(arrivalTxt.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(lineImg.start)
                }
            )

            Text(
                text = booking.flight.FromShort,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.constrainAs(fromShortTxt) {
                    top.linkTo(fromTxt.bottom, margin = 8.dp)
                    start.linkTo(fromTxt.start)
                    end.linkTo(fromTxt.end)
                    bottom.linkTo(lineImg.bottom)
                }
            )

            Text(
                text ="to",
                fontSize = 14.sp,
                color = Color.Black,
                modifier = Modifier.constrainAs(toTxt) {
                    top.linkTo(arrivalTxt.bottom)
                    end.linkTo(parent.end)
                    start.linkTo(lineImg.end)
                }
            )

            Text(
                text = booking.flight.ToShort,
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color.Black,
                modifier = Modifier.constrainAs(toShortTxt) {
                    top.linkTo(toTxt.bottom, margin = 8.dp)
                    start.linkTo(toTxt.start)
                    end.linkTo(toTxt.end)
                    bottom.linkTo(lineImg.bottom)
                }
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
             Column (
                 modifier = Modifier
                     .weight(0.5f)
             ){
                 Text(text = "From", color = Color.Black)
                 Text(text = booking.flight.From, color = Color.Black, fontWeight = FontWeight.Bold)
                 Spacer(modifier = Modifier.height(16.dp))
                 Text(text = "Date", color = Color.Black)
                 Text(text = booking.flight.Date, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
             }

            Spacer(modifier = Modifier.width(16.dp))

            Column (
                modifier = Modifier
                    .weight(0.5f)
            ){
                Text(text = "To", color = Color.Black)
                Text(text = booking.flight.To, color = Color.Black, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Time", color = Color.Black)
                Text(text = booking.flight.DepartureTime, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
            }
        }

        Image(
            painter = painterResource(R.drawable.dash_line),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Column (
                modifier = Modifier
                    .weight(0.5f)
            ){
                Text(text = "Full Name", color = Color.Black)
                Text(text = passengerName, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row {
                    Text(text = "Seat", color = Color.Black)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = seatNumber, color = Color.Black, fontWeight = FontWeight.Bold, fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(text = "Airlines", color = Color.Black)
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = booking.flight.AirlineName, color = Color.Black, fontWeight = FontWeight.Bold)
                }
            }
        }
        

        Image(
            painter = painterResource(R.drawable.dash_line),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth(),
            contentScale = ContentScale.FillWidth
        )

        Image(
            painter = painterResource(R.drawable.barcode),
            contentDescription = null,
            modifier = Modifier.fillMaxWidth().padding(16.dp),
            contentScale = ContentScale.FillWidth
        )
    }
}