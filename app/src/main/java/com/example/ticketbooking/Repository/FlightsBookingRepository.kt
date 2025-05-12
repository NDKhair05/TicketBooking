package com.example.ticketbooking.Repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class FlightsBookingRepository {
    private val database = FirebaseDatabase.getInstance().getReference("FlightsBookings")

    fun createBooking(booking: FlightsBookingModel, onResult: (Boolean) -> Unit) {
        val bookingId = database.push().key ?: return onResult(false)
        val bookingWithId = booking.copy(bookingId = bookingId)

        database.child(bookingId).setValue(bookingWithId)
            .addOnSuccessListener { onResult(true) }
            .addOnFailureListener { onResult(false) }
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