package com.example.ticketbooking.ViewModel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.ticketbooking.Repository.AuthRepository
import com.google.firebase.auth.FirebaseUser

class AuthViewModel : ViewModel() {
    private val repository = AuthRepository()

    private val _authResult = MutableLiveData<Pair<Boolean, String?>>()
    val authResult: LiveData<Pair<Boolean, String?>> get() = _authResult

    fun register(fullName: String, email: String, password: String) {
        repository.registerUser(fullName, email, password) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }

    fun login(email: String, password: String) {
        repository.loginUser(email, password) { success, message ->
            _authResult.value = Pair(success, message)
        }
    }

    fun getCurrentUser(): FirebaseUser? = repository.getCurrentUser()
}
