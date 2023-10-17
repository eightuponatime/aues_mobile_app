package com.example.messenger.application

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.messenger.application.Arguments.ChatArguments
import com.example.messenger.application.LoggedPage.MainPage
import com.example.messenger.application.StarterPage.LoginScreen
import com.example.messenger.application.StarterPage.SignupScreen
import com.example.messenger.data.DatabaseManager
import com.example.messenger.ui.theme.MessengerTheme
import com.example.messenger.application.Arguments.ServerPageArguments
import com.example.messenger.application.Arguments.UserListPageArguments
import com.example.messenger.application.Arguments.MainPageArguments
import com.example.messenger.application.Arguments.OfflineScheduleArguments
import com.example.messenger.application.Arguments.SchedulePageArguments
import com.example.messenger.application.Arguments.UserPageArguments
import com.example.messenger.application.Arguments.UserSchedulePageArguments
import com.example.messenger.application.LoggedPage.Chat
import com.example.messenger.application.LoggedPage.FriendsPage
import com.example.messenger.application.LoggedPage.OfflineScedule
import com.example.messenger.application.LoggedPage.SchedulePage
import com.example.messenger.application.LoggedPage.ServerPage
import com.example.messenger.application.LoggedPage.UserPage
import com.example.messenger.application.LoggedPage.UserSchedulePage

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        DatabaseManager.initialize(applicationContext)

        setContent {
            MessengerTheme {
                val navController = rememberNavController()
                NavHost(navController = navController, startDestination = "login") {

                    //navigator to login screen
                    composable("login") {
                        LoginScreen(navController = navController)
                    }

                    //navigator to signup screen
                    composable("signup") {
                        SignupScreen(navController = navController)
                    }

                    //navigator to chat page
                    composable(
                        route = "chat/{username}/{name}/{group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {}
                        )
                    ) {
                        backStackEntry ->
                        val arguments = ChatArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: ""
                        )
                        Chat(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group
                        )
                    }

                    //navigator to friends page
                    composable(
                        route = "user-list/{username}/{name}/{group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {}
                        )
                    ) {backStackEntry ->
                        val arguments = UserListPageArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: ""
                        ) 
                        FriendsPage(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group
                        )
                    }

                    //navigator to server confirmation
                    composable(
                        route = "server/{username}/{name}/{group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {}
                        )
                    ) { backStackEntry ->
                        val arguments = ServerPageArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: ""
                        )
                        ServerPage(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group
                        )

                    }

                    //navigator to main page
                    composable(
                        route = "main/{username}/{name}/{group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {},
                        )
                    ) { backStackEntry ->
                        val arguments = MainPageArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: "",
                        )
                        MainPage(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group
                        )
                    }

                    //navigator to user page
                    composable(
                        route = "user-page/{username}/{name}/{group}/{user_username}/{user_name}/{user_group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {},
                            navArgument("user_username") {},
                            navArgument("user_name") {},
                            navArgument("user_group") {}
                        )
                    ) { backStackEntry ->
                        val arguments = UserPageArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: "",
                            user_username = backStackEntry.arguments?.getString("user_username") ?: "",
                            user_name = backStackEntry.arguments?.getString("user_name") ?: "",
                            user_group = backStackEntry.arguments?.getString("user_group") ?: ""
                        )
                        UserPage(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group,
                            user_username = arguments.user_username,
                            user_name = arguments.user_name,
                            user_group = arguments.user_group
                        )
                    }

                    //navigator to schedule page
                    composable(
                        route = "schedule/{username}/{name}/{group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {},
                        )
                    ) {
                        backStackEntry ->
                        val arguments = SchedulePageArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: "",
                        )
                        SchedulePage(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group,
                        )
                    }

                    //navigator to offline schedule page
                    composable(
                        route = "offline-schedule/{username}",
                        arguments = listOf(
                            navArgument("username") {}
                        )
                    ) { backStackEntry ->
                        val arguments = OfflineScheduleArguments(
                            username = backStackEntry.arguments?.getString("username") ?: ""
                        )
                        OfflineScedule(navController = navController, username = arguments.username)
                    }

                    //navigator to user schedule page
                    composable(
                        route = "user-schedule/{username}/{name}/{group}/{user_username}/{user_name}/{user_group}",
                        arguments = listOf(
                            navArgument("username") {},
                            navArgument("name") {},
                            navArgument("group") {},
                            navArgument("user_username") {},
                            navArgument("user_name") {},
                            navArgument("user_group") {}
                        )
                    ) { backStackEntry ->
                        val arguments = UserSchedulePageArguments(
                            username = backStackEntry.arguments?.getString("username") ?: "",
                            name = backStackEntry.arguments?.getString("name") ?: "",
                            group = backStackEntry.arguments?.getString("group") ?: "",
                            user_username = backStackEntry.arguments?.getString("user_username") ?: "",
                            user_name = backStackEntry.arguments?.getString("user_name") ?: "",
                            user_group = backStackEntry.arguments?.getString("user_group") ?: ""
                        )
                        UserSchedulePage(
                            navController = navController,
                            username = arguments.username,
                            name = arguments.name,
                            group = arguments.group,
                            user_username = arguments.user_username,
                            user_name = arguments.user_name,
                            user_group = arguments.user_group
                        )
                    }

                }
            }
        }
    }
}
