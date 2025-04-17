package com.example.ticketbooking.ViewModel

import androidx.lifecycle.LiveData
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.Domain.LocationModel
import com.example.ticketbooking.Repository.MainRepository

class MainViewModel {
    private val repository = MainRepository()

    fun loadLocations(): LiveData<MutableList<LocationModel>> {
        return repository.loadLocation()
    }

    fun loadFiltered(from: String, to: String) :
            LiveData<MutableList<FlightModel>> {
        return repository.loadFiltered(from, to)
    }
}

