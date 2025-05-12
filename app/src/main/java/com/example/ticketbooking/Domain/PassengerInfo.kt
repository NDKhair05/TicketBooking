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
    val email: String = "",
    val phoneNumber: String = "",
    val seatNumber: String = ""
): Parcelable