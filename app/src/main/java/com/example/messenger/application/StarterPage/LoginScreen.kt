package com.example.messenger.application.StarterPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.data.DatabaseManager
import com.example.messenger.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@Composable
fun LoginScreen(navController: NavController) {
    val textColor = Color.White
    val buttonColor = Color(0xFF7289DA)
    val gradientColorList = listOf(
        Color(0xFFA2D2FF),
        Color(0xFFBDE0FE),
        Color(0xFFFFAFCC),
        Color(0xFFFFC8DD),
        Color(0xFFCDB4DB)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                brush = GradientBackgroundBrush(
                    isVerticalGradient = true,
                    colors = gradientColorList
                )
            )
    ) {
        Box(
            modifier = Modifier
                .align(Alignment.TopCenter)
                .padding(16.dp)
                .border(3.dp, Color.White, shape = RoundedCornerShape(30.dp))
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(16.dp)
            ) {
                Text(
                    text = "Welcome to",
                    color = textColor,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.dank_mono))
                )
                Spacer(Modifier.height(8.dp))
                Text(
                    text = "aues scraper",
                    color = textColor,
                    fontSize = 30.sp,
                    fontFamily = FontFamily(Font(R.font.dank_mono))
                )
            }
        }
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Spacer(modifier = Modifier.height(16.dp))


                val context = LocalContext.current
                val userManager = remember { UserManager(context) }

                val usernameState = remember {
                    mutableStateOf(userManager.getUsername() ?: "")
                }
                UsernameTextField(textColor, usernameState)
                Spacer(modifier = Modifier.height(8.dp))

                val passwordState = remember {
                    mutableStateOf(userManager.getPassword() ?: "")
                }
                PasswordTextField(textColor, passwordState)

                var isChecked by remember {
                    mutableStateOf(userManager.isRememberMeEnabled())
                }

                Spacer(Modifier.height(4.dp))
                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Remember me",
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.dank_mono_regular)),
                        modifier = Modifier.align(Alignment.CenterVertically),
                        fontSize = 18.sp
                    )

                    Checkbox(
                        checked = isChecked,
                        onCheckedChange = {
                            isChecked = it
                            if (it) {
                                userManager.saveUserData(usernameState.value, passwordState.value)
                            } else {
                                userManager.clearUserData()
                            }
                        },
                        colors = CheckboxDefaults.colors(
                            checkedColor = buttonColor,
                            uncheckedColor = Color.White
                        )
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))

                var authenticationState by remember { mutableStateOf(AuthenticationState.INITIAL) }
                val scope = rememberCoroutineScope()
                val showUnsuccessfulDialog = remember {mutableStateOf(false)}

                val emptyFieldsList = remember { mutableStateOf(emptyList<String>()) }
                val showEmptyFieldsDialog = remember { mutableStateOf(false) }
                
                Button(
                    onClick = {
                        scope.launch {
                            val username = usernameState.value
                            val password = passwordState.value

                            val emptyFields = validateFields(username, password)

                            if(emptyFields.isEmpty()) {
                                authenticationState =
                                    if (ApiClient.logInDatabase(username, password)) {
                                        AuthenticationState.AUTHENTICATED
                                    } else {
                                        AuthenticationState.UNAUTHENTICATED
                                    }

                                when (authenticationState) {
                                    AuthenticationState.AUTHENTICATED -> {

                                        val name = ApiClient.getFirstName(username)
                                        val group = ApiClient.getGroup(username)
                                        navController.navigate("main/$username/$name/$group")
                                    }

                                    AuthenticationState.UNAUTHENTICATED -> {
                                        showUnsuccessfulDialog.value = true
                                    }

                                    else -> {
                                        showUnsuccessfulDialog.value = true
                                    }
                                }
                            } else {
                                showEmptyFieldsDialog.value = true
                                emptyFieldsList.value = emptyFields
                            }
                        }
                    },
                    colors = ButtonDefaults.buttonColors(Color(0xFF7289DA)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Log In",
                        color = textColor,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.dank_mono))
                    )
                }



                if (showEmptyFieldsDialog.value) {
                    EmptyStateAlertDialog(
                        emptyFieldsList.value,
                        onClose = {
                            showEmptyFieldsDialog.value = false
                        }
                    )
                }

                if(showUnsuccessfulDialog.value) {
                    WrongLoginAlertDialog {
                        showUnsuccessfulDialog.value = false
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                val showScheduleButton = remember { mutableStateOf(false) }

                if (isChecked) {
                    val username = usernameState.value
                    val scheduleDao = DatabaseManager.getDatabase().scheduleDao()

                    LaunchedEffect(username) {
                        val scheduleEntity = withContext(Dispatchers.IO) {
                            scheduleDao.getScheduleByUsername(username)
                        }

                        if (scheduleEntity != null) {
                            showScheduleButton.value = true
                        }
                    }
                }

                if (showScheduleButton.value) {
                    Button(
                        onClick = {
                            navController.navigate("offline-schedule/${usernameState.value}")
                        },
                        colors = ButtonDefaults.buttonColors(Color(0xFF7289DA)),
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .fillMaxWidth()
                            .height(50.dp)
                    ) {
                        Text(
                            text = "Schedule",
                            color = textColor,
                            fontSize = 18.sp,
                            fontFamily = FontFamily(Font(R.font.dank_mono)),
                            modifier = Modifier.align(Alignment.CenterVertically))
                    }
                }

                Spacer(Modifier.height(8.dp))

                Button(onClick = {navController.navigate("signup")},
                    colors = ButtonDefaults.buttonColors(buttonColor),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                ) {
                    Text(
                        text = "Don't have an account?",
                        color = textColor,
                        fontSize = 15.sp,
                        fontFamily = FontFamily(Font(R.font.dank_mono)),
                        modifier = Modifier.align(Alignment.CenterVertically)
                    )
                }


            }
        }

    }
}
@Composable
fun UsernameTextField(textColor: Color, usernameState: MutableState<String>) {
    val buttonColor = Color(0xFF7289DA)
    Text(
        text = "Username",
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.dank_mono)),
        textAlign = TextAlign.Center
    )

    BasicTextField(
        value = usernameState.value,
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
            .padding(12.dp),
        )
}

@Composable
fun PasswordTextField(textColor: Color, passwordState: MutableState<String>) {
    val buttonColor = Color(0xFF7289DA)
    Text(
        text = "Password",
        color = Color.White,
        fontSize = 18.sp,
        fontFamily = FontFamily(Font(R.font.dank_mono)),
        textAlign = TextAlign.Center
    )

    BasicTextField(
        value = passwordState.value,
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
            .padding(12.dp),
    )
}

@Composable
fun GradientBackgroundBrush(isVerticalGradient: Boolean, colors: List<Color>): Brush {
    val endOffSet = if (isVerticalGradient) {
        Offset(0f, Float.POSITIVE_INFINITY)
    } else {
        Offset(Float.POSITIVE_INFINITY, 0f)
    }
    return Brush.linearGradient(
        colors = colors,
        start = Offset.Zero,
        end = endOffSet
    )
}

@Composable
fun WrongLoginAlertDialog(onClose: () -> Unit) {
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
                text = "wrong login or password",
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


fun validateFields(username: String, password: String): List<String> {
    val emptyFields = mutableListOf<String>()
    if (username.isEmpty()) {
        emptyFields.add("Username")
    }
    if (password.isEmpty()) {
        emptyFields.add("Password")
    }
    return emptyFields
}

