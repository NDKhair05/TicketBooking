
package com.example.ticketbooking.Activities.Splash
import androidx.compose.ui.Modifier
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import com.example.ticketbooking.R
import com.google.accompanist.systemuicontroller.rememberSystemUiController
import androidx.compose.material.OutlinedTextField
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.background
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.Alignment
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.text.style.TextDecoration

import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ticketbooking.Activities.Authentication.RegisterActivity
import com.example.ticketbooking.Domain.UserModel
import com.example.ticketbooking.MainActivity
import com.example.ticketbooking.Utils.UserPreferences
import com.example.ticketbooking.ViewModel.AuthViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SplashScreen(onGetStartedClick = {
                startActivity(Intent(this,MainActivity:: class.java))
            })
        }
    }
}
@Composable
@Preview
fun SplashScreen(onGetStartedClick: () -> Unit = {}) {
    val viewModel: AuthViewModel = viewModel()
    val authResult by viewModel.authResult.observeAsState()

    var showLoginForm by remember { mutableStateOf(false) }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    val offsetY by animateDpAsState(
        targetValue = if (showLoginForm) 0.dp else 100.dp, // Khoảng cách trượt ban đầu
        animationSpec = tween(durationMillis = 800)
    )
    val alpha by animateFloatAsState(
        targetValue = if (showLoginForm) 1f else 0f,
        animationSpec = tween(durationMillis = 800)
    )
    val context = LocalContext.current // get context from the Composable tree
    StatusTopBarColor()
    Column(modifier = Modifier
            .fillMaxSize()
            .background(color = colorResource(R.color.darkPurple))
    ) {
                ConstraintLayout {
                val (backgroundImg, title, subtitle, startbtn, loginFormColumn) = createRefs()
                val guideline = createGuidelineFromTop(0.1f) // 10% từ trên xuống
                Image(
                    painter = painterResource(R.drawable.splash_bg),
                    contentDescription = null,
                    modifier = Modifier
                        .constrainAs(backgroundImg) {
                            top.linkTo(parent.top)
                            start.linkTo(parent.start)
                        }
                        .fillMaxHeight()
                )

                val styledText = buildAnnotatedString {
                    append("Discover your\n Dream")
                    withStyle(style = SpanStyle(fontWeight = FontWeight.Bold)) {
                        append(" Flight")
                    }
                    append("\nEasily")
                }

                if (showLoginForm == false) {
                    Text(
                        text = styledText,
                        fontSize = 53.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White,
                        modifier = Modifier
                            .padding(top = 32.dp)
                            .padding(horizontal = 16.dp)
                            .constrainAs(title) {
                                top.linkTo(parent.top)
                                start.linkTo(parent.start)
                            }
                    )

                    Text(
                        text = stringResource(R.string.subtitle_splash),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = colorResource(R.color.orange),
                        modifier = Modifier
                            .padding(top = 32.dp, start = 16.dp)
                            .constrainAs(subtitle) {
                                top.linkTo(title.bottom)
                                start.linkTo(title.start)
                            }
                    )
                }

                // Column chứa form đăng nhập (với hiệu ứng trượt và độ mờ)
                Column(
                    modifier = Modifier
                        .constrainAs(loginFormColumn) {
                            top.linkTo(guideline) // Đặt ban đầu ở phía trên nút
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                        .padding(16.dp)
                        .offset(y = offsetY)



                ) {
                    if (showLoginForm) {
                        Text(
                            text = stringResource(R.string.sign_in_title),
                            fontSize = 50.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = colorResource(R.color.orange),
                            modifier = Modifier
                                .padding(top = 15.dp, bottom = 20.dp)
                                .align(Alignment.CenterHorizontally)
                        )
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .padding(vertical = 8.dp)
                                .background(colorResource(R.color.lightPurple).copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                                .padding(16.dp)
                        ) {
                            OutlinedTextField(
                                value = email,
                                onValueChange = { email = it },
                                label = { Text("Email") },
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                            )
                            var passwordVisible by remember { mutableStateOf(false) }
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                label = { Text("Password") },
                                visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                trailingIcon = {
                                    val image = if (passwordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                                    IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                        Icon(painter = painterResource(id = image), contentDescription = null)
                                    }
                                },
                                modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                                keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                            )

                            Text(
                                text = stringResource(R.string.sign_up_link),
                                fontSize = 12.sp,
                                color = colorResource(R.color.orange),
                                modifier = Modifier
                                    .padding(top = 8.dp)
                                    .clickable {
                                        // Create an Intent to navigate to the registration screen.
                                        val intent = Intent(context, RegisterActivity::class.java)
                                        context.startActivity(intent)
                                    },
                                textDecoration = TextDecoration.Underline
                            )
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .padding(bottom = 32.dp)
                        .constrainAs(startbtn) {
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        },
                    contentAlignment = Alignment.Center // Để căn giữa nút và text
                ) {
                    GradientButton(
                        onClick = {
                            if (showLoginForm) {
                                if(email.isBlank() || password.isBlank()) {
                                    Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                                } else {
                                    viewModel.login(email, password)
                                }
                            } else {
                                // Hiển thị form đăng nhập khi nút "Log in" được nhấn
                                showLoginForm = true
                            }
                        },
                        text = if (!showLoginForm) stringResource(R.string.sign_in_title) else "Continue",
                    )
                    authResult?.let { (success, message) ->
                        if (success) {
                            val uid = FirebaseAuth.getInstance().currentUser?.uid ?: return@let
                            val ref = FirebaseDatabase.getInstance().getReference("Users").child(uid)

                            ref.addListenerForSingleValueEvent(object : ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    val user = snapshot.getValue(UserModel::class.java)
                                    if (user != null) {
                                        val userPreferences = UserPreferences(context)
                                        userPreferences.saveUser(user) // Lưu toàn bộ dữ liệu người dùng

                                        Toast.makeText(context, "Login successful", Toast.LENGTH_SHORT).show()
                                        onGetStartedClick()
                                    } else {
                                        Toast.makeText(context, "User data not found", Toast.LENGTH_SHORT).show()
                                    }
                                }

                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(context, "Failed to load user data: ${error.message}", Toast.LENGTH_SHORT).show()
                                }
                            })

                        } else {
                            Toast.makeText(context, message ?: "Login failed", Toast.LENGTH_SHORT).show()
                        }
                        viewModel.resetAuthResult()
                    }
            }
        }
    }
}

@Composable
fun StatusTopBarColor() {
    val systemUiController = rememberSystemUiController()

    SideEffect {
        systemUiController.setStatusBarColor(
            color = Color.Transparent,
            darkIcons = false
        )
    }
}

