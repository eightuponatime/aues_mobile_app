@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.messenger.application.LoggedPage

import androidx.compose.foundation.background
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.messenger.R
import com.example.messenger.application.StarterPage.GradientBackgroundBrush
import com.example.messenger.network.ApiClient
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserPage(
    navController: NavController,
    username: String,
    name: String,
    group: String,
    user_username: String,
    user_name: String,
    user_group: String
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
                                    //don't forget to use regex till space to show only name
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
                val scrollBehavior = TopAppBarDefaults.enterAlwaysScrollBehavior(rememberTopAppBarState())
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
                        Column(
                            verticalArrangement = Arrangement.Center,
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CreateImageDrawer(user_username)
                            Spacer(Modifier.height(40.dp))
                            CreateInfo(user_username, user_name, user_group)
                            Spacer(Modifier.height(40.dp))

                            var password by remember {
                                mutableStateOf<String>("")
                            }

                            Button(
                                onClick = {
                                    scope.launch {
                                        password = ApiClient.getPassword(user_username)
                                    }
                                    navController.navigate("user-schedule/$username/$name/$group/$user_username/$user_name/$user_group")
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
                                    fontFamily = FontFamily(Font(R.font.dank_mono))
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    /*
                                    * open a chat page with a user
                                    * */
                                },
                                colors = ButtonDefaults.buttonColors(Color(0xff7289DA)),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Message",
                                    color = textColor,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.dank_mono))
                                )
                            }
                            Spacer(Modifier.height(8.dp))
                            Button(
                                onClick = {
                                    /*
                                    * send a friends request
                                    * */
                                },
                                colors = ButtonDefaults.buttonColors(Color(0xff7289DA)),
                                modifier = Modifier
                                    .align(Alignment.CenterHorizontally)
                                    .fillMaxWidth()
                                    .height(50.dp)
                            ) {
                                Text(
                                    text = "Add to Friends List",
                                    color = textColor,
                                    fontSize = 18.sp,
                                    fontFamily = FontFamily(Font(R.font.dank_mono))
                                )
                            }
                        }
                    }
                }
            }
        )
    }
}

