package com.example.messenger.application.StarterPage


import io.ktor.client.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.data.DatabaseManager
import com.example.messenger.data.UserEntity
import com.example.messenger.network.ApiClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@Composable
fun SignupScreen(navController: NavController) {
    val background = Color(25, 22, 34)
    val textColor = Color.White
    val buttonColor = Color(0xFF7289DA)
    val showSuccessDialog = remember { mutableStateOf(false) }
    val showUnsuccessDialog = remember {mutableStateOf(false)}

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
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Button(
                onClick = { navController.popBackStack() },
                colors = ButtonDefaults.buttonColors(buttonColor),
                modifier = Modifier
                    .width(150.dp)
                    .height(50.dp)
                    .align(Alignment.TopStart)
            ) {
                Text(
                    text = "Back",
                    color = textColor,
                    fontSize = 18.sp,
                    fontFamily = FontFamily(Font(R.font.dank_mono))
                )
            }
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Box(
                    modifier = Modifier
                        .border(
                            3.dp, Color.White,
                            shape = RoundedCornerShape(10.dp)
                        )
                        .padding(16.dp)
                ) {
                    Text(
                        text = "Sign Up",
                        fontFamily = FontFamily(Font(R.font.dank_mono)),
                        fontSize = 30.sp,
                        color = Color.White
                        )
                }
                Spacer(Modifier.height(16.dp))

                val firstNameState = remember {
                    mutableStateOf("")
                }
                FirstNameField(firstNameState = firstNameState, textColor = textColor)

                Spacer(Modifier.height(8.dp))

                val lastNameState = remember {
                    mutableStateOf("")
                }
                LastNameField(lastNameState = lastNameState, textColor = textColor)

                Spacer(Modifier.height(8.dp))

                val groupState = remember {
                    mutableStateOf("")
                }
                GroupField(groupState = groupState, textColor = textColor)

                Spacer(Modifier.height(8.dp))

                val usernameState = remember {
                    mutableStateOf("")
                }
                UsernameField(usernameState,  textColor)

                Spacer(Modifier.height(8.dp))

                val passwordState = remember {
                    mutableStateOf("")
                }
                PasswordField(passwordState, textColor)

                Spacer(Modifier.height(8.dp))

                Box(
                    modifier = Modifier
                        .border(1.dp, Color.White, shape = RoundedCornerShape(30.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "Enter username and password from aues portal",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.dank_mono)),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(30.dp))

                val scope = rememberCoroutineScope()

                val emptyFieldsList = remember { mutableStateOf(emptyList<String>()) }
                val showEmptyFieldsDialog = remember { mutableStateOf(false) }

                Button(
                    onClick = {
                        scope.launch {
                            val username = usernameState.value
                            val password = passwordState.value
                            val firstName = firstNameState.value
                            val lastName = lastNameState.value
                            val studyGroup = groupState.value

                            val emptyFields = validateFields(username, password, firstName,
                                lastName, studyGroup)

                            if (emptyFields.isEmpty()) {
                                val success = registerUser(username, password, firstName,
                                    lastName, studyGroup)

                                if (success) {
                                    showSuccessDialog.value = true
                                } else {
                                    showUnsuccessDialog.value = true
                                }
                            } else {
                                showEmptyFieldsDialog.value = true
                                emptyFieldsList.value = emptyFields
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(buttonColor),
                ) {
                    Text(
                        text = "Sign Up",
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.dank_mono_regular)),
                        color = textColor
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

                if (showSuccessDialog.value) {
                    RegistrationAlertDialog(navController) {
                        showSuccessDialog.value = false
                    }
                }
                if(showUnsuccessDialog.value) {
                    WrongRegistrationAlertDialog {
                        showUnsuccessDialog.value = false
                    }
                }
            }
        }
    }
}

suspend fun registerUser(username: String, password: String, first_name: String,
                         last_name: String, study_group: String): Boolean {
    return withContext(Dispatchers.IO) {
        val userExists = ApiClient.checkIfUserExists(username)
        if (!userExists) {
            val newUser = UserEntity(username = username, password = password)
            DatabaseManager.getDatabase().userDao().insert(newUser)
            ApiClient.sendUserDataToServer(username, password, first_name, last_name, study_group)
            true
        } else {
            false
        }
    }
}


fun validateFields(username: String, password: String, firstName: String, lastName: String, studyGroup: String): List<String> {
    val emptyFields = mutableListOf<String>()
    if (username.isEmpty()) {
        emptyFields.add("Username")
    }
    if (password.isEmpty()) {
        emptyFields.add("Password")
    }
    if (firstName.isEmpty()) {
        emptyFields.add("First Name")
    }
    if (lastName.isEmpty()) {
        emptyFields.add("Last Name")
    }
    if (studyGroup.isEmpty()) {
        emptyFields.add("Study Group")
    }
    return emptyFields
}

