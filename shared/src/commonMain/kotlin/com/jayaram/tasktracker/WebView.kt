package com.jayaram.tasktracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
expect fun WebView(
    url: String,
    modifier: Modifier = Modifier,
    onTitleChanged: (String) -> Unit,
    noteData: String? = null
)