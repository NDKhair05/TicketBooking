package com.example.ticketbooking

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.WindowCompat
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.ticketbooking.Activities.Dashboard.DashboardScreen
import com.example.ticketbooking.Activities.Dashboard.MyBottomBar
import com.example.ticketbooking.Activities.Profile.ProfileScreen
import com.example.ticketbooking.Activities.Splash.SplashActivity
import com.example.ticketbooking.Utils.UserPreferences
import com.example.ticketbooking.ui.theme.TicketBookingTheme

class MainActivity : ComponentActivity() {
    @SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val userPreferences = UserPreferences(this)
        if (userPreferences.getUser() == null) {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
            return
        }
        WindowCompat.setDecorFitsSystemWindows(window, false)
        window.navigationBarColor = ContextCompat.getColor(this, R.color.purple)

        setContent {
            Box (
                modifier = Modifier
                    .fillMaxSize()
                    .background(colorResource(R.color.darkPurple2))
            ){
                val navController = rememberNavController()
                var selectedItem by rememberSaveable { mutableStateOf(BottomNavScreen.Home.name) }

                Scaffold(
                    bottomBar = {
                        MyBottomBar(navController, selectedItem) { selectedItem = it }
                    },
                    modifier = Modifier
                        .fillMaxSize()
                ) {
                    MainNavGraph(navController) {
                        // khi logout từ ProfileScreen
                        startActivity(Intent(this@MainActivity, SplashActivity::class.java))
                        finish()
                    }
                }
            }
        }
    }

    enum class BottomNavScreen(val label: String) {
        Home("Home"),
        Cart("Cart"),
        Favorite("Favorite"),
        Order("Order")
    }

    @Composable
    fun MainNavGraph(navController: NavHostController, onLogout: () -> Unit) {
        NavHost(navController, startDestination = BottomNavScreen.Home.name) {
            composable(BottomNavScreen.Home.name) { DashboardScreen() }
            composable(BottomNavScreen.Order.name) { ProfileScreen(onLogout) }
            // Thêm Cart, Favorite nếu có
        }
    }
}
