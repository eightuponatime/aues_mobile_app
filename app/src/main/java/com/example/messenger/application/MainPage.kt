package com.example.messenger.application


import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R

@Composable
fun MainPage(
    navController: NavController,
    username: String,
    name: String,
    group: String
) {
    val textColor = Color.White
    val buttonColor = Color(0xFF7289DA)
    val gradientColorList = listOf(
        Color(0xFFabc4ff),
        Color(0xFFb6ccfe),
        Color(0xFFc1d3fe),
        Color(0xFFccdbfd),
        Color(0xFFd7e3fc),
        Color(0xFFe2eafc),
        Color(0xFFedf2fb)
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
                .padding(8.dp),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                CreateImageProfile()
                Spacer(Modifier.height(40.dp))
                CreateInfo(username, name, group)
                Spacer(Modifier.height(40.dp))
                Button(
                    onClick = { /* Handle for user's schedule */ },
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
                        fontFamily = FontFamily(Font(R.font.dank_mono))
                    )
                }
                Spacer(Modifier.height(8.dp))
                Button(
                    onClick = { /* Handle for user's task ccalendar */ },
                    colors = ButtonDefaults.buttonColors(Color(0xFF7289DA)),
                    modifier = Modifier
                        .align(Alignment.CenterHorizontally)
                        .fillMaxWidth()
                        .height(50.dp)
                ) {
                    Text(
                        text = "Task calendar",
                        color = textColor,
                        fontSize = 18.sp,
                        fontFamily = FontFamily(Font(R.font.dank_mono))
                    )
                }
            }
        }
    }
}

@Composable
private fun CreateImageProfile() {
    Surface(
        modifier = Modifier
            .size(200.dp)
            .padding(5.dp),
        shape = CircleShape,
        border = BorderStroke(0.5.dp, Color.LightGray),
        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
        shadowElevation = 4.dp
    ) {
        Image(
            painter = painterResource(id = R.drawable.profile_pic),
            contentDescription = "profile image",
            modifier = Modifier.size(200.dp),
            contentScale = ContentScale.Crop
        )

    }
}

@Composable
private fun CreateInfo(
    username: String,
    name: String,
    group: String
) {
    Column {
        Row(modifier = Modifier.align(Alignment.Start)) {
            Text(color = Color(0xFFf8961e),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                text = "username:~$ ")
            Text(color = Color.White,
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                text = username)
        }

        Row {
            Text(
                color = Color(0xFFf8961e),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                text = "name:~$ "
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "your name",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                color = Color.White
            )
        }
        Row() {
            Text(
                color = Color(0xFFf8961e),
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                text = "group:~$ "
            )
            Spacer(Modifier.width(8.dp))
            Text(
                text = "your group",
                fontSize = 24.sp,
                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                color = Color.White
            )
        }
    }
}
