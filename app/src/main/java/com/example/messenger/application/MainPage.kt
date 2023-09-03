@file:OptIn(ExperimentalMaterial3Api::class)

package com.example.messenger.application

import android.content.Context
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.NavigationDrawerItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
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
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.messenger.R
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File

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

    val bgradientColorList = listOf(
        Color(0xFFabc4ff),
        Color(0xFFb6ccfe),
        Color(0xFFedf2fb),
        Color(0xFFe2eafc),
        Color(0xFFd7e3fc),
        Color(0xFFccdbfd),
        Color(0xFFc1d3fe),
    )
    var showDialog by remember { mutableStateOf(false) }
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
            MenuItem(Icons.Default.Face, "Friends", onClick = {}) ,
            MenuItem(Icons.Default.Email, "Messages", onClick = {})
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
                        CreateImageDrawer()
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
                            CreateImageProfile(username)
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
        )
    }
}

data class MenuItem(
    val icon: ImageVector,
    val label: String,
    val onClick: () -> Unit,
)

@Composable
private fun CreateImageProfile(username: String) {

    val context = LocalContext.current

    val imageUri = rememberSaveable() {
        mutableStateOf("")
    }
    val painter = rememberAsyncImagePainter(
        if (imageUri.value.isEmpty()) {
            val fileName = "image_${username}.jpg"
            val file = File(context.filesDir, fileName)
            if (file.exists()) {
                file.toUri()
            } else {
                R.drawable.ic_user
            }
        } else {
            imageUri.value
        }
    )

    val isImageSelected = imageUri.value.isNotEmpty()

    var isSaveButtonClicked by remember { mutableStateOf(false) }

    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) {
            uri: Uri? ->
        uri?.let { imageUri.value = it.toString() }
        isSaveButtonClicked = false
    }

    Column(
        modifier = Modifier.padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            modifier = Modifier
                .size(200.dp)
                .padding(5.dp),
            shape = CircleShape,
            color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
            shadowElevation = 4.dp
        ) {
            Image(
                painter = painter,
                contentDescription = null,
                modifier = Modifier
                    .clickable { launcher.launch("image/*") },
                contentScale = ContentScale.Crop
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (isImageSelected && !isSaveButtonClicked) {
            Button(
                onClick = {
                    isSaveButtonClicked = true
                    saveImageToInternalStorage(context, Uri.parse(imageUri.value), username)
                },
                colors = ButtonDefaults.buttonColors(Color(0xFF7289DA))
            ) {
                Text(text = "Сохранить изображение")
            }
        }
    }
}

fun saveImageToInternalStorage(context: Context, uri: Uri, username: String) {
    val fileName = "image_${username}.jpg"
    val inputStream = context.contentResolver.openInputStream(uri)
    val outputStream = context.openFileOutput(fileName, Context.MODE_PRIVATE)
    inputStream?.use { input ->
        outputStream.use { output ->
            input.copyTo(output)
        }
    }
}



@Composable
private fun CreateImageDrawer() {
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
            TypingTextConsole("username:~$ ")
            Spacer(Modifier.width(8.dp))
            TypingText(username)
        }

        Row {
            TypingTextConsole("name:~$ ")
            Spacer(Modifier.width(8.dp))
            TypingText("user's name")
        }
        Row() {
            TypingTextConsole("group:~$ ")
            Spacer(Modifier.width(8.dp))
            TypingText("user's group")
        }
    }
}

@Composable
fun TypingTextConsole(text: String) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        for (i in text.indices) {
            delay(50) // Задержка между появлением символов (можете изменить)
            visibleText = text.substring(0, i + 1)
        }
    }

    Text(
        color = Color(0xFFf8961e),
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
        text = visibleText
    )
}

@Composable
fun TypingText(text: String) {
    var visibleText by remember { mutableStateOf("") }

    LaunchedEffect(text) {
        for (i in text.indices) {
            delay(50) // Задержка между появлением символов (можете изменить)
            visibleText = text.substring(0, i + 1)
        }
    }

    Text(
        text = visibleText,
        fontSize = 24.sp,
        fontFamily = FontFamily(Font(R.font.ubuntu_mono)),
        color = Color.White
    )
}