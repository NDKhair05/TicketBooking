package com.example.ticketbooking.Activities.Dashboard

import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.BottomAppBar
import androidx.compose.material.BottomNavigationItem
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBarItem
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat.startActivity
import androidx.navigation.NavController
import com.example.ticketbooking.R

data class BottomMenuItem (
    val label: String,
    val icon: Painter
)

@Composable
fun prepareButtonMenu(): List<BottomMenuItem> {
    return listOf(
        BottomMenuItem(label = "Home", icon = painterResource(R.drawable.bottom_btn1)),
        BottomMenuItem(label = "Cart", icon = painterResource(R.drawable.bottom_btn2)),
        BottomMenuItem(label = "Favorite", icon = painterResource(R.drawable.bottom_btn3)),
        BottomMenuItem(label = "Order", icon = painterResource(R.drawable.bottom_btn4))
        )
}

@Composable
fun MyBottomBar(navController: NavController, selectedItem: String, onItemSelected: (String) -> Unit) {
    val bottomMenuItemsList = prepareButtonMenu()

    BottomAppBar(
        backgroundColor = colorResource(R.color.purple),
        elevation = 3.dp,
        modifier = Modifier
            .fillMaxWidth()
            .navigationBarsPadding()
    ) {
        bottomMenuItemsList.forEach { item ->
            BottomNavigationItem(
                selected = (selectedItem == item.label),
                onClick = {
                    onItemSelected(item.label)
                    navController.navigate(item.label) {
                        launchSingleTop = true
                        restoreState = true
                        popUpTo(navController.graph.startDestinationId) { saveState = true }
                    }
                },
                icon = {
                    Icon(
                        painter = item.icon,
                        contentDescription = null,
                        tint = colorResource(R.color.orange),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .size(20.dp)
                    )
                }
            )
        }
    }
}