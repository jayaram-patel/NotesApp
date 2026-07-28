package com.jayaram.tasktracker

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun BrowserScreen(
    initialUrl: String = "https://www.google.com",
    onBack: () -> Unit
) {

    var url by remember {
        mutableStateOf(initialUrl)
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .safeDrawingPadding()
    ) {

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {

            IconButton(
                onClick = onBack
            ) {

                Icon(
                    Icons.Default.ArrowBack,
                    contentDescription = null
                )

            }

            OutlinedTextField(
                value = url,
                onValueChange = {
                    url = it
                },
                modifier = Modifier.weight(1f),
                singleLine = true
            )

            IconButton(
                onClick = {
                    // Reload later
                }
            ) {

                Icon(
                    Icons.Default.Refresh,
                    contentDescription = null
                )

            }

        }

        Divider()

        WebView(
            url = url,
            modifier = Modifier.fillMaxSize()
        )

    }
}