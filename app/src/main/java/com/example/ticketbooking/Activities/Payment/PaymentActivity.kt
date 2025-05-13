package com.example.ticketbooking.Activities.Payment

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Domain.FlightModel

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val flight = intent.getParcelableExtra<FlightModel>("flight")

        setContent {
            StatusTopBarColor()
            if (flight != null) {
                PaymentScreen(
                    flight = flight,
                    onPaymentSuccess = {
                        // Trả về kết quả hoặc chuyển màn khác nếu cần
                        finish()
                    },
                    onCancel = {
                        finish()
                    },
                    userId = TODO(),
                    bookingId = TODO()
                )
            } else {
                println("Flight Unvalid")
            }
        }
    }
}
