package com.example.ticketbooking.Activities.Admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ticketbooking.Domain.UserModel
import com.example.ticketbooking.ui.theme.TicketBookingTheme
import com.google.firebase.database.*

class UserListActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TicketBookingTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val database = FirebaseDatabase.getInstance().getReference("Users")
                    UserListScreen(database, onBack = { finish() })
                }
            }
        }
    }
}

@Composable
fun UserListScreen(database: DatabaseReference, onBack: () -> Unit) {
    var userList by remember { mutableStateOf(listOf<UserModel>()) }
    var searchQuery by remember { mutableStateOf("") }
    var selectedRole by remember { mutableStateOf("Tất cả") }
    var editUser by remember { mutableStateOf<UserModel?>(null) }
    var userToDelete by remember { mutableStateOf<UserModel?>(null) }

    val roles = listOf("Tất cả", "user", "admin")

    LaunchedEffect(Unit) {
        database.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val users = mutableListOf<UserModel>()
                for (userSnapshot in snapshot.children) {
                    val user = userSnapshot.getValue(UserModel::class.java)
                    user?.let { users.add(it) }
                }
                userList = users
            }

            override fun onCancelled(error: DatabaseError) {}
        })
    }

    val filteredList = userList.filter {
        (selectedRole == "Tất cả" || it.role == selectedRole) &&
                (it.fullName.contains(searchQuery, ignoreCase = true) ||
                        it.email.contains(searchQuery, ignoreCase = true))
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFFF7F7F7))
            .padding(16.dp)
    ) {
        // Top bar with Back button
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.fillMaxWidth()
        ) {
            IconButton(onClick = onBack) {
                Icon(Icons.Default.ArrowBack, contentDescription = "Back")
            }
            Text(
                text = "📋 Danh sách người dùng",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2B2D42)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Search & Filter
        Row(
            Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            TextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                label = { Text("Tìm kiếm...") },
                modifier = Modifier.weight(1f).padding(end = 8.dp)
            )

            var expanded by remember { mutableStateOf(false) }

            Box {
                Button(onClick = { expanded = true }) {
                    Text(selectedRole)
                }

                DropdownMenu(expanded = expanded, onDismissRequest = { expanded = false }) {
                    roles.forEach { role ->
                        DropdownMenuItem(onClick = {
                            selectedRole = role
                            expanded = false
                        }) {
                            Text(role)
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))
        Divider()

        LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
            items(filteredList) { user ->
                UserRow(user,
                    onEdit = { editUser = it },
                    onDelete = { userToDelete = it }
                )
            }
        }

        editUser?.let { user ->
            EditUserDialog(user = user,
                onDismiss = { editUser = null },
                onSave = { updatedUser ->
                    database.child(updatedUser.uid).setValue(updatedUser)
                    editUser = null
                }
            )
        }

        userToDelete?.let { user ->
            // Confirmation Dialog before Deleting
            AlertDialog(
                onDismissRequest = { userToDelete = null },
                title = { Text("Xác nhận xoá") },
                text = { Text("Bạn có chắc muốn xoá người dùng ${user.fullName}?") },
                confirmButton = {
                    Button(onClick = {
                        database.child(user.uid).removeValue()
                        userToDelete = null
                    }) {
                        Text("Xoá")
                    }
                },
                dismissButton = {
                    TextButton(onClick = { userToDelete = null }) {
                        Text("Hủy")
                    }
                }
            )
        }
    }
}

@Composable
fun UserRow(user: UserModel, onEdit: (UserModel) -> Unit, onDelete: (UserModel) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 4.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Column {
            Text(text = user.fullName, fontSize = 14.sp)
            Text(text = user.email, fontSize = 12.sp, color = Color.Gray)
        }
        Row {
            Text(text = user.role, fontSize = 14.sp, modifier = Modifier.padding(end = 12.dp))
            Text("✏️", modifier = Modifier.clickable { onEdit(user) })
            Spacer(modifier = Modifier.width(8.dp))
            Text("❌", color = Color.Red, modifier = Modifier.clickable { onDelete(user) })
        }
    }
}

@Composable
fun EditUserDialog(user: UserModel, onDismiss: () -> Unit, onSave: (UserModel) -> Unit) {
    var name by remember { mutableStateOf(user.fullName) }
    var email by remember { mutableStateOf(user.email) }
    var role by remember { mutableStateOf(user.role) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Chỉnh sửa người dùng") },
        text = {
            Column {
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Họ tên") })
                OutlinedTextField(value = email, onValueChange = { email = it }, label = { Text("Email") })
                OutlinedTextField(value = role, onValueChange = { role = it }, label = { Text("Vai trò") })
            }
        },
        confirmButton = {
            Button(onClick = {
                onSave(user.copy(fullName = name, email = email, role = role))
            }) {
                Text("Lưu")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Hủy")
            }
        }
    )
}
