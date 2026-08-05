package com.jayaram.tasktracker

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView

import android.util.Log
import android.webkit.JavascriptInterface

class WebAppInterface {

    @JavascriptInterface
    fun submitForm(name: String, email: String, message: String) {

        Log.d("ContactForm", "Name: $name")
        Log.d("ContactForm", "Email: $email")
        Log.d("ContactForm", "Message: $message")

    }
}
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

                    addJavascriptInterface(
                        WebAppInterface(),
                        "Android"
                    )

                    loadUrl("file:///android_asset/contact.html")
                }
            },
            update = {
                // Do nothing for now
            }
        )
    }