package com.example.ticketbooking.Activities.Payment

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor

class PaymentActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StatusTopBarColor()

            PaymentScreen(
                onPaymentSuccess = {
                    // Trả về kết quả hoặc chuyển màn khác nếu cần
                    finish()
                },
                onCancel = {
                    finish()
                }
            )
        }
    }
}
