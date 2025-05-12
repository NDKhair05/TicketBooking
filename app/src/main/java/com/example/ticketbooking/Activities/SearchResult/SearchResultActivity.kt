package com.example.ticketbooking.Activities.SearchResult

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.ViewModel.MainViewModel

class SearchResultActivity : AppCompatActivity() {
    private val viewModel = MainViewModel()
    private var from: String = ""
    private var to: String = ""
    private var numPassenger: Int = 1
    private var numAdultPassenger: Int = 1
    private var numKidPassenger: Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        from = intent.getStringExtra("from")?:""
        to = intent.getStringExtra("to")?:""
        numPassenger = intent.getIntExtra("numPassenger", 1)
        numAdultPassenger = intent.getIntExtra("numAdultPassenger", 1)
        numKidPassenger = intent.getIntExtra("numKidPassenger", 0)

        setContent {
            StatusTopBarColor()

            ItemListScreen(
                from = from,
                to = to,
                numPassenger = numPassenger,
                numAdultPassenger = numAdultPassenger,
                numKidPassenger = numKidPassenger,
                viewModel = viewModel,
                onBackClick = {finish()}
            )
        }
    }

}