package com.example.ticketbooking.Activities.Payment

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.google.firebase.database.FirebaseDatabase

@Composable
fun PaymentScreen(
    flight: FlightModel,
    userId: String, // ID người dùng
    bookingId: String, // ID của booking để cập nhật trạng thái
    onPaymentSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val showQR = remember { mutableStateOf(false) }
    val paymentConfirmed = remember { mutableStateOf(false) }
    val showSuccessMessage = remember { mutableStateOf(false) }

    val amount = flight.TotalPrice

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(title = { Text("Payment") })
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tiêu đề
                Text(
                    text = "Thanh toán qua Vietcombank",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                // Số tiền cần thanh toán
                Text("Số tiền cần thanh toán: $amount VNĐ")

                // Nút thanh toán
                Button(
                    onClick = { showQR.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.purple_500)),
                    enabled = !showQR.value
                ) {
                    Text("Payment", color = Color.White, fontSize = 18.sp)
                }

                // Hiển thị mã QR và nút "I have paid" sau khi nhấn nút thanh toán
                if (showQR.value && !paymentConfirmed.value) {
                    Text("Quét mã QR để thanh toán", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Hiển thị mã QR để thanh toán
                    AsyncImage(
                        model = "https://img.vietqr.io/image/VCB-1762731356-compact2.png?amount=$amount&addInfo=Thanh+toan+ve",
                        contentDescription = "QR VietQR",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    // Nút "I have paid"
                    Button(
                        onClick = {
                            Toast.makeText(context, "Confirm.", Toast.LENGTH_SHORT).show()
                            paymentConfirmed.value = true

                            // Cập nhật trạng thái thanh toán của booking là "pending"
                            val database = FirebaseDatabase.getInstance().getReference("Bookings/$bookingId")
                            val updatedBooking = FlightsBookingModel(
                                bookingId = bookingId,
                                status = "Pending", // Cập nhật trạng thái thanh toán là "pending"
                                flight = flight,
                                totalPrice = amount,
                                passengers = listOf() // Thêm thông tin hành khách nếu cần
                            )
                            database.setValue(updatedBooking)

                            // Sau 1.5 giây, hiển thị thông báo và cập nhật trạng thái
                            scope.launch {
                                delay(1500)
                                showSuccessMessage.value = true
                                delay(2000) // Delay thêm 2 giây trước khi thực hiện hành động sau
                                onPaymentSuccess() // Trigger sự kiện thanh toán thành công
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("I have paid", fontSize = 16.sp)
                    }
                }

                // Hiển thị thông báo "Pending Payment" sau khi người dùng thanh toán
                if (showSuccessMessage.value) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "✔ Pending Payment",
                        color = Color.Yellow,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Nút hủy thanh toán
                OutlinedButton(
                    onClick = { onCancel() },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Cancel", color = Color.Gray, fontSize = 18.sp)
                }
            }
        }
    )
}
