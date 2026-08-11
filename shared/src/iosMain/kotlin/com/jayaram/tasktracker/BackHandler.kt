package com.jayaram.tasktracker

import androidx.compose.runtime.Composable

@Composable
actual fun BackHandler(enabled: Boolean, onBack: () -> Unit) {
    //iOS handles default back getures.
}