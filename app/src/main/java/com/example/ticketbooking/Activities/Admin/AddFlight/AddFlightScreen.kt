package com.example.ticketbooking.Activities.Admin.AddFlight

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R
import com.google.firebase.database.FirebaseDatabase

@Composable
fun AddFlightScreen(
    onFlightAdded: () -> Unit,
    onBackClick: () -> Unit
) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("Flights")

// State cho từng trường
    var airlineName by remember { mutableStateOf("") }
    var airlineLogo by remember { mutableStateOf("") }
    var departureTime by remember { mutableStateOf("") }
    var destinationTime by remember { mutableStateOf("") }
    var classSeat by remember { mutableStateOf("") }
    var date by remember { mutableStateOf("") }
    var from by remember { mutableStateOf("") }
    var fromShort by remember { mutableStateOf("") }
    var to by remember { mutableStateOf("") }
    var toShort by remember { mutableStateOf("") }
    var numberSeat by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }

    Image(
        painter = painterResource(R.drawable.back),
        contentDescription = null,
        modifier = Modifier
            .clickable { onBackClick() }
            .padding(top = 30.dp, start = 20.dp)
    )

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .background(colorResource(R.color.darkPurple2)),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {


        Text("Thêm Chuyến Bay", fontSize = 22.sp, fontWeight = FontWeight.Bold, color = colorResource(R.color.orange), modifier = Modifier.padding(20.dp).padding(top = 15.dp))

        Spacer(Modifier.height(16.dp))
        OutlinedTextField(value = airlineName, onValueChange = { airlineName = it }, label = { Text("Hãng bay") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = airlineLogo, onValueChange = { airlineLogo = it }, label = { Text("Logo URL") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = departureTime, onValueChange = { departureTime = it }, label = { Text("Giờ khởi hành") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = destinationTime, onValueChange = { destinationTime = it }, label = { Text("Giờ đến") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
//        OutlinedTextField(value = classSeat, onValueChange = { classSeat = it }, label = { Text("Hạng ghế") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
//            colorResource(R.color.lightPurple)
//        ))
        OutlinedTextField(value = date, onValueChange = { date = it }, label = { Text("Ngày") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = from, onValueChange = { from = it }, label = { Text("Từ") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = fromShort, onValueChange = { fromShort = it }, label = { Text("Từ (viết tắt)") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = to, onValueChange = { to = it }, label = { Text("Đến") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = toShort, onValueChange = { toShort = it }, label = { Text("Đến (viết tắt)") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ))
        OutlinedTextField(value = numberSeat, onValueChange = { numberSeat = it }, label = { Text("Số ghế") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))
        OutlinedTextField(value = price, onValueChange = { price = it }, label = { Text("Giá") }, textStyle = TextStyle(color = Color.White), colors = OutlinedTextFieldDefaults.colors(
            colorResource(R.color.lightPurple)
        ), keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number))

        Spacer(Modifier.height(16.dp))
        Button (onClick = {
            val flightId = database.push().key ?: return@Button
            val flight = FlightModel(
                flightId = flightId,
                AirlineLogo = airlineLogo,
                AirlineName = airlineName,
                DepartureTime = departureTime,
                DestinationTime = destinationTime,
                ClassSeat = classSeat,
                Date = date,
                From = from,
                FromShort = fromShort,
                To = to,
                ToShort = toShort,
                NumberSeat = numberSeat.toIntOrNull() ?: 0,
                Price = price.toDoubleOrNull() ?: 0.0,
                ReservedSeats = ""
            )

            database.child(flightId).setValue(flight)
                .addOnSuccessListener {
                    Toast.makeText(context, "Đã thêm chuyến bay!", Toast.LENGTH_SHORT).show()
                    onFlightAdded()
                }
                .addOnFailureListener {
                    Toast.makeText(context, "Thêm thất bại: ${it.message}", Toast.LENGTH_SHORT).show()
                }
        },
        modifier = Modifier.padding(bottom = 16.dp).width(100.dp)) {
            Text("Thêm")
        }
    }
}