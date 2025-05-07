package com.example.ticketbooking.Domain

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserModel(
    val uid: String = "",
    val fullName: String = "",
    val email: String = "",
    val role: String = "user"
)
