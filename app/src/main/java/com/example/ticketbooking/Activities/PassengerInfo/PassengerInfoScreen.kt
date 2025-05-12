package com.example.ticketbooking.Activities.PassengerInfo

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ticketbooking.Activities.Dashboard.YellowTitle
import com.example.ticketbooking.Activities.PassengerInfo.TopSection
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.Activities.TicketDetail.TicketDetailActivity
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.Domain.FlightsBookingModel
import com.example.ticketbooking.Domain.PassengerInfo
import com.example.ticketbooking.R
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale
import java.util.UUID

@Composable
fun PassengerInfoScreen(
    passengerTypes: List<Boolean>, // true = người lớn, false = trẻ em
    selectedSeats: List<String>,
    flight: FlightModel,
    onBackClick: () -> Unit,
) {
    val context = LocalContext.current
    val passengerForms = remember { mutableStateListOf<PassengerInfo>() }
    val allNationalities = listOf("Afghan", "Albanian", "Algerian", "American", "Andorran", "Angolan", "Anguillan", "Antiguan and Barbudan", "Argentine", "Armenian", "Australian", "Austrian", "Azerbaijani", "Bahamian", "Bahraini", "Bangladeshi", "Barbadian", "Belarusian", "Belgian", "Belizean", "Beninese", "Bermudian", "Bhutanese", "Bolivian", "Bosnian and Herzegovinian", "Botswanan", "Brazilian", "British", "British Virgin Islander", "Bruneian", "Bulgarian", "Burkinabé", "Burmese", "Burundian", "Cambodian", "Cameroonian", "Canadian", "Cape Verdean", "Caymanian", "Central African", "Chadian", "Chilean", "Chinese", "Colombian", "Comoran", "Congolese (Congo)", "Congolese (DRC)", "Cook Islander", "Costa Rican", "Croatian", "Cuban", "Cypriot", "Czech", "Danish", "Djiboutian", "Dominican", "Dutch", "East Timorese", "Ecuadorean", "Egyptian", "Emirati", "English", "Equatorial Guinean", "Eritrean", "Estonian", "Ethiopian", "Faroese", "Fijian", "Filipino", "Finnish", "French", "Gabonese", "Gambian", "Georgian", "German", "Ghanaian", "Greek", "Grenadian", "Guatemalan", "Guinean", "Guinea-Bissauan", "Guyanese", "Haitian", "Honduran", "Hungarian", "Icelander", "Indian", "Indonesian", "Iranian", "Iraqi", "Irish", "Israeli", "Italian", "Ivorian", "Jamaican", "Japanese", "Jordanian", "Kazakhstani", "Kenyan", "Kittitian and Nevisian", "Kuwaiti", "Kyrgyz", "Laotian", "Latvian", "Lebanese", "Liberian", "Libyan", "Liechtensteiner", "Lithuanian", "Luxembourger", "Macedonian", "Malagasy", "Malawian", "Malaysian", "Maldivian", "Malian", "Maltese", "Marshallese", "Mauritanian", "Mauritian", "Mexican", "Micronesian", "Moldovan", "Monacan", "Mongolian", "Montenegrin", "Moroccan", "Mozambican", "Myanmar (Burmese)", "Namibian", "Nauruan", "Nepalese", "New Zealander", "Nicaraguan", "Nigerian", "Nigerien", "North Korean", "Norwegian", "Omani", "Pakistani", "Palauan", "Palestinian", "Panamanian", "Papua New Guinean", "Paraguayan", "Peruvian", "Polish", "Portuguese", "Puerto Rican", "Qatari", "Romanian", "Russian", "Rwandan", "Saint Lucian", "Salvadoran", "Samoan", "San Marinese", "Sao Tomean", "Saudi", "Scottish", "Senegalese", "Serbian", "Seychellois", "Sierra Leonean", "Singaporean", "Slovakian", "Slovenian", "Solomon Islander", "Somali", "South African", "South Korean", "Spanish", "Sri Lankan", "Sudanese", "Surinamese", "Swazi", "Swedish", "Swiss", "Syrian", "Taiwanese", "Tajik", "Tanzanian", "Thai", "Togolese", "Tongan", "Trinidadian and Tobagonian", "Tunisian", "Turkish", "Turkmen", "Tuvaluan", "Ugandan", "Ukrainian", "Uruguayan", "Uzbekistani", "Venezuelan", "Vietnamese", "Welsh", "Yemeni", "Zambian", "Zimbabwean")

    LaunchedEffect(passengerTypes, selectedSeats) {
        val newForms = passengerTypes.mapIndexed { index, isAdult ->
            PassengerInfo(
                fullName = "",
                identifyCardNumber = if (isAdult) "" else null,
                dateOfBirth = "",
                nationality = "",
                seatNumber = selectedSeats.getOrNull(index) ?: "",
                sex = ""
            )
        }
        passengerForms.clear()
        passengerForms.addAll(newForms)
    }

    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(colorResource(R.color.darkPurple2))
    ) {
        val (topSection, scrollSection, buttonSection) = createRefs()
        val guideline = createGuidelineFromTop(0.15f) // 10% từ trên xuống

        TopSection(
            modifier = Modifier.constrainAs(topSection) {
                top.linkTo(parent.top)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            },
            onBackClick = onBackClick
        )

        LazyColumn(
            modifier = Modifier
                .padding(top = 80.dp, bottom = 80.dp)
                .padding(horizontal = 20.dp)
                .constrainAs(scrollSection) {
                    top.linkTo(guideline)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                    bottom.linkTo(buttonSection.top)
                }
        ) {
            items(passengerForms.size) { index ->
                val passenger = passengerForms[index]
                val isAdult = passengerTypes[index]
                var nationalityExpanded by remember { mutableStateOf(false) }

                Column (
                    horizontalAlignment = Alignment.Start,
                    modifier = Modifier
                        .padding(vertical = 16.dp)
                        .background(colorResource(R.color.lightPurple).copy(alpha = 0.8f), RoundedCornerShape(16.dp) )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Passenger ${index + 1}: ${if (isAdult) "People over 16 years old" else "People under 16 years old" }",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.orange),
                        modifier = Modifier.padding(bottom = 16.dp)
                    )
                    Text("Sex *",fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.White)
                    Row (
                        horizontalArrangement = Arrangement.Start,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = passenger.sex == "Male",
                            onClick = {
                                passengerForms[index] = passenger.copy(sex = "Male")
                            },
                            colors = RadioButtonDefaults.colors(colorResource(R.color.orange))

                        )
                        Text("Male", color = Color.White)
                        Spacer(modifier = Modifier.width(8.dp))
                        RadioButton(
                            selected = passenger.sex == "Female",
                            onClick = {
                                passengerForms[index] = passenger.copy(sex = "Female")
                            },
                            colors = RadioButtonDefaults.colors(colorResource(R.color.orange))
                        )
                        Text("Female", color = Color.White)
                    }

                    CustomTextField(
                        value = passenger.fullName,
                        hint = "Full Name (ex: NGUYEN VAN A)",
                        onValueChange = {
                            passengerForms[index] = passenger.copy(fullName = it)
                        },
                        modifier = Modifier.fillMaxWidth()
                    )

                    if (isAdult) {
                        CustomTextField(
                            value = passenger.identifyCardNumber ?: "",
                            hint = "Citizen ID",
                            onValueChange = {
                                passengerForms[index] = passenger.copy(identifyCardNumber = it)
                            },
                            modifier = Modifier.fillMaxWidth()
                        )
                    }

                    Text("Birthday *",fontSize = 14.sp, fontWeight = FontWeight.Normal, color = Color.White)
                    Row(horizontalArrangement = Arrangement.Center) {
                        var day by remember { mutableStateOf("") }
                        var month by remember { mutableStateOf("") }
                        var year by remember { mutableStateOf("") }

                        CustomTextField(
                            value = day,
                            hint = "DD",
                            modifier = Modifier
                                .weight(1f)
                                .width(20.dp)
                                .padding(end = 15.dp),
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.length <= 2) {
                                    day = it
                                    passengerForms[index] = passenger.copy(dateOfBirth = "$day/$month/$year")
                                }
                            },
                        )
                        CustomTextField(
                            value = month,
                            hint = "MM",
                            modifier = Modifier
                                .weight(1f)
                                .width(20.dp)
                                .padding(end = 15.dp),

                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.length <= 2) {
                                    month = it
                                    passengerForms[index] = passenger.copy(dateOfBirth = "$day/$month/$year")
                                }
                            }
                        )
                        CustomTextField(
                            value = year,
                            hint = "YYYY",
                            modifier = Modifier
                                .weight(1f)
                                .width(20.dp),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            onValueChange = {
                                if (it.length <= 4) {
                                    year = it
                                    passengerForms[index] = passenger.copy(dateOfBirth = "$day/$month/$year")
                                }
                            }
                        )
                    }


                    Text(
                        text = "Nationality *",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Normal,
                        modifier = Modifier.padding(top = 8.dp, bottom = 4.dp)
                    )

                    Box(
                        contentAlignment = Alignment.CenterStart,
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, Color.White, RoundedCornerShape(5.dp))
                            .clickable { nationalityExpanded = true }
                            .size(55.dp)
                            .background(colorResource(R.color.lightPurple)),
                    ) {
                        Text(
                            text = passenger.nationality.ifBlank { "Select Nationality" },
                            fontSize = 14.sp,
                            color = if (passenger.nationality.isBlank()) Color.Gray else Color.White,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(start = 15.dp)
                            )
                        DropdownMenu(
                            expanded = nationalityExpanded,
                            onDismissRequest = { nationalityExpanded = false }
                        ) {
                            allNationalities.forEach { nation ->
                                DropdownMenuItem(
                                    onClick = {
                                        passengerForms[index] = passenger.copy(nationality = nation)
                                        nationalityExpanded = false
                                    },
                                    text = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            RadioButton(
                                                selected = passenger.nationality == nation,
                                                onClick = null // handled by parent
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Text(text = nation)
                                        }
                                    }
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }

        Box(
            modifier = Modifier
                .constrainAs(buttonSection) {
                    bottom.linkTo(parent.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
                .fillMaxWidth()
                .padding(top = 15.dp)
        ) {
            GradientButton(
                onClick = {
                    val isValid = passengerForms.all {
                        it.fullName.isNotBlank() &&
                                it.dateOfBirth.isNotBlank() &&
                                it.nationality.isNotBlank() &&
                                (it.identifyCardNumber?.isNotBlank() != false)
                    }
                    if (!isValid) {
                        Toast.makeText(context, "Please fill in all fields", Toast.LENGTH_SHORT)
                            .show()
                        return@GradientButton
                    }
                    val bookingDate = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(
                        Date()
                    )
                    val totalPrice = passengerForms.size * (flight.Price ?: 0.0)

                    val booking = FlightsBookingModel(
                        bookingId = UUID.randomUUID().toString(),
                        passengers = passengerForms,
                        flight = flight,
                        bookingDate = bookingDate,
                        status = "Pending",
                        totalPrice = totalPrice
                    )

                    val intent = Intent(context, TicketDetailActivity::class.java).apply {
                        putExtra("booking", booking)
                    }
                    context.startActivity(intent)
                },
                text = "Confirm"
            )
        }
    }
}

