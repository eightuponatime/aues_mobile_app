package com.example.messenger.application

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import org.jetbrains.exposed.sql.transactions.transaction

@Composable
fun UsernameField(usernameState: MutableState<String>, textColor: Color) {
    val usernameValue = usernameState.value
    val buttonColor = Color(0xFF7289DA)
    Text(
        text = "Username",
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.dank_mono)),
        textAlign = TextAlign.Center
    )
    BasicTextField(
        value = usernameValue,
        onValueChange = { usernameState.value = it },
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.dank_mono)),
        ),
        cursorBrush = SolidColor(Color.White),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Transparent)
            .border(1.dp, buttonColor, RoundedCornerShape(30.dp))
            .padding(12.dp)
    )
}

@Composable
fun PasswordField(passwordState: MutableState<String>, textColor: Color) {
    val passwordValue = passwordState.value
    val buttonColor = Color(0xFF7289DA)
    Text(
        text = "Password",
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.dank_mono)),
        textAlign = TextAlign.Center
    )
    BasicTextField(
        value = passwordValue,
        onValueChange = { passwordState.value = it },
        textStyle = TextStyle(
            color = Color.White,
            fontSize = 24.sp,
            fontFamily = FontFamily(Font(R.font.dank_mono_italic))
        ),
        visualTransformation = PasswordVisualTransformation(),
        cursorBrush = SolidColor(textColor),
        modifier = Modifier
            .fillMaxWidth()
            .height(50.dp)
            .background(Color.Transparent)
            .border(1.dp, buttonColor, RoundedCornerShape(30.dp))
            .padding(12.dp)
    )
}

@Composable
fun RegistrationAlertDialog(navController: NavController, onClose: () -> Unit) {
    AlertDialog(
        containerColor = Color(0xffbbd0ff),
        onDismissRequest = {
            onClose()
        },
        title = {
            Text(
                text = "Registered successfully.",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.dank_mono))
            )
        },
        text = {
            Text(
                text = "You have successfully signed up!",
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.dank_mono))
            )
        },
        confirmButton = {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onClose()
                        navController.popBackStack()
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF7289DA)
                    ),
                ) {
                    Text(
                        text = "Move to log in screen",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.dank_mono))
                    )
                }
            }
        }
    )
}

@Composable
fun WrongRegistrationAlertDialog(onClose: () -> Unit) {
    AlertDialog(
        containerColor = Color(0xffbbd0ff),
        onDismissRequest = {
            onClose()
        },
        title = {
            Text(
                text = "Warning",
                color = Color.White,
                fontSize = 18.sp,
                fontFamily = FontFamily(Font(R.font.dank_mono))
            )
        },
        text = {
            Text(
                text = "this user is already in database",
                fontSize = 16.sp,
                color = Color.White,
                fontFamily = FontFamily(Font(R.font.dank_mono))
            )
        },
        confirmButton = {
            Box(
                modifier = Modifier.fillMaxWidth().padding(16.dp),
                contentAlignment = Alignment.Center
            ) {
                Button(
                    onClick = {
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(
                        Color(0xFF7289DA)
                    ),
                ) {
                    Text(
                        text = "ok :c",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.dank_mono))
                    )
                }
            }
        }
    )
}