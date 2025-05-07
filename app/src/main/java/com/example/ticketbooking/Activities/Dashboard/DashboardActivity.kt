package com.example.ticketbooking.Activities.Dashboard

import android.content.Intent
import android.icu.text.CaseMap.Title
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivities
import androidx.core.content.ContextCompat.startActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.ticketbooking.Activities.SearchResult.SearchResultActivity
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.Domain.LocationModel
import com.example.ticketbooking.R
import com.example.ticketbooking.Utils.UserPreferences
import com.example.ticketbooking.ViewModel.MainViewModel

class DashboardActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MainScreen()
        }
    }
}

@Composable
@Preview
fun MainScreen() {
    val locations = remember { mutableStateListOf<LocationModel>() }
    val viewModel = MainViewModel()
    var showLocationLoading by remember { mutableStateOf(true) }
    var from: String = ""
    var to: String = ""
    var classes: String = ""
    var adultPassenger:String = ""
    var childrenPassenger:String = ""
    val context = LocalContext.current

    val userPreferences = UserPreferences(context)
    val currentUser = userPreferences.getUser()

    StatusTopBarColor()

    LaunchedEffect(Unit) {
        viewModel.loadLocations().observeForever {result->
            locations.clear()
            locations.addAll(result)
            showLocationLoading = false
        }
    }

    Scaffold(
        bottomBar = { MyBottomBar() }
        ) {paddingValues ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(color = colorResource(R.color.darkPurple2))
                .padding(paddingValues = paddingValues)
        ) {
            item { currentUser?.let {
                val fullName = it.fullName
                TopBar(fullName)
            } }
            item {
                Column(
                    modifier = Modifier
                        .padding(32.dp)
                        .background(
                            color = colorResource(R.color.darkPurple),
                            shape = RoundedCornerShape(20.dp)
                        )
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 24.dp)
                ) {
                    //the From section
                    YellowTitle("From")
                    val locationNames:List<String> = locations.map{it.Name}

                    DropDownList(
                        items = locationNames,
                        loadingIcon = painterResource(R.drawable.from_ic),
                        hint = "Select Origin",
                        showLocationLoading = showLocationLoading
                    ) {
                        selectedItem ->
                        from = selectedItem
                    }


                    Spacer(modifier = Modifier.height(16.dp))

                    //the To section
                    YellowTitle("To")

                    DropDownList(
                        items = locationNames,
                        loadingIcon = painterResource(R.drawable.to_ic),
                        hint = "Select Destination",
                        showLocationLoading = showLocationLoading
                    ) {
                        selectedItem ->
                        to = selectedItem
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //passenger counter
                    YellowTitle(text = "Passengers")
                    Row (modifier = Modifier.fillMaxWidth()) {
                        PassengerCounter(
                            title = "Adult",
                            modifier = Modifier.weight(1f),
                            onItemSelected = { adultPassenger = it }
                        )

                        Spacer(modifier = Modifier.width(16.dp))

                        PassengerCounter(
                            title = "Child",
                            modifier = Modifier.weight(1f),
                            onItemSelected = { childrenPassenger = it }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    //the Date Picker Section
                    Row {
                        YellowTitle("Departure Date", Modifier.weight(1f))
                        Spacer(modifier = Modifier.width(16.dp))
                        YellowTitle("Return Date", Modifier.weight(1f))
                    }
                    DatePickerScreen(Modifier.weight(1f))

                    Spacer(modifier = Modifier.height(16.dp))

                    //the Class section
                    YellowTitle("Class")
                    val classItems = listOf("Business class", "First class", "Economy class")

                    DropDownList(
                        items = classItems,
                        loadingIcon = painterResource(R.drawable.seat_black_ic),
                        hint = "Select CLass",
                        showLocationLoading = showLocationLoading
                    ) {
                        selectedItem ->
                        classes = selectedItem
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    GradientButton(
                        onClick = {
                            val intent = Intent(context, SearchResultActivity::class.java).apply {
                                putExtra("from", from)
                                putExtra("to", to)
                                putExtra("numPassenger", adultPassenger+childrenPassenger)
                            }
                            startActivity(context, intent, null)
                        },
                        text = "Search"
                    )
                }
            }
        }
    }
}

@Composable
fun YellowTitle(text: String, modifier: Modifier = Modifier) {
    Text(
        text = text,
        fontWeight = FontWeight.SemiBold,
        color = colorResource(R.color.orange),
        modifier = modifier
    )
}