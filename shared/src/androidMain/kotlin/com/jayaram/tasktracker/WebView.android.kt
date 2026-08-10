package com.jayaram.tasktracker

import android.annotation.SuppressLint
import android.util.Log
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.jayaram.tasktracker.repository.ContactRepository

import android.widget.Toast
import android.content.Context
import android.os.Handler
import android.os.Looper

class WebAppInterface(
    private val repository: ContactRepository,
    private val context: Context
) {
    @JavascriptInterface
    fun submitForm(
        name: String,
        email: String,
        message: String
    ) {

        Log.d("ContactForm", "========== CONTACT FORM ==========")
        Log.d("ContactForm", "Name    : $name")
        Log.d("ContactForm", "Email   : $email")
        Log.d("ContactForm", "Message : $message")
        Log.d("ContactForm", "==================================")

        repository.insertSubmission(
            name,
            email,
            message
        )
    }

    @JavascriptInterface
    fun showSuccess() {

        Handler(Looper.getMainLooper()).post {

            Toast.makeText(
                context,
                "Contact form submitted successfully!",
                Toast.LENGTH_SHORT
            ).show()

        }
    }
    @JavascriptInterface
    fun showError() {

        Handler(Looper.getMainLooper()).post {

            Toast.makeText(
                context,
                "Please fill all fields!",
                Toast.LENGTH_SHORT
            ).show()

        }
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

            val database = DatabaseModule.provideDatabase(context)
            val repository = ContactRepository(database)

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
                    WebAppInterface(
                        repository,
                        context
                    ),
                    "Android"
                )

                // Check if we are trying to open the contact form with parameters
                if (url.startsWith("contact_us")) {
                    val queryParams = url.substringAfter("?", "")
                    loadUrl("file:///android_asset/contact.html?$queryParams")
                } else {
                    loadUrl(url)
                }
            }
        },

        update = {
            // No updates required for now
        }
    )
}