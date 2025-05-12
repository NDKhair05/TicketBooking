package com.example.ticketbooking.Activities.SeatSelect

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Activities.PassengerInfo.PassengerInfoActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Domain.FlightModel

class SeatSelectActivity : AppCompatActivity() {
    private var numPassenger: Int = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val flight = intent.getParcelableExtra<FlightModel>("flight")
        numPassenger = intent.getIntExtra("numPassenger", 1)
        val numAdultPassenger = intent.getIntExtra("numAdultPassenger", 1)
        val numKidPassenger = intent.getIntExtra("numKidPassenger", 0)
        val listPassengerType = List(numKidPassenger) {false} + List(numAdultPassenger) {true}

        setContent {
            StatusTopBarColor()

            if (flight != null) {
                SeatListScreen(
                    flight = flight,
                    passengerCount = numPassenger,
                    onBackClick = { finish() },
                    onConfirm = {
                        Log.d("debug", listPassengerType.toString())
                        val intent = Intent(this, PassengerInfoActivity::class.java).apply {
                            putExtra("flight", flight)
                            putExtra("numAdultPassenger", numAdultPassenger)
                            putExtra("numKidPassenger", numKidPassenger)
                        }
                        startActivity(intent, null)
                    }
                )
            }
        }
    }
}