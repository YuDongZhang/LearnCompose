package com.example.learncompose

import android.Manifest
import android.content.ContentResolver
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.List
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
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
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import java.io.File
import java.io.FileOutputStream
import java.util.Objects
import java.util.UUID
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState

import androidx.compose.material3.Card
import androidx.compose.runtime.snapshotFlow
import com.example.learncompose.network.DynamicItem

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
            composable(Screen.Activity.route) { ActivityScreen(viewModel = viewModel()) }
            composable(Screen.Profile.route) { ProfileScreen(viewModel = viewModel()) }
        }
    }
}

@Composable
fun HomeScreen() {
    Text(text = "主页内容", modifier = Modifier.fillMaxSize())
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalPermissionsApi::class)
@Composable
fun ActivityScreen(viewModel: ActivityViewModel = viewModel()) {
    val selectedImageUris by viewModel.selectedImageUris.collectAsState()
    val postText by viewModel.postText.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val context = LocalContext.current

    var showBottomSheet by remember { mutableStateOf(false) }
    val sheetState = rememberModalBottomSheetState()

    val cameraPermissionState = rememberPermissionState(Manifest.permission.CAMERA)
    val galleryPermissionState = rememberPermissionState(Manifest.permission.READ_EXTERNAL_STORAGE)

    val takePictureLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicturePreview()) { bitmap ->
        if (bitmap != null) {
            // Save bitmap to a temporary file and get its URI
            val uri = saveBitmapToTempUri(context, bitmap)
            if (uri != null) {
                viewModel.onImageSelected(uri)
            }
        }
        showBottomSheet = false
    }

    val pickImageLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        if (uri != null) {
            viewModel.onImageSelected(uri)
        }
        showBottomSheet = false
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("动态") },
                actions = {
                    IconButton(onClick = { showBottomSheet = true }) {
                        Icon(Icons.Filled.Add, contentDescription = "Add Image")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Display selected images
            if (selectedImageUris.isNotEmpty()) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .horizontalScroll(rememberScrollState())
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedImageUris.forEach { uri ->
                        Image(
                            painter = rememberAsyncImagePainter(model = uri),
                            contentDescription = null,
                            modifier = Modifier
                                .size(180.dp)
                                .clickable { viewModel.removeImage(uri) }, // Allow removal
                            contentScale = ContentScale.Crop
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            OutlinedTextField(
                value = postText,
                onValueChange = { viewModel.onPostTextChange(it) },
                label = { Text("说点什么...") },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Button(
                onClick = { viewModel.publishDynamic() },
                enabled = !isLoading && (selectedImageUris.isNotEmpty() || postText.isNotBlank()),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text("发表")
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Button(
                onClick = { viewModel.deleteAllDynamics() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth(),
                colors = androidx.compose.material3.ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
            ) {
                Text("删除所有动态")
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = message)

            // Dynamic list display
            val dynamicList by viewModel.dynamicList.collectAsState()
            val isListLoading by viewModel.isListLoading.collectAsState()
            val canLoadMoreList by viewModel.canLoadMoreList.collectAsState()
            val listState = rememberLazyListState() // New state for dynamic list

            Spacer(modifier = Modifier.height(16.dp))
            Text(text = "动态列表", style = MaterialTheme.typography.titleLarge)
            Spacer(modifier = Modifier.height(8.dp))

            LazyColumn(
                state = listState,
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(8.dp)
            ) {
                items(dynamicList) { dynamicItem ->
                    DynamicListItem(dynamicItem = dynamicItem)
                }

                if (isListLoading && canLoadMoreList) {
                    item {
                        Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }

            // Load More scroll listener for dynamic list
            LaunchedEffect(listState) {
                snapshotFlow { listState.layoutInfo.visibleItemsInfo.lastOrNull()?.index }
                    .collect { index ->
                        if (index != null && index >= dynamicList.size - 1 && !isListLoading && canLoadMoreList) {
                            viewModel.fetchDynamicList()
                        }
                    }
            }
        }
    }

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = { showBottomSheet = false },
            sheetState = sheetState
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Button(
                    onClick = {
                        if (cameraPermissionState.status.isGranted) {
                            takePictureLauncher.launch(null)
                        } else {
                            cameraPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("拍照")
                }
                Spacer(modifier = Modifier.height(8.dp))
                Button(
                    onClick = {
                        if (galleryPermissionState.status.isGranted) {
                            pickImageLauncher.launch("image/*")
                        } else {
                            galleryPermissionState.launchPermissionRequest()
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("从相册选择")
                }
            }
        }
    }
}

// Helper function to save bitmap to a temporary URI
private fun saveBitmapToTempUri(context: Context, bitmap: Bitmap): Uri? {
    val filesDir = context.cacheDir
    val imageFile = File(filesDir, "${UUID.randomUUID()}.jpg")
    return try {
        val fos = FileOutputStream(imageFile)
        bitmap.compress(Bitmap.CompressFormat.JPEG, 90, fos)
        fos.flush()
        fos.close()
        Uri.fromFile(imageFile)
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}

@Composable
fun DynamicListItem(dynamicItem: DynamicItem) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp)
    ) {
        Column(modifier = Modifier.padding(8.dp)) {
            dynamicItem.userInfo?.let { userInfo ->
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Image(
                        painter = rememberAsyncImagePainter(model = userInfo.head_url),
                        contentDescription = "User Avatar",
                        modifier = Modifier.size(40.dp),
                        contentScale = ContentScale.Crop
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = userInfo.name ?: "Unknown User", style = MaterialTheme.typography.titleSmall)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            dynamicItem.images?.let { images ->
                if (images.isNotEmpty()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp)
                            .horizontalScroll(rememberScrollState()),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        images.forEach { dynamicImage ->
                            Image(
                                painter = rememberAsyncImagePainter(model = dynamicImage.url),
                                contentDescription = null,
                                modifier = Modifier.size(180.dp),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            dynamicItem.text?.let { text ->
                if (text.isNotBlank()) {
                    Text(text = text, style = MaterialTheme.typography.bodyMedium)
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }

            dynamicItem.createdAt?.let { createdAt ->
                Text(text = "Posted: $createdAt", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
fun ProfileScreen(viewModel: ProfileViewModel = viewModel()) {
    val userInfo by viewModel.userInfo.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    Box(modifier = Modifier
        .fillMaxSize()
        .padding(16.dp), contentAlignment = Alignment.TopStart) {
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
