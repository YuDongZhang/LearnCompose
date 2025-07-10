package com.example.learncompose.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

/**
 * 官方文档: https://developer.android.com/develop/ui/compose/components/search-bar?hl=zh-cn
 * 本文件演示 Compose SearchBar 的典型用法，包含搜索栏、搜索建议、搜索结果等。
 */

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarExamples() {
    Column(
        verticalArrangement = Arrangement.spacedBy(32.dp),
        modifier = Modifier.fillMaxSize().padding(16.dp)
    ) {
        Text("SearchBar（搜索栏）", style = MaterialTheme.typography.titleMedium)
        SimpleSearchBar()
        Text("带建议的搜索栏", style = MaterialTheme.typography.titleMedium)
        SearchBarWithSuggestions()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SimpleSearchBar() {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { searchQuery ->
            // 处理搜索逻辑
            query = searchQuery
            expanded = false
        },
        active = expanded,
        onActiveChange = { expanded = it },
        placeholder = { Text("搜索...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "更多选项") }
    ) {
        LazyColumn {
            items(listOf("搜索结果 1", "搜索结果 2", "搜索结果 3")) { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    modifier = Modifier.clickable {
                        query = result
                        expanded = false
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchBarWithSuggestions() {
    var query by remember { mutableStateOf("") }
    var expanded by remember { mutableStateOf(false) }
    val searchResults = remember(query) {
        if (query.isEmpty()) {
            listOf("建议的文字搜索", "建议的图片搜索", "建议的视频搜索")
        } else {
            listOf("$query 结果 1", "$query 结果 2", "$query 结果 3")
        }
    }
    SearchBar(
        query = query,
        onQueryChange = { query = it },
        onSearch = { searchQuery ->
            query = searchQuery
            expanded = false
        },
        active = expanded,
        onActiveChange = { expanded = it },
        placeholder = { Text("搜索内容...") },
        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "搜索") },
        trailingIcon = { Icon(Icons.Default.MoreVert, contentDescription = "更多选项") }
    ) {
        LazyColumn {
            items(searchResults) { result ->
                ListItem(
                    headlineContent = { Text(result) },
                    supportingContent = { Text("点击查看详情") },
                    leadingContent = { Icon(Icons.Default.Star, contentDescription = "收藏") },
                    colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    modifier = Modifier
                        .clickable {
                            query = result
                            expanded = false
                        }
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp)
                )
            }
        }
    }
} 