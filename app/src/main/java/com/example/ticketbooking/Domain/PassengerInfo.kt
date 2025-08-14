package com.example.ticketbooking.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class PassengerInfo(
    val fullName: String = "",
    val sex: String = "",
    val identifyCardNumber: String? = null,
    val dateOfBirth: String = "",
    val nationality: String = "",
    val seatNumber: String = ""
): Parcelable