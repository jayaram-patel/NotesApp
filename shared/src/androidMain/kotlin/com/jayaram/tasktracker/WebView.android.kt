package com.jayaram.tasktracker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

@SuppressLint("SetJavaScriptEnabled")
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onTitleChanged: (String) -> Unit
) {

    AndroidView(
        modifier = modifier,
        factory = { context ->

            android.webkit.WebView(context).apply {

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true

                webViewClient = object : WebViewClient() {

                    override fun onPageFinished(
                        view: android.webkit.WebView?,
                        url: String?
                    ) {

                        super.onPageFinished(view, url)

                        view?.evaluateJavascript(
                            """
            document.body.style.zoom = "110%";
            """.trimIndent(),
                            null
                        )
                    }
                }

                webChromeClient = object : WebChromeClient() {
                    override fun onReceivedTitle(
                        view: android.webkit.WebView?,
                        title: String?
                    ) {
                        onTitleChanged(title ?: "Web Page")
                    }
                }

                loadUrl(url)
            }
        },
        update = {
            if (it.url != url) {
                it.loadUrl(url)
            }
        }
    )
}