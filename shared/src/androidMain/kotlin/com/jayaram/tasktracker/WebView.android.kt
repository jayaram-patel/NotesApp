package com.jayaram.tasktracker

import android.annotation.SuppressLint
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier
) {

    val currentUrl = remember {
        url
    }

    AndroidView(
        modifier = modifier,
        factory = { context ->

            android.webkit.WebView(context).apply {

                webViewClient = WebViewClient()

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                loadUrl(currentUrl)
            }

        }
    )

}