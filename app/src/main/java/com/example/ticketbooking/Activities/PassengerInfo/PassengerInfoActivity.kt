package com.example.ticketbooking.Activities.PassengerInfo

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R

class PassengerInfoActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val flight = intent.getParcelableExtra<FlightModel>("flight")
        val listOfSeats = flight?.Seats?.split(",")?.map {it.trim()}
        val numAdultPassenger = intent.getIntExtra("numAdultPassenger", 1)
        val numKidPassenger = intent.getIntExtra("numKidPassenger", 0)
        val listPassengerType = List(numKidPassenger) {false} + List(numAdultPassenger) {true}
        setContent {
            if (listOfSeats != null) {
                PassengerInfoScreen(
                    passengerTypes = listPassengerType,
                    selectedSeats = listOfSeats,
                    flight = flight,
                    onBackClick = {finish()},
                )
            }
        }
    }
}