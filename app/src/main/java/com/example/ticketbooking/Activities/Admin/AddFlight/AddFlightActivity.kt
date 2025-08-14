package com.example.ticketbooking.Activities.Admin.AddFlight

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity

class AddFlightActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AddFlightScreen(
                onFlightAdded = {},
                onBackClick = {finish()})
        }
    }
}