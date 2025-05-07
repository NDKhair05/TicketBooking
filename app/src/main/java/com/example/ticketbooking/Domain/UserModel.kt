package com.example.ticketbooking.Domain

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserModel(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "user"
):Parcelable
