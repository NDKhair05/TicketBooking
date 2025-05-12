package com.example.ticketbooking.Activities.Dashboard

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.LocationModel
import com.example.ticketbooking.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DropDownList(
    locations: List<LocationModel>,
    loadingIcon: Painter,
    hint: String = "",
    showLocationLoading: Boolean,
    onItemSelected: (LocationModel) -> Unit
) {
    var searchText by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    var filteredLocations by remember { mutableStateOf<List<LocationModel>>(emptyList()) }
    val keyboardController = LocalSoftwareKeyboardController.current

    Column {
        if (showLocationLoading) {
            Box(
                modifier = Modifier
                    .padding(top = 8.dp)
                    .fillMaxWidth()
                    .background(
                        colorResource(R.color.lightPurple),
                        shape = RoundedCornerShape(10.dp)
                    )
                    .height(55.dp),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            OutlinedTextField(
                value = searchText,
                onValueChange = { searchText = it },
                singleLine = true,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp)),
                placeholder = {
                    Text(
                        text = hint,
                        color = Color.Black,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                },
                leadingIcon = {
                    Image(painter = loadingIcon, contentDescription = null)
                },
                textStyle = TextStyle(
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                ),
                colors = TextFieldDefaults.outlinedTextFieldColors(
                    focusedBorderColor = Color.Transparent,
                    unfocusedBorderColor = Color.Transparent,
                    containerColor = colorResource(R.color.lightPurple)
                ),
                keyboardOptions = KeyboardOptions.Default.copy(
                    imeAction = ImeAction.Done
                ),
                keyboardActions = KeyboardActions(
                    onDone = {
                        keyboardController?.hide()
                        filteredLocations = locations.filter {
                            it.Name.contains(searchText, ignoreCase = true) ||
                                    it.Airport.contains(searchText, ignoreCase = true) ||
                                    it.IATA.contains(searchText, ignoreCase = true)
                        }
                        expanded = true
                    }
                )
            )

            DropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                if (filteredLocations.isEmpty()) {
                    DropdownMenuItem(
                        text = { Text("No results found") },
                        onClick = { expanded = false }
                    )
                } else {
                    filteredLocations.forEach { location ->
                        DropdownMenuItem(
                            text = {
                                Column {
                                    Text(location.Name, fontWeight = FontWeight.Bold)
                                    Text("${location.Airport} - ${location.IATA}", fontSize = 12.sp, color = Color.Gray)
                                }
                            },
                            onClick = {
                                searchText = location.Name
                                expanded = false
                                onItemSelected(location)
                            }
                        )
                    }
                }
            }
        }
    }
}
