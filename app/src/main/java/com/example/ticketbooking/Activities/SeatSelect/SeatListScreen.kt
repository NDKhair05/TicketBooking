package com.example.ticketbooking.Activities.SeatSelect

import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ticketbooking.Domain.FlightModel
import com.example.ticketbooking.R

enum class SeatStatus {
    AVAILABLE,
    SELECTED,
    UNAVAILABLE,
    EMPTY
}

data class Seat(
    var status: SeatStatus,
    var name: String
)
@Composable
fun SeatListScreen(
    flight: FlightModel,
    onBackClick: () -> Unit,
    onConfirm: (FlightModel) -> Unit
) {
    val context = LocalContext.current

    var seatList by remember { mutableStateOf(listOf<Seat>()) }

    val selectedSeatNames = remember { mutableListOf<String>() }

    var seatCount by remember { mutableStateOf(0) }
    var totalPrice by remember { mutableStateOf(0.0) }


    LaunchedEffect(flight) {
        seatList = generateSeatList(flight)
        seatCount = selectedSeatNames.size
        totalPrice = seatCount * flight.Price
    }

    fun updatePriceAndCount() {
        seatCount = selectedSeatNames.size
        totalPrice = seatCount * flight.Price
    }

    fun onSeatClick(index: Int) {
        val seat = seatList[index]
        val updatedSeat = seat.copy(
            status = when (seat.status) {
                SeatStatus.AVAILABLE -> SeatStatus.SELECTED
                SeatStatus.SELECTED -> SeatStatus.AVAILABLE
                else -> seat.status
            }
        )
        val updatedSeatList = seatList.toMutableList()
        updatedSeatList[index] = updatedSeat
        seatList = updatedSeatList

        if (updatedSeat.status == SeatStatus.SELECTED) {
            selectedSeatNames.add(updatedSeat.name)
        } else if (updatedSeat.status == SeatStatus.AVAILABLE) {
            selectedSeatNames.remove(updatedSeat.name)
        }

        updatePriceAndCount()
    }


    ConstraintLayout(
        modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkPurple2))
    ) {
        val (topSection, middleSection, bottomSection) = createRefs()

        // Phần đầu màn hình
        TopSection(
            modifier = Modifier
                .constrainAs(topSection) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
            onBackClick = onBackClick
        )

        // Phần giữa màn hình
        ConstraintLayout(
            modifier = Modifier
                .padding(top = 100.dp)
                .constrainAs(middleSection) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {
            val (airplane, seatGrid) = createRefs()
            Image(
                painter = painterResource(R.drawable.airple_seat),
                contentDescription = null,
                modifier = Modifier.constrainAs(airplane) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            LazyVerticalGrid(
                columns = GridCells.Fixed(7),
                modifier = Modifier
                    .padding(top = 200.dp)
                    .padding(horizontal = 64.dp)
                    .constrainAs(seatGrid) {
                        top.linkTo(parent.top)
                        start.linkTo(airplane.start)
                        end.linkTo(airplane.end)
                    }
            ) {
                items(seatList.size) { index ->
                    val seat = seatList[index]

                    SeatItem(
                        seat = seat,
                        onSeatClick = { onSeatClick(index) }
                    )
                }
            }
        }

        // Phần dưới màn hình
        BottomSection(
            seatCount = seatCount,
            selectedSeats = selectedSeatNames.joinToString(","),
            totalPrice = totalPrice,
            onConfirmClick = {
                if (seatCount > 0) {
                    flight.Seats = selectedSeatNames.joinToString(",")
                    flight.TotalPrice = totalPrice
                    onConfirm(flight)
                } else {
                    Toast.makeText(context, "Please select your seat", Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier.constrainAs(bottomSection) {
                bottom.linkTo(parent.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            }
        )
    }
}



fun  generateSeatList(flight: FlightModel): List<Seat> {
    val seatList = mutableListOf<Seat>()
    val numberSeat = flight.NumberSeat + (flight.NumberSeat / 7) + 1
    val seatAlphabetMap = mapOf(
        0 to "A",
        1 to "B",
        2 to "C",
        4 to "D",
        5 to "E",
        6 to "F",
        )

    var row = 0
    for(i in 0 until numberSeat) {
        if (i%7 == 0) {
           row++
        }
        if (i%7 == 3) {
            seatList.add(Seat(SeatStatus.EMPTY, row.toString()))
        } else {
            val seatName = seatAlphabetMap[i % 7] + row
            val seatStatus = if (flight.ReservedSeats.contains(seatName)) {
                SeatStatus.UNAVAILABLE
            } else {
                SeatStatus.AVAILABLE
            }
            seatList.add(Seat(seatStatus, seatName))
        }
    }
    return seatList
}
