package com.example.messenger.application

import org.jetbrains.exposed.sql.*

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
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import androidx.room.Room
import com.example.messenger.R
import com.example.messenger.data.AppDatabase
import com.example.messenger.data.DatabaseManager
import com.example.messenger.data.UserEntity
import com.example.messenger.network.ApiClient.apiService
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.jetbrains.exposed.sql.transactions.transaction


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
                        text = "Enter username and password from platonus",
                        color = Color.White,
                        fontSize = 14.sp,
                        fontFamily = FontFamily(Font(R.font.dank_mono)),
                        textAlign = TextAlign.Center
                    )
                }

                Spacer(Modifier.height(30.dp))

                /*val context = LocalContext.current
                val database = Room.databaseBuilder(
                    context?.applicationContext ?: throw IllegalStateException("Context is null."),
                    AppDatabase::class.java, "app-database"
                ).build()*/

                val scope = rememberCoroutineScope()

                Button(
                    onClick = {
                        scope.launch {
                            val username = usernameState.value
                            val password = passwordState.value
                            val success = registerUser(username, password)
                            if (success) {
                                // Регистрация выполнена успешно
                                showSuccessDialog.value = true
                            } else {
                                showUnsuccessDialog.value = true
                                // Пользователь с таким именем уже существует
                            }
                        }
                        /*if (validateUserData(usernameState.value, passwordState.value)) {
                            CoroutineScope(Dispatchers.IO).launch {
                                val newUser = UserEntity(0, usernameState.value, passwordState.value)
                                database.userDao().insert(newUser)

                                val response = apiService.register(newUser) // Выполнение сетевого запроса

                                withContext(Dispatchers.Main) {
                                    showDialog.value = true
                                }
                            }
                        }*/
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

                if (showSuccessDialog.value) {
                    RegistrationAlertDialog(navController) {
                        showSuccessDialog.value = false // Закрыть диалог
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


suspend fun registerUser(username: String, password: String): Boolean {
    return withContext(Dispatchers.IO) {
        val existingUser = DatabaseManager.getDatabase().userDao().getUserByUsername(username)
        return@withContext if (existingUser == null) {
            val newUser = UserEntity(username = username, password = password)
            DatabaseManager.getDatabase().userDao().insert(newUser)
            true
        } else {
            false
        }
    }
}


/*fun validateUserData(username: String, password: String): Boolean {
    // Выполните необходимую проверку данных, возвратите true, если данные корректны
    // Например, можно добавить проверку наличия в базе данных и соответствие регулярным выражениям
    return true // Измените на основе вашей проверки
}*/

