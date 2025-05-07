package com.example.ticketbooking.Utils

import android.content.Context
import android.content.SharedPreferences
import com.example.ticketbooking.Domain.UserModel
import com.google.gson.Gson

class UserPreferences(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    private val gson = Gson()

    fun saveUser(user: UserModel) {
        val json = gson.toJson(user)
        prefs.edit().putString("user_data", json).apply()
    }

    fun getUser(): UserModel? {
        val json = prefs.getString("user_data", null)
        return json?.let {
            gson.fromJson(it, UserModel::class.java)
        }
    }

    fun clearUser() {
        prefs.edit().remove("user_data").apply()
    }
}