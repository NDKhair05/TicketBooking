package com.example.ticketbooking.Activities.Profile

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat.startActivity
import com.example.ticketbooking.Activities.Dashboard.MyBottomBar
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.Activities.Splash.SplashActivity
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.Domain.UserModel
import com.example.ticketbooking.R
import com.example.ticketbooking.Utils.UserPreferences

@Composable
@Preview
fun ProfileScreen(
    onLogout: () -> Unit = {} // callback khi đăng xuất
) {
    val context = LocalContext.current
    val userPreferences = UserPreferences(context)
    val user: UserModel?
    user = userPreferences.getUser()

    Column (
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.darkPurple2))
                .padding(24.dp)
        ) {
            Text(
                text = "Profile",
                fontSize = 32.sp,
                fontWeight = FontWeight.Bold,
                color = colorResource(R.color.orange),
                modifier = Modifier.padding(bottom = 24.dp)
            )

            user?.let {
                ProfileItem(title = "Full Name", value = it.fullName)
                ProfileItem(title = "Email", value = it.email)
            }

            Spacer(modifier = Modifier.height(32.dp))

            GradientButton(
                onClick = {
                    userPreferences.clearUser()
                    onLogout()
                },
                text = "Logout"
            )
        }
    }





@Composable
fun ProfileItem(title: String, value: String) {
    Column(modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = title,
            fontWeight = FontWeight.SemiBold,
            color = colorResource(R.color.orange),
            fontSize = 16.sp
        )
        Text(
            text = value,
            color = Color.White,
            fontSize = 14.sp
        )
    }
}
