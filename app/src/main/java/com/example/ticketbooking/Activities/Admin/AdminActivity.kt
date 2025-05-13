package com.example.ticketbooking.Activities.Admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.PopupProperties
import com.example.ticketbooking.R
import com.example.ticketbooking.Utils.UserPreferences
import com.example.ticketbooking.ui.theme.TicketBookingTheme
import android.content.Intent
import com.example.ticketbooking.Activities.Admin.UserListActivity

class AdminActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val userPreferences = UserPreferences(this)
        val adminName = userPreferences.getUser()?.fullName ?: "Admin"

        setContent {
            TicketBookingTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    AdminScreen(
                        adminName = adminName,
                        onApproveTickets = {
                            val intent = Intent(this, ApproveTicketsScreen::class.java)
                            startActivity(intent)

                        },
                        onManageUsers = {
                            val intent = Intent(this, UserListActivity::class.java)
                            startActivity(intent)
                        },
                        onAddFlight = {

                        }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminScreen(
    adminName: String,
    onApproveTickets: () -> Unit,
    onManageUsers: () -> Unit,
    onAddFlight: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF2B2D42))
            .padding(horizontal = 24.dp, vertical = 24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceBetween // Thay đổi để có khoảng cách giữa các phần
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally, verticalArrangement = Arrangement.spacedBy(24.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                // Avatar
                androidx.compose.foundation.Image(
                    painter = painterResource(id = R.drawable.admin1), // Thay thế bằng ID ảnh avatar của bạn
                    contentDescription = "Admin Avatar",
                    modifier = Modifier
                        .size(60.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.width(16.dp))
                // Tên người dùng
                Text(
                    text = adminName,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )
            }

            AdminActionButton("📋 Duyệt vé", onApproveTickets)
            AdminActionButton("👤 Quản lý người dùng", onManageUsers)
            AdminActionButton("✈️ Thêm chuyến bay", onAddFlight)
        }

        // Thanh điều hướng dưới cùng
        BottomNavigationBar()
    }
}

@Composable
fun AdminActionButton(text: String, onClick: () -> Unit) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(60.dp),
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(backgroundColor = Color(0xFFFF6F00))
    ) {
        Text(text = text, fontSize = 18.sp, color = Color.White)
    }
}

@Composable
fun BottomNavigationBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color(0xFF3A3D57)) // Màu nền cho thanh điều hướng
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = { /* TODO: Handle home click */ }) {
            Icon(Icons.Filled.Home, contentDescription = "Trang chủ", tint = Color(0xFFFFD700)) // Màu vàng
        }
        IconButton(onClick = { /* TODO: Handle search click */ }) {
            Icon(Icons.Filled.Search, contentDescription = "Tìm kiếm", tint = Color(0xFFFFD700)) // Màu vàng
        }
        IconButton(onClick = { /* TODO: Handle bookmark click */ }) {
            Icon(Icons.Filled.Star, contentDescription = "Đã lưu", tint = Color(0xFFFFD700)) // Màu vàng
        }
        IconButton(onClick = { /* TODO: Handle profile click */ }) {
            Icon(Icons.Filled.Person, contentDescription = "Hồ sơ", tint = Color(0xFFFFD700)) // Màu vàng
        }
    }
}