package com.example.learncompose.component

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.learncompose.ComponentExampleCard

@Composable
internal fun DialogExamples() {
    var showDialog by remember { mutableStateOf(false) }

    ComponentExampleCard("Alert Dialog") {
        Text("Dialogs inform users about a task and can contain critical information, require decisions, or involve multiple tasks.")
        Spacer(Modifier.height(16.dp))
        Button(onClick = { showDialog = true }) {
            Text("Show Dialog")
        }
    }

    if (showDialog) {
        AlertDialog(
            onDismissRequest = { showDialog = false },
            title = { Text("Dialog Title") },
            text = { Text("This is the content of the dialog. You can inform the user about something important here.") },
            confirmButton = {
                Button(onClick = { showDialog = false }) { Text("Confirm") }
            },
            dismissButton = {
                Button(onClick = { showDialog = false }) { Text("Dismiss") }
            }
        )
    }
}