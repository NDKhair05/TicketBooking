package com.example.ticketbooking.Activities.SeatSelect

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Activities.TicketDetail.TicketDetailActivity
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R

class SeatSelectActivity : AppCompatActivity() {
    private lateinit var flight: FlightModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val flight = intent.getParcelableExtra<FlightModel>("flight")

        setContent {
            StatusTopBarColor()

            if (flight != null) {
                SeatListScreen(
                    flight = flight,
                    onBackClick = { finish() },
                    onConfirm = {
                        val intent = Intent(this, TicketDetailActivity::class.java).apply {
                            putExtra("flight", flight)
                        }
                        startActivity(intent, null)
                    }
                )
            }
        }
    }
}