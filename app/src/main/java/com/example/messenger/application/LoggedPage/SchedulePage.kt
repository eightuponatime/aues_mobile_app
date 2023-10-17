package com.example.messenger.application.LoggedPage

import androidx.compose.foundation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.application.StarterPage.GradientBackgroundBrush
import com.example.messenger.data.DatabaseManager
import com.example.messenger.data.ScheduleEntity
import com.example.messenger.network.ApiClient.getPassword
import com.example.messenger.network.ApiClient.scheduleParser
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.HttpResponse
import io.ktor.client.statement.bodyAsText
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SchedulePage(
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
    val bgradientColorList = listOf(
        Color(0xFFabc4ff),
        Color(0xFFb6ccfe),
        Color(0xFFedf2fb),
        Color(0xFFe2eafc),
        Color(0xFFd7e3fc),
        Color(0xFFccdbfd),
        Color(0xFFc1d3fe),
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
        val drawerState = rememberDrawerState(DrawerValue.Closed)
        val scope = rememberCoroutineScope()
        val menuItems = listOf(
            MenuItem(Icons.Default.Home, "Main Page", onClick =
            {navController.navigate("main/$username/$name/$group")}),
            MenuItem(Icons.Default.Face, "User List", onClick =
            {navController.navigate("user-list/$username/$name/$group")}) ,
            MenuItem(Icons.Default.Email, "Messages", onClick =
            {navController.navigate("server/$username/$name/$group")})
        )
        val selectedItem = remember { mutableStateOf(menuItems[0]) }

        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet(
                    drawerContainerColor = Color(0xFFc1d3fe)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                brush = GradientBackgroundBrush(
                                    isVerticalGradient = true,
                                    colors = bgradientColorList
                                )
                            )
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center,
                    ) {
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .background(Color.Transparent)
                        ) {
                            CreateImageDrawer(username)
                            if(drawerState.isOpen) {
                                Row() {
                                    Text(
                                        color = Color(0xFFf8961e),
                                        fontSize = 24.sp,
                                        fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
                                        text = "user@${username}:~$ "
                                    )
                                    Spacer(Modifier.width(4.dp))
                                    TypingText(text = "show menu")
                                }
                            }
                        }
                    }

                    Spacer(Modifier.height(12.dp))

                    menuItems.forEach { item ->
                        NavigationDrawerItem(
                            icon = { Icon(item.icon, contentDescription = null) },
                            label = { Text(item.label) },
                            selected = item == selectedItem.value,
                            colors = NavigationDrawerItemDefaults.colors(
                                selectedContainerColor = Color(0xFF7289DA),
                                unselectedContainerColor = Color(0xFF7289DA),
                                selectedIconColor = Color.White,
                                unselectedIconColor = Color.White,
                                selectedTextColor = Color.White,
                                unselectedTextColor = Color.White
                            ),
                            onClick = {
                                scope.launch { drawerState.close() }
                                selectedItem.value = item
                                item.onClick()
                            },
                            modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                    }
                }
            },
            content = {
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
                                IconButton(onClick = {scope.launch { drawerState.open() } }) {
                                    Icon(
                                        imageVector = Icons.Filled.Menu,
                                        contentDescription = "Localized description",
                                        tint = Color.White
                                    )
                                }
                            },
                            actions = {
                                IconButton(onClick = { navController.navigate("login") }) {
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
                        var result by remember {
                            mutableStateOf("")
                        }

                        var password by remember {
                            mutableStateOf("")
                        }
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .align(Alignment.TopStart)
                        ) {
                            Row{
                                Button(
                                    onClick = {
                                        result = "parsing result waiting room..."
                                        CoroutineScope(Dispatchers.IO).launch {
                                            password = getPassword(username)

                                            try {
                                                result = scheduleParser(username, password)

                                                if (result.startsWith("День недели: Понедельник")) {
                                                    val scheduleDao = DatabaseManager.getDatabase().scheduleDao()

                                                    val existingSchedule = scheduleDao.getScheduleByUsername(username)

                                                    if (existingSchedule != null) {
                                                        existingSchedule.schedule = result
                                                        scheduleDao.update(existingSchedule)
                                                    } else {
                                                        val scheduleEntity = ScheduleEntity(username = username, schedule = result)
                                                        scheduleDao.insert(scheduleEntity)
                                                    }
                                                }

                                            } catch (e: Exception) {
                                                println("")
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color(0xFF7289DA)),
                                ) {
                                    Text(
                                        text = "Schedule from server",
                                        fontFamily = FontFamily(Font(R.font.ubuntu_mono))
                                    )
                                }


                                Spacer(Modifier.width(8.dp))
                                var emptySchedule by remember {
                                    mutableStateOf(false)
                                }
                                Button(
                                    onClick = {
                                        CoroutineScope(Dispatchers.IO).launch {

                                            val scheduleDao = DatabaseManager.getDatabase().scheduleDao()
                                            val scheduleEntity = scheduleDao.getScheduleByUsername(
                                                username
                                            )

                                            if (scheduleEntity != null) {
                                                val schedule = scheduleEntity.schedule
                                                result = schedule

                                            } else {
                                                emptySchedule = true
                                            }
                                        }
                                    },
                                    colors = ButtonDefaults.buttonColors(Color(0xFF7289DA)),
                                ) {
                                    Text(
                                        text = "Schedule from client",
                                        fontFamily = FontFamily(Font(R.font.ubuntu_mono))
                                    )
                                }
                                if(emptySchedule) {
                                    EmptyScheduleAlertDialog {
                                        emptySchedule = false
                                    }
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
        )
    }
}

@Composable
fun EmptyScheduleAlertDialog(onClose: () -> Unit) {
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
                text = "there're no schedules for this user",
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