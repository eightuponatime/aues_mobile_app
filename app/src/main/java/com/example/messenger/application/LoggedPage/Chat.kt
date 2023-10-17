package com.example.messenger.application.LoggedPage

import androidx.compose.foundation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import kotlin.math.cos
import kotlin.math.sin

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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberDrawerState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Alignment.Companion.Center
import androidx.compose.ui.Alignment.Companion.End
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.application.StarterPage.GradientBackgroundBrush
import com.example.messenger.network.ApiService
import io.ktor.client.HttpClient
import io.ktor.client.plugins.websocket.DefaultClientWebSocketSession
import io.ktor.client.plugins.websocket.WebSockets
import io.ktor.client.plugins.websocket.webSocket
import io.ktor.client.plugins.websocket.webSocketSession
import io.ktor.client.request.HttpRequestBuilder
import io.ktor.client.request.get
import io.ktor.client.request.request
import io.ktor.client.request.url
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import io.ktor.client.statement.readText
import io.ktor.http.HttpMethod
import io.ktor.http.HttpStatusCode
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
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.HttpURLConnection
import java.net.URL


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Chat(
    navController: NavController,
    username: String,
    name: String,
    group: String
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
    val scope = rememberCoroutineScope()
    val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(
        rememberTopAppBarState()
    )
    val chatMessages = remember { mutableStateListOf<ChatMessage>() }
    var newMessageText by remember { mutableStateOf("") }

    val client = HttpClient {
        install(WebSockets)
    }

    val connection = rememberUpdatedState(client)

    val socket = remember { mutableStateOf<DefaultClientWebSocketSession?>(null) }

    suspend fun sendMessage(message: String) {
        val newSocket = socket.value
        val frame = Frame.Text(message)
        newSocket?.send(frame)
    }

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
                        val newSocket = socket.value
                        if (newSocket != null && newSocket.isActive) {
                            MainScope().launch {
                                try {
                                    newSocket.close(CloseReason(CloseReason.Codes.NORMAL, "User exited"))
                                } catch (e: Exception) {
                                    e.printStackTrace()
                                }
                                navController.navigate("main/$username/$name/$group")
                            }
                        } else {
                            navController.navigate("main/$username/$name/$group")
                        }
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


                LaunchedEffect(connection.value) {
                    val url = URLBuilder().apply {
                        protocol = URLProtocol("ws", 8080)
                        //url for local host from emulator
                        //host = "10.0.2.2"

                        //url for home wi-fi
                        //host = "192.168.1.4"

                        //url for mobile wi-fi
                        host = "192.168.111.125"
                        encodedPath = "/chat"
                    }.build()

                    val request = HttpRequestBuilder().apply {
                        method = HttpMethod.Get
                        url(url)
                    }

                    val newSocket = connection.value.webSocketSession(
                        method = HttpMethod.Get
                    ) {
                        url(url)
                    }

                    socket.value = newSocket

                    val receiveChannel = newSocket.incoming

                    try {
                        for (frame in receiveChannel) {
                            if (frame is  io.ktor.websocket.Frame.Text) {
                                val text = frame.readText()
                                chatMessages.add(ChatMessage(sender = "Server", text = text))
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    } finally {
                        newSocket.close()
                    }
                }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(Color.Transparent)
                        .border(2.dp, Color.White),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    BasicTextField(
                        value = newMessageText,
                        onValueChange = {
                            newMessageText = it
                        },
                        textStyle = TextStyle(
                            color = Color.White,
                            fontSize = 24.sp,
                            fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                        ),
                        cursorBrush = SolidColor(Color.White),
                        modifier = Modifier
                            .height(50.dp)
                            .background(Color.Transparent)
                            .weight(1f)
                            .border(2.dp, Color.White)
                            .padding(3.dp, 5.dp)
                    )
                    IconButton(
                        onClick = {
                            val message = newMessageText
                            //chatMessages.add(ChatMessage(username, newMessageText))

                            CoroutineScope(Dispatchers.IO).launch {
                                sendMessage(message)
                            }

                            newMessageText = ""
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Send,
                            contentDescription = "Send",
                            tint = Color.White
                        )
                    }
                }
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(8.dp)
                ) {
                    items(chatMessages) { message ->
                        Card(
                            colors = CardDefaults.cardColors(Color(0xFF7289DA)),
                            modifier = Modifier
                                .padding(8.dp)) {
                            ChatMessageItem(message)
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun ChatMessageItem(message: ChatMessage) {
    val textColor = Color.White
    Text(
        text = message.text,
        color = textColor,
        fontSize = 24.sp,
        modifier = Modifier.padding(8.dp),
        fontFamily = FontFamily(Font(R.font.ubuntu_mono))
    )
}

data class ChatMessage(val sender: String, val text: String)