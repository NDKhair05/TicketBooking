package com.example.ticketbooking.Activities.TicketDetail

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Activities.Payment.PaymentActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.R
import com.example.ticketbooking.ViewModel.FlightBookingViewModel

class TicketDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val booking = intent.getParcelableExtra<FlightsBookingModel>("booking")
        val flightBookingViewModel = FlightBookingViewModel()

        setContent {
            StatusTopBarColor()

            if (booking != null) {
                TicketDetailScreen(
                    booking = booking,
                    onBackClick = { finish() },
                    onConfirmBookingClick = {
                        val intent = Intent(this, PaymentActivity::class.java).apply {
                            putExtra("flight", booking.flight)
                        }
                        startActivity(intent)
                    },
                    flightBookingViewModel = flightBookingViewModel
                )
            }
        }
    }
}