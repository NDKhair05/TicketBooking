package com.example.ticketbooking.Activities.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
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
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.R
import com.example.ticketbooking.ui.theme.TicketBookingTheme
import com.google.firebase.database.*

class ApproveTicketsScreen : ComponentActivity() {
    private lateinit var database: DatabaseReference
    private val tickets = mutableStateListOf<FlightsBookingModel>() // Lưu trữ danh sách vé

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance().getReference("FlightsBookings")

        // Lấy danh sách vé từ Firebase
        getTicketsFromFirebase()
        enableEdgeToEdge()
        setContent {
            TicketBookingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = colorResource(R.color.darkPurple2)
                ) {
                    // Truyền onUpdateStatus vào màn hình duyệt vé
                    ApproveTicketsScreenContent(
                        tickets,
                        onBackClick = { finish() }
                    )
                }
            }
        }
    }

    // Hàm lấy dữ liệu vé từ Firebase
    private fun getTicketsFromFirebase() {
        database.orderByChild("status").equalTo("Pending").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                tickets.clear()
                for (ticketSnapshot in snapshot.children) {
                    val ticket = ticketSnapshot.getValue(FlightsBookingModel::class.java)
                    if (ticket != null) {
                        tickets.add(ticket)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(this@ApproveTicketsScreen, "Failed to load tickets: ${error.message}", Toast.LENGTH_SHORT).show()
            }
        })
    }

    // Hàm cập nhật trạng thái vé
    private fun onUpdateStatus(bookingId: String, approved: Boolean) {
        val ticketRef = database.child(bookingId)
        val status = if (approved) "Approved" else "Rejected"
        ticketRef.child("status").setValue(status).addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Toast.makeText(this, "Ticket updated to $status", Toast.LENGTH_SHORT).show()
            } else {
                Toast.makeText(this, "Failed to update ticket status", Toast.LENGTH_SHORT).show()
            }
        }
    }
}

@Composable
fun ApproveTicketsScreenContent(tickets: List<FlightsBookingModel>, onBackClick: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkPurple2))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        ConstraintLayout (modifier = Modifier.padding(top =  20.dp)) {
            val (backBtn, headerTitle, ticketList) = createRefs()

            Image(
                painter = painterResource(R.drawable.back),
                contentDescription = null,
                modifier = Modifier
                    .clickable { onBackClick() }
                    .constrainAs(backBtn) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                    }
            )

            Text(
                text = "Duyệt Vé",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.orange),
                modifier = Modifier
                    .padding(16.dp)
                    .constrainAs(headerTitle) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )


            // Hiển thị danh sách vé
            LazyColumn (
                modifier = Modifier
                    .constrainAs(ticketList) {
                        top.linkTo(headerTitle.bottom, margin = 20.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                if (tickets.isNotEmpty()) {
                    items(tickets) { ticket ->
                        TicketItem(ticket)
                    }
                } else {
                    item {
                        Text(
                            text = "Không có vé nào cần duyệt",
                            fontSize = 18.sp,
                            color = Color.White,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(16.dp),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun TicketItem(ticket: FlightsBookingModel) {
    val context = LocalContext.current
    val database = FirebaseDatabase.getInstance().getReference("FlightsBookings")

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(12.dp),
        elevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(text = "Booking ID: ${ticket.bookingId}", color = Color.Black, fontSize = 14.sp)
                Text(text = "Date: ${ticket.bookingDate}", color = Color.Black, fontSize = 14.sp)
                Text(text = "Total Price: $${ticket.totalPrice}", color = Color.Black, fontSize = 14.sp)
            }

            Row {
                IconButton(onClick = {
                    // Cập nhật trạng thái vé khi nhấn "Approve"
                    val ticketRef = database.child(ticket.bookingId)
                    ticketRef.child("status").setValue("Approved")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Ticket Approved", Toast.LENGTH_SHORT).show()
                            } else {
                                Toast.makeText(context, "Failed to approve ticket", Toast.LENGTH_SHORT).show()
                            }
                        }
                }) {
                    Icon(Icons.Filled.Check, contentDescription = "Approve", tint = Color.Green)
                }
                IconButton(onClick = {
                    val ticketRef = database.child(ticket.bookingId)
                    val flightId = ticket.flight.flightId
                    val flightRef = FirebaseDatabase.getInstance().getReference("Flights").child(flightId)

                    // 1. Đặt trạng thái vé là "Rejected"
                    ticketRef.child("status").setValue("Rejected").addOnCompleteListener { task ->
                        if (task.isSuccessful) {
                            // 2. Lấy ReservedSeats thực tế từ FlightModel
                            flightRef.child("reservedSeats").get().addOnSuccessListener { snapshot ->
                                val reservedSeatsString = snapshot.getValue(String::class.java) ?: ""
                                val reservedList = reservedSeatsString.split(",").map { it.trim() }.filter { it.isNotEmpty() }.toMutableList()

                                val seatsToRemove = ticket.passengers.map { it.seatNumber.trim() }

                                // 3. Loại bỏ các ghế đã đặt bởi vé này
                                reservedList.removeAll(seatsToRemove)

                                // 4. Cập nhật lại reservedSeats
                                flightRef.child("reservedSeats").setValue(reservedList.joinToString(",")).addOnCompleteListener { updateTask ->
                                    if (updateTask.isSuccessful) {
                                        Toast.makeText(context, "Ticket rejected & seats released", Toast.LENGTH_SHORT).show()
                                    } else {
                                        Toast.makeText(context, "Status updated but failed to release seats", Toast.LENGTH_SHORT).show()
                                    }
                                }
                            }.addOnFailureListener {
                                Toast.makeText(context, "Failed to load reserved seats", Toast.LENGTH_SHORT).show()
                            }
                        } else {
                            Toast.makeText(context, "Failed to reject ticket", Toast.LENGTH_SHORT).show()
                        }
                    }
                }) {
                    Icon(Icons.Filled.Close, contentDescription = "Reject", tint = Color.Red)
                }
            }
        }
    }
}
