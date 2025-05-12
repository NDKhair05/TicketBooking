package com.example.ticketbooking.ViewModel

import androidx.lifecycle.LiveData
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.Repository.FlightsBookingRepository

class FlightBookingViewModel {
    private val repository = FlightsBookingRepository()

    fun createBooking(booking: FlightsBookingModel, onResult: (Boolean) -> Unit) {
        repository.createBooking(booking, onResult)
    }

    fun getUserBookings(userId: String): LiveData<List<FlightsBookingModel>> {
        return repository.getBookingsByUser(userId)
    }
}