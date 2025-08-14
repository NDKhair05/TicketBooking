package com.example.ticketbooking.Activities.Payment

import android.content.Intent
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.Activities.Dashboard.DashboardScreen
import com.example.ticketbooking.Activities.Splash.SplashActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.ViewModel.FlightBookingViewModel

class PaymentActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val booking = intent.getParcelableExtra<FlightsBookingModel>("booking")
        val flightBookingViewModel = FlightBookingViewModel()

        setContent {
            StatusTopBarColor()
            if (booking != null) {
                PaymentScreen(
                    booking = booking,
                    onPaymentSuccess = {
                        // Trả về kết quả hoặc chuyển màn khác nếu cần
                        startActivity(Intent(this, MainActivity()::class.java))
                        finish()
                    },
                    onCancel = {
                        finish()
                    },
                    bookingId = booking.bookingId,
                    flightBookingViewModel = flightBookingViewModel
                )
            }
        }
    }
}
