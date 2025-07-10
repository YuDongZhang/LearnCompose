package com.example.learncompose.component

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment

/**
 * 官方文档：
 * - NavigationBar: https://developer.android.com/develop/ui/compose/components/navigation-bar?hl=zh-cn
 * - Drawer: https://developer.android.com/develop/ui/compose/components/drawer?hl=zh-cn
 * - NavigationRail: https://developer.android.com/develop/ui/compose/components/navigation-rail?hl=zh-cn
 * 本文件演示 Compose 三大导航组件的典型用法。
 */

@Composable
fun NavigationExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("NavigationBar（底部导航栏）", style = MaterialTheme.typography.titleMedium)
        SimpleNavigationBar()
        Text("ModalNavigationDrawer（抽屉导航）", style = MaterialTheme.typography.titleMedium)
        SimpleDrawer()
        Text("NavigationRail（侧边导航栏）", style = MaterialTheme.typography.titleMedium)
        SimpleNavigationRail()
    }
}

// region NavigationBar 示例
@Composable
fun SimpleNavigationBar() {
    var selected by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavItem("歌曲", Icons.Default.Menu),
        NavItem("专辑", Icons.Default.AccountBox),
        NavItem("播放列表", Icons.Default.PlayArrow)
    )
    NavigationBar {
        items.forEachIndexed { index, item ->
            NavigationBarItem(
                selected = selected == index,
                onClick = { selected = index },
                icon = { Icon(item.icon, contentDescription = item.label) },
                label = { Text(item.label) }
            )
        }
    }
    Text("当前选中: ${items[selected].label}", modifier = Modifier.padding(top = 8.dp))
}

// region Drawer 示例
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleDrawer() {
    val drawerItems = listOf("主页", "设置", "关于")
    var drawerOpen by remember { mutableStateOf(false) }
    var selectedIndex by remember { mutableIntStateOf(0) }
    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Text("导航菜单", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
                drawerItems.forEachIndexed { index, label ->
                    NavigationDrawerItem(
                        label = { Text(label) },
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            drawerOpen = false
                        },
                        modifier = Modifier.padding(NavigationDrawerItemDefaults.ItemPadding)
                    )
                }
            }
        },
        gesturesEnabled = true,
        drawerState = rememberDrawerState(if (drawerOpen) DrawerValue.Open else DrawerValue.Closed)
    ) {
        Box(Modifier.fillMaxWidth()) {
            IconButton(onClick = { drawerOpen = true }) {
                Icon(Icons.Default.Menu, contentDescription = "打开抽屉")
            }
            Text("当前页面: ${drawerItems[selectedIndex]}", modifier = Modifier.align(Alignment.Center))
        }
    }
}

// region NavigationRail 示例
@Composable
fun SimpleNavigationRail() {
    var selected by remember { mutableIntStateOf(0) }
    val items = listOf(
        NavItem("歌曲", Icons.Default.Info),
        NavItem("专辑", Icons.Default.Home),
        NavItem("播放列表", Icons.Default.Phone)
    )
    Row(Modifier.height(80.dp)) {
        NavigationRail {
            items.forEachIndexed { index, item ->
                NavigationRailItem(
                    selected = selected == index,
                    onClick = { selected = index },
                    icon = { Icon(item.icon, contentDescription = item.label) },
                    label = { Text(item.label) }
                )
            }
        }
        Text("当前选中: ${items[selected].label}", modifier = Modifier.padding(start = 16.dp, top = 24.dp))
    }
}

data class NavItem(val label: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) 