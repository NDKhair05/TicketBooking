package com.example.ticketbooking.Activities.Payment

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.ViewModel.FlightBookingViewModel
import com.google.firebase.database.FirebaseDatabase
import java.text.NumberFormat
import java.util.Locale

@Composable
fun PaymentScreen(
    booking: FlightsBookingModel,
    bookingId: String, // ID của booking để cập nhật trạng thái
    onPaymentSuccess: () -> Unit,
    onCancel: () -> Unit,
    flightBookingViewModel: FlightBookingViewModel
) {
    val context = LocalContext.current
    val showQR = remember { mutableStateOf(false) }
    val paymentConfirmed = remember { mutableStateOf(false) }
    val showSuccessMessage = remember { mutableStateOf(false) }

    val amount = booking.flight.TotalPrice
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    val formatedPrice = formatter.format(amount)


    val scope = rememberCoroutineScope()

    Scaffold(
        modifier = Modifier.background(colorResource(R.color.darkPurple2)),
        topBar = {
            TopAppBar(
                title = { Text("Payment", modifier = Modifier.padding(top = 40.dp, start = 10.dp))},
                backgroundColor = colorResource(R.color.darkPurple2),
                contentColor = colorResource(R.color.orange),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
            )
        },
        content = { paddingValues ->
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(colorResource(R.color.darkPurple2)),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Tiêu đề
                Text(
                    text = "Pay by Online Banking",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold, color = Color.White),
                    modifier = Modifier.padding(top = 10.dp, bottom = 5.dp)
                )

                // Số tiền cần thanh toán
                Text("The total for the ticket : $formatedPrice", color = Color.White)

                // Nút thanh toán
                Button(
                    onClick = { showQR.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    enabled = !showQR.value
                ) {
                    Box(
                        modifier = Modifier
                            .background(brush = Brush.linearGradient(
                                colors = listOf(
                                    colorResource(R.color.purple),
                                    colorResource(R.color.pink)
                                )
                            ),
                                shape = RoundedCornerShape(50.dp)
                            )
                            .fillMaxWidth()
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        androidx.compose.material3.Text(
                            text = "Payment",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp
                        )
                    }
                }

                // Hiển thị mã QR và nút "I have paid" sau khi nhấn nút thanh toán
                if (showQR.value && !paymentConfirmed.value) {
                    Text("Scan QR to pay", fontSize = 16.sp, fontWeight = FontWeight.Medium, color = Color.White)
                    Spacer(modifier = Modifier.height(8.dp))

                    // Hiển thị mã QR để thanh toán
                    AsyncImage(
                        model = "https://img.vietqr.io/image/VCB-1762731356-compact2.png?amount=$amount&addInfo=${booking.bookingId}",
                        contentDescription = "QR VietQR",
                        modifier = Modifier.size(270.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(8.dp))
                    // Nút "I have paid"

                    GradientButton(
                        text = "I have paid",
                        onClick = {
                            flightBookingViewModel.createBooking(booking) { success ->
                                if (success) {
                                    paymentConfirmed.value = true
                                    Toast.makeText(context, "Booking Successful.", Toast.LENGTH_SHORT).show()
                                    onPaymentSuccess()
                                } else {
                                    Toast.makeText(context, "Booking Failed", Toast.LENGTH_SHORT)
                                        .show()                                       }
                            }

                            // Sau 1.5 giây, hiển thị thông báo và cập nhật trạng thái
                            scope.launch {
                                delay(1500)
                                showSuccessMessage.value = true
                                delay(2000) // Delay thêm 2 giây trước khi thực hiện hành động sau
                                onPaymentSuccess() // Trigger sự kiện thanh toán thành công
                            }
                        }
                    )
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
                    shape = RoundedCornerShape(50.dp),
                    modifier = Modifier.width(310.dp).height(48.dp)
                ) {
                    Text("Cancel", color = Color.Gray, fontSize = 18.sp)
                }
            }
        }
    )
}
