package com.example.ticketbooking.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.GenericTypeIndicator
import com.google.firebase.database.ValueEventListener

class FlightsBookingRepository {
    private val database = FirebaseDatabase.getInstance().getReference("FlightsBookings")
    private val bookingsRef = FirebaseDatabase.getInstance().getReference("FlightsBookings")
    private val flightsRef = FirebaseDatabase.getInstance().getReference("Flights")

    fun createBooking(booking: FlightsBookingModel, onResult: (Boolean) -> Unit) {
        val bookingId = bookingsRef.push().key ?: return onResult(false)
        val bookingWithId = booking.copy(bookingId = bookingId)

        bookingsRef.child(bookingId).setValue(bookingWithId)
            .addOnSuccessListener {
                updateReservedSeats(bookingWithId)
                onResult(true)
            }
            .addOnFailureListener {
                onResult(false)
            }
    }

    private fun updateReservedSeats(booking: FlightsBookingModel) {
        val flightId = booking.flight.flightId
        if (flightId.isBlank()) return

        val selectedSeats = booking.passengers.map { it.seatNumber }

        flightsRef.child(flightId).get().addOnSuccessListener { snapshot ->
            val currentReservedSeats = snapshot.child("reservedSeats").getValue(String::class.java)
            val existingSeats = currentReservedSeats?.split(",")?.filter { it.isNotBlank() }?.toMutableList() ?: mutableListOf()

            val updatedSeats = (existingSeats + selectedSeats).distinct()
            flightsRef.child(flightId).child("reservedSeats").setValue(updatedSeats.joinToString(","))
        }
    }

    fun getBookingsByUser(userId: String): LiveData<List<FlightsBookingModel>> {
        val bookingsLiveData = MutableLiveData<List<FlightsBookingModel>>()

        database.orderByChild("userId").equalTo(userId)
            .addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val bookings = mutableListOf<FlightsBookingModel>()
                    for (child in snapshot.children) {
                        val booking = child.getValue(FlightsBookingModel::class.java)
                        booking?.let { bookings.add(it) }
                    }
                    bookingsLiveData.value = bookings
                }

                override fun onCancelled(error: DatabaseError) {
                    bookingsLiveData.value = emptyList()
                }
            })

        return bookingsLiveData
    }
}