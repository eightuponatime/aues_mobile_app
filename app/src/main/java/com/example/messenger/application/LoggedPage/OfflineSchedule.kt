package com.example.messenger.application.LoggedPage

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.application.StarterPage.GradientBackgroundBrush
import com.example.messenger.data.DatabaseManager
import com.example.messenger.data.ScheduleDao
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.url
import io.ktor.http.HttpMethod
import io.ktor.http.URLBuilder
import io.ktor.http.URLProtocol
import io.ktor.http.encodedPath
import io.ktor.websocket.CloseReason
import io.ktor.websocket.Frame
import io.ktor.websocket.close
import io.ktor.websocket.readText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OfflineScedule(
    navController: NavController,
    username: String,
) {
    val gradientColorList = listOf(
        Color(0xFFabc4ff),
        Color(0xFFb6ccfe),
        Color(0xFFc1d3fe),
        Color(0xFFccdbfd),
        Color(0xFFd7e3fc),
        Color(0xFFe2eafc),
        Color(0xFFedf2fb)
    )
    val buttonColor = Color(0xFF7289DA)
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )

    Scaffold(
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        topBar = {
            CenterAlignedTopAppBar(
                colors = TopAppBarDefaults.largeTopAppBarColors(
                    containerColor = Color(0xFF7289DA),
                    titleContentColor = Color.White,
                ),
                title = {
                    Text(
                        text = "aues scraper",
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        color = Color.White,
                        fontFamily = FontFamily(Font(R.font.dank_mono))
                    )
                },
                navigationIcon = {
                    IconButton(onClick = {
                        navController.popBackStack()
                    }) {
                        Icon(
                            imageVector = Icons.Filled.ExitToApp,
                            contentDescription = "",
                            tint = Color.White
                        )
                    }
                },
                scrollBehavior = scrollBehavior
            )
        },
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(
                    brush = GradientBackgroundBrush(
                        isVerticalGradient = true,
                        colors = gradientColorList
                    )
                ),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                var result by remember {
                    mutableStateOf("")
                }

                LaunchedEffect(username) {
                    val schedule = withContext(Dispatchers.IO) {
                        val scheduleDao = DatabaseManager.getDatabase().scheduleDao()
                        val scheduleEntity = scheduleDao.getScheduleByUsername(username)
                        scheduleEntity?.schedule
                    }
                    if (schedule != null) {
                        result = schedule
                    }
                }
                LazyColumn(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxSize()
                ) {
                    if (result.isNotEmpty()) {
                        val dayStrings = result.split(
                            "День недели: Вторник", "День недели: Среда", "День недели: Четверг",
                            "День недели: Пятница", "День недели: Суббота", "День недели: Воскресенье")

                        val daysOfWeek = listOf("Понедельник", "День недели: Вторник",
                            "День недели: Среда", "День недели: Четверг",
                            "День недели: Пятница", "День недели: Суббота",
                            "День недели: Воскресенье")

                        for ((index, dayString) in dayStrings.withIndex()) {
                            item {
                                Card(
                                    colors = CardDefaults.cardColors(buttonColor),
                                    modifier = Modifier.fillMaxWidth()
                                        .padding(0.dp,0.dp,0.dp,8.dp)
                                ) {
                                    Column {
                                        if(index > 0) {
                                            Text(
                                                text = daysOfWeek[index],
                                                color = Color.White,
                                                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                                                modifier = Modifier.padding(16.dp)
                                            )
                                        }
                                        Text(
                                            text = dayString.trim(),
                                            color = Color.White,
                                            fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                                            modifier = Modifier.padding(16.dp)
                                        )
                                    }
                                }
                            }
                        }
                    } else {
                        item {
                            Text(
                                text = result,
                                color = Color.White,
                                fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                            )
                        }
                    }
                }
            }
        }
    }
}