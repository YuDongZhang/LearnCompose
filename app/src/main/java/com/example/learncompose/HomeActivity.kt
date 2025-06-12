package com.example.learncompose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.semantics.Role.Companion.Image
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.learncompose.ui.theme.LearnComposeTheme
import coil.compose.rememberAsyncImagePainter

sealed class Screen(val route: String, val label: String) {
    object Home : Screen("home", "主页")
    object Activity : Screen("activity", "动态")
    object Profile : Screen("profile", "个人")
}

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LearnComposeTheme {
                MainScreen()
            }
        }
    }
}

@Composable
fun MainScreen() {
    val navController = rememberNavController()
    val items = listOf(Screen.Home, Screen.Activity, Screen.Profile)
    Scaffold(bottomBar = {
        NavigationBar {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination
            items.forEach { screen ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = when (screen) {
                                Screen.Home -> Icons.Filled.Home
                                Screen.Activity -> Icons.Filled.List
                                Screen.Profile
                                    -> Icons.Filled.AccountCircle
                            }, contentDescription = null
                        )
                    },
                    label = { Text(screen.label) },
                    selected = currentDestination?.hierarchy?.any { it.route == screen.route } == true,
                    onClick = {
                        navController.navigate(screen.route) {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    })
            }
        }
    }) { innerPadding ->
        NavHost(
            navController,
            startDestination = Screen.Home.route,
            Modifier.padding(innerPadding)
        ) {
            composable(
                Screen.Home.route
            ) { VideoListScreen(viewModel = viewModel()) }
            composable(Screen.Activity.route) { ActivityScreen() }
            composable(Screen.Profile.route) { ProfileScreen(viewModel = viewModel()) }
        }
    }
}

@Composable
fun HomeScreen() {
    Text(text = "主页内容", modifier = Modifier.fillMaxSize())
}

@Composable
fun ActivityScreen() {
    Text(text = "动态内容", modifier = Modifier.fillMaxSize())
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val userInfo by viewModel.userInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.Center) {
        if (isLoading) {
            CircularProgressIndicator()
        } else if (errorMessage != null) {
            Text(text = "Error: ${errorMessage}", color = MaterialTheme.colorScheme.error)
        } else if (userInfo != null) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = rememberAsyncImagePainter(model = userInfo!!.head_url),
                    contentDescription = "User Avatar",
                    modifier = Modifier.size(120.dp),
                    contentScale = ContentScale.Crop
                )
                Spacer(modifier = Modifier.height(16.dp))
                Text(text = "Name: ${userInfo!!.name}", style = MaterialTheme.typography.titleLarge)
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Birthday: ${userInfo!!.birthday}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Blurb: ${userInfo!!.blurb}",
                    style = MaterialTheme.typography.bodyMedium
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Last Updated: ${userInfo!!.updatedAt}",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        } else {
            Text(text = "No user information available.")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewMainScreen() {
    LearnComposeTheme { MainScreen() }
}
