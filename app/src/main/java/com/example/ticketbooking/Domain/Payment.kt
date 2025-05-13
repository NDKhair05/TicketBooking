package com.example.ticketbooking.Domain

data class Payment(
    val id: String = "",
    val userId: String = "",
    val amount: Double = 0.0,
    val flightId: String = "",
    val status: String = "pending"
)
