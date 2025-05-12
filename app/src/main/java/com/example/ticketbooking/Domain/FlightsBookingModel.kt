package com.example.ticketbooking.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlightsBookingModel(
    val bookingId: String = "",
    val passengers: List<PassengerInfo> = emptyList(),
    val flight: FlightModel = FlightModel(),
    val bookingDate: String = "",
    val status: String = "Pending",
    val totalPrice: Double = 0.0
) : Parcelable
