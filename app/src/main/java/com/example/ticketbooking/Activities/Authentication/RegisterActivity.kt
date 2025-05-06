package com.example.ticketbooking.Activities.Authentication

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Activities.Dashboard.DashboardActivity
import com.example.ticketbooking.Activities.Register.RegisterScreen
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.R

class RegisterActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            StatusTopBarColor()
            RegisterScreen(
                onRegisterSuccess = {
                    startActivity(Intent(this, DashboardActivity::class.java))
                    finish()
                }
            )
        }
    }
}