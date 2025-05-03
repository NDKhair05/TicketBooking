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

@Composable
fun PaymentScreen(
    flight: FlightModel,
    onPaymentSuccess: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    val showQR = remember { mutableStateOf(false) }
    val paymentConfirmed = remember { mutableStateOf(false) } // Track if payment is confirmed
    val showSuccessMessage = remember { mutableStateOf(false) } // Track if success message should show

    // Số tiền thanh toán
    val amount = flight.TotalPrice // đơn vị: VNĐ

    // Thông tin Vietcombank
    val bank = "VCB"
    val accountNumber = "1762731356"
    val accountName = "NGUYEN VAN HOANG"

    val qrUrl = "https://img.vietqr.io/image/$bank-$accountNumber-compact2.png?amount=$amount&addInfo=Thanh+toan+ve"

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
                Text(
                    text = "Thanh toán qua Vietcombank",
                    style = TextStyle(fontSize = 20.sp, fontWeight = FontWeight.Bold)
                )

                Text("Số tiền cần thanh toán: $amount VNĐ")

                Button(
                    onClick = { showQR.value = true },
                    modifier = Modifier.fillMaxWidth(),
                    colors = ButtonDefaults.buttonColors(backgroundColor = colorResource(id = R.color.purple_500)),
                    enabled = !showQR.value
                ) {
                    Text("Payment", color = Color.White, fontSize = 18.sp)
                }

                if (showQR.value && !paymentConfirmed.value) {
                    Text("Quét mã QR để thanh toán", fontSize = 16.sp, fontWeight = FontWeight.Medium)
                    Spacer(modifier = Modifier.height(8.dp))

                    AsyncImage(
                        model = qrUrl,
                        contentDescription = "QR VietQR",
                        modifier = Modifier.size(200.dp),
                        contentScale = ContentScale.Fit
                    )

                    Spacer(modifier = Modifier.height(16.dp))
                    Button(
                        onClick = {
                            Toast.makeText(context, "Confirm.", Toast.LENGTH_SHORT).show()
                            paymentConfirmed.value = true
                            // Start showing success message with delay
                            scope.launch {
                                delay(1500) // Delay 1.5 seconds
                                showSuccessMessage.value = true
                                delay(2000) // Delay 2 seconds before navigating back
                                onPaymentSuccess() // Trigger onPaymentSuccess after the delay
                            }
                        },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("I have paid", fontSize = 16.sp)
                    }
                }

                // Hiển thị thông báo thanh toán thành công sau khi delay
                if (showSuccessMessage.value) {
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "✔ Payment Successful",
                        color = Color.Green,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

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
