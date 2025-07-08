package com.example.learncompose.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.AssistChip
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.InputChip
import androidx.compose.material3.SuggestionChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun ChipExamples() {
    Column(verticalArrangement = Arrangement.spacedBy(16.dp), horizontalAlignment = Alignment.CenterHorizontally) {
        ComponentExampleCard("Assist Chip") {
            Text("Used to assist the user with a smart or suggested action.")
            Spacer(Modifier.height(8.dp))
            AssistChip(
                onClick = { /* Do something */ },
                label = { Text("Assist Chip") },
                leadingIcon = { Icon(Icons.Filled.Star, contentDescription = "Favorite") }
            )
        }

        ComponentExampleCard("Filter Chip") {
            var selected by remember { mutableStateOf(false) }
            Text("Used to filter content, such as in a search.")
            Spacer(Modifier.height(8.dp))
            FilterChip(
                selected = selected,
                onClick = { selected = !selected },
                label = { Text("Filter Chip") },
                leadingIcon = if (selected) { { Icon(Icons.Filled.Done, contentDescription = "Done") } } else { null }
            )
        }

        ComponentExampleCard("Input Chip") {
            var enabled by remember { mutableStateOf(true) }
            Text("Represents a complex piece of information, such as an entity (person, place, or thing) or text.")
            Spacer(Modifier.height(8.dp))
            if (enabled) {
                InputChip(
                    selected = false,
                    onClick = { enabled = false },
                    label = { Text("Input Chip") },
                    avatar = { Icon(Icons.Filled.Person, contentDescription = "Person") },
                    trailingIcon = { Icon(Icons.Default.Close, contentDescription = "Close") }
                )
            }
        }

        ComponentExampleCard("Suggestion Chip") {
            Text("Used to provide suggested replies or actions.")
            Spacer(Modifier.height(8.dp))
            SuggestionChip(onClick = { /* Do something */ }, label = { Text("Suggestion Chip") })
        }
    }
}