package com.example.ticketbooking.Activities.Admin.FlightCheck

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

@Composable
fun FlightManagementScreen(onBackClick: () -> Unit) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("Flights")
    var flights by remember { mutableStateOf(listOf<FlightModel>()) }

// Load chuyến bay từ Firebase khi Composable được tạo
    LaunchedEffect(true) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val tempList = mutableListOf<FlightModel>()
                for (flightSnapshot in snapshot.children) {
                    val flight = flightSnapshot.getValue(FlightModel::class.java)
                    if (flight != null) tempList.add(flight)
                }
                flights = tempList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context, "Không thể tải dữ liệu: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkPurple2))
            .padding(16.dp)
    ) {

        Image(
            painter = painterResource(R.drawable.back),
            contentDescription = null,
            modifier = Modifier
                .clickable { onBackClick() }
                .padding(top = 20.dp, start = 8.dp)
        )

        Text(
            text = "Quản lý Chuyến Bay",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = colorResource(R.color.orange),
            modifier = Modifier.padding(bottom = 16.dp, top = 20.dp)
        )

        if (flights.isEmpty()) {
            Text("Không có chuyến bay", color = Color.White)
        } else {
            LazyColumn {
                items(flights) { flight ->
                    FlightItem(flight = flight, onDelete = {
                        database.child(flight.flightId).removeValue()
                            .addOnSuccessListener {
                                Toast.makeText(context, "Đã xoá chuyến bay", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Toast.makeText(context, "Lỗi khi xoá: ${it.message}", Toast.LENGTH_SHORT).show()
                            }
                    })
                }
            }
        }
    }
}


@Composable
fun FlightItem(flight: FlightModel, onDelete: () -> Unit) {
    Card(
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp),
    ) {
        Row(
            modifier = Modifier
                .background(Color.White)
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(72.dp)
        ) {
            Column {
                Text("Hãng: ${flight.AirlineName}", fontWeight = FontWeight.Bold)
                Text("Từ ${flight.FromShort} đến ${flight.ToShort}")
                Text("Ngày: ${flight.Date}")
                Text("Giờ: ${flight.DepartureTime} - ${flight.DestinationTime}")
                Text("Giá: ${flight.Price} đ")
                Text("Hạng: ${flight.ClassSeat}")
            }

            IconButton(onClick = onDelete) {
                Icon(Icons.Default.Delete, contentDescription = "Xoá", tint = Color.Red)
            }
        }
    }
}