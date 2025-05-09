package com.example.ticketbooking.Activities.TicketDetail

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Activities.Dashboard.DashboardActivity
import com.example.ticketbooking.Activities.Payment.PaymentActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R

class TicketDetailActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val flight = intent.getParcelableExtra<FlightModel>("flight")

        setContent {
            StatusTopBarColor()

            if (flight != null) {
                TicketDetailScreen(
                    flight = flight,
                    onBackClick = { finish() },
                    onConfirmBookingClick = {
                        val intent = Intent(this, PaymentActivity::class.java).apply {
                            putExtra("flight", flight)
                        }
                        startActivity(intent)
                    }
                )
            }
        }
    }
}