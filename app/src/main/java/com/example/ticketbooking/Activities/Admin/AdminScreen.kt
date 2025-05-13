package com.example.ticketbooking.Screens.Admin

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        Text(
            text = "Welcome, $adminName",
            fontSize = 24.sp,
            fontWeight = FontWeight.Bold,
            color = Color.White
        )

        AdminActionButton("📋 Duyệt vé", onApproveTickets)
        AdminActionButton("👤 Quản lý người dùng", onManageUsers)
        AdminActionButton("✈️ Thêm chuyến bay", onAddFlight)
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
