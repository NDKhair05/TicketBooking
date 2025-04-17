package com.example.ticketbooking.navigation

import com.example.ticketbooking.Domain.FlightModel
import com.google.gson.Gson

sealed class Screen(val route: String) {

    object Payment : Screen("payment")
    object BookingConfirmation : Screen("booking_confirmation")
    // Add other routes as needed
    object TicketDetail : Screen("ticket_detail/{flight}") {
        fun createRoute(flight: FlightModel): String {
            val flightJson = Gson().toJson(flight)
            return "ticket_detail/$flightJson"
        }

        const val routeWithArgs = "ticket_detail/{flight}"
    }
}