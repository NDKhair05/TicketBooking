package com.example.ticketbooking.Activities.PassengerInfo

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.PassengerInfo
import com.example.ticketbooking.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomTextField(value: String, hint: String, onValueChange: (String) -> Unit, keyboardOptions: KeyboardOptions = KeyboardOptions(), modifier: Modifier = Modifier) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        keyboardOptions = keyboardOptions,
        label = {
            Text(
                text = hint,
                color = Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium
            )
        },
        singleLine = true,
        modifier = modifier
            .padding(vertical = 4.dp),
        colors = TextFieldDefaults.outlinedTextFieldColors(
            containerColor = colorResource(R.color.lightPurple),
            unfocusedBorderColor = Color.White,
            focusedBorderColor = Color.Yellow,
        )
    )
}
