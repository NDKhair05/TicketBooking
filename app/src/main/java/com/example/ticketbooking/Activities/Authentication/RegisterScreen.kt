package com.example.ticketbooking.Activities.Register

import android.content.Intent
import android.widget.Toast
import androidx.compose.animation.*
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ticketbooking.Activities.Authentication.RegisterActivity
import com.example.ticketbooking.Activities.Splash.GradientButton
import com.example.ticketbooking.Activities.Splash.SplashActivity
import com.example.ticketbooking.Activities.Splash.StatusTopBarColor
import com.example.ticketbooking.R
import com.example.ticketbooking.ViewModel.AuthViewModel
import com.google.accompanist.systemuicontroller.rememberSystemUiController

@Composable
@Preview
fun RegisterScreen(onRegisterSuccess: () -> Unit = {}) {
    val viewModel: AuthViewModel = viewModel()
    val authResult by viewModel.authResult.observeAsState()

    var fullName by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var showForm by remember { mutableStateOf(true) }

    val context = LocalContext.current

    StatusTopBarColor()
    Column(modifier = Modifier
        .fillMaxHeight()
        .background(color = colorResource(R.color.darkPurple2))
    ) {
        ConstraintLayout {
            val (backgroundImg, signUpBtn, regisFormColumn) = createRefs()
            val guideline = createGuidelineFromTop(0.2f) // 10% từ trên xuống


            Image(
                painter = painterResource(R.drawable.world),
                contentDescription = null,
                modifier = Modifier
                    .constrainAs(backgroundImg) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(top = 20.dp, start = 15.dp)

            )

            Column(
                modifier = Modifier
                    .constrainAs(regisFormColumn) {
                        top.linkTo(guideline) // Đặt ban đầu ở phía trên nút
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
                    .padding(16.dp)
            ) {
                Text(
                    text = stringResource(R.string.sign_up_title),
                    fontSize = 50.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = colorResource(R.color.orange),
                    modifier = Modifier
                        .padding(top = 5.dp, bottom = 30.dp)
                        .align(Alignment.CenterHorizontally)
                )
                // Column chứa form đăng nhập (với hiệu ứng trượt và độ mờ)
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .padding(vertical = 8.dp)
                        .background(colorResource(R.color.lightPurple).copy(alpha = 0.8f), RoundedCornerShape(16.dp))
                        .padding(16.dp)
                ) {
                    OutlinedTextField(
                        value = fullName,
                        onValueChange = { fullName = it },
                        label = { Text("Full Name") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
                    )
                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email") },
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)
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
                        var confirmPasswordVisible by remember { mutableStateOf(false) }
                    OutlinedTextField(
                        value = confirmPassword,
                        onValueChange = { confirmPassword = it },
                        label = { Text("Confirm Password") },
                        visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                        trailingIcon = {
                            val image = if (confirmPasswordVisible) R.drawable.ic_visibility_off else R.drawable.ic_visibility
                            IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                                Icon(painter = painterResource(id = image), contentDescription = null)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().padding(vertical = 4.dp),
                        keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done)

                    )

                    Text(
                        text = stringResource(R.string.sign_in_link),
                        fontSize = 12.sp,
                        color = colorResource(R.color.orange),
                        modifier = Modifier
                            .padding(top = 8.dp)
                            .clickable {
                                // Create an Intent to navigate to the registration screen.
                                val intent = Intent(context, SplashActivity::class.java)
                                context.startActivity(intent)
                            },
                        textDecoration = TextDecoration.Underline
                    )
                }


            }
            Box(
                modifier = Modifier
                    .padding(top = 32.dp)
                    .constrainAs(signUpBtn) {
                        top.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    },
                contentAlignment = Alignment.Center // Để căn giữa nút và text
            ) {
                GradientButton(
                    onClick = {
                        if(fullName.isBlank() || email.isBlank() || password.isBlank() || confirmPassword.isBlank()) {
                            Toast.makeText(context, "Try again", Toast.LENGTH_SHORT).show()
                        } else {
                            viewModel.register(fullName, email, password)
                        }
                    },
                    text = stringResource(R.string.sign_up_title),
                )
                authResult?.let { (success, message) ->
                    if (success) {
                        Toast.makeText(context, "Regis successful", Toast.LENGTH_SHORT).show()
                        onRegisterSuccess()
                    } else {
                        Toast.makeText(context, message ?: "Regis failed", Toast.LENGTH_SHORT).show()
                    }
                    viewModel.resetAuthResult()
                }
            }

        }
    }
}
