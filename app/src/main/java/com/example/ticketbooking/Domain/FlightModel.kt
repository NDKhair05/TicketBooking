package com.example.ticketbooking.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import java.io.Serializable

@Parcelize
data class FlightModel(
    var AirlineLogo: String = "",
    var AirlineName: String = "",
    var ArriveTime: String = "",
    var ClassSeat: String = "",
    var Date: String = "",
    var From: String = "",
    var FromShort: String = "",
    var NumberSeat: Int = 0,
    var Price: Double = 0.0,
    var Passenger: String = "",
    var Seats: String = "",
    var ReservedSeats: String = "",
    var Time: String = "",
    var To: String = "",
    var ToShort: String = ""
):Parcelable

