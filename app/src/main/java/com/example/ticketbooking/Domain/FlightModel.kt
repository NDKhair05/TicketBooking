package com.example.ticketbooking.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class FlightModel(
    var AirlineLogo: String = "",
    var AirlineName: String = "",
    var DepartureTime: String = "",
    var ClassSeat: String = "",
    var Date: String = "",
    var From: String = "",
    var FromShort: String = "",
    var NumberSeat: Int = 0,
    var Price: Double = 0.0,
    var TotalPrice: Double = 0.0,
    var Passenger: String = "",
    var Seats: String = "",
    var ReservedSeats: String = "",
    var DestinationTime: String = "",
    var To: String = "",
    var ToShort: String = ""
):Parcelable

