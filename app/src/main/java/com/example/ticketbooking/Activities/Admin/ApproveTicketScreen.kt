package com.example.ticketbooking.Activities.Admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.ui.theme.TicketBookingTheme
import com.google.firebase.database.*

class ApproveTicketsScreen : ComponentActivity() {
    private lateinit var database: DatabaseReference
    private val tickets = mutableListOf<FlightsBookingModel>() // Lưu trữ danh sách vé

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Khởi tạo Firebase Database
        database = FirebaseDatabase.getInstance().getReference("FlightsBookings")

        // Lấy danh sách vé từ Firebase
        getTicketsFromFirebase()

        setContent {
            TicketBookingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    // Truyền onUpdateStatus vào màn hình duyệt vé
                    ApproveTicketsScreenContent(tickets)
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
fun ApproveTicketsScreenContent(tickets: List<FlightsBookingModel>) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2D42))
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Duyệt Vé",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        // Hiển thị danh sách vé
        if (tickets.isNotEmpty()) {
            tickets.forEach { ticket ->
                TicketItem(ticket)
            }
        } else {
            Text(text = "Không có vé nào cần duyệt", fontSize = 18.sp, color = Color.White)
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
                Text(text = "Booking ID: ${ticket.bookingId}", color = Color.Black)
                Text(text = "Date: ${ticket.bookingDate}", color = Color.Black)
                Text(text = "Total Price: $${ticket.totalPrice}", color = Color.Black)
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
                    // Cập nhật trạng thái vé khi nhấn "Reject"
                    val ticketRef = database.child(ticket.bookingId)
                    ticketRef.child("status").setValue("Rejected")
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                Toast.makeText(context, "Ticket Rejected", Toast.LENGTH_SHORT).show()
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
