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

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue

import androidx.compose.foundation.layout.fillMaxSize

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

    var webView by remember {
        mutableStateOf<android.webkit.WebView?>(null)
    }

    var isDark by remember {
        mutableStateOf(false)
    }
    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        Button(
            onClick = {

                isDark = !isDark

                val js = if (isDark) {
                    "setDarkTheme();"
                } else {
                    "setLightTheme();"
                }

                webView?.evaluateJavascript(js, null)

            }
        ) {
            Text("Toggle Theme")
        }

        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->

                android.webkit.WebView(context).apply {
                    webView = this
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

                    //temporarily for local HTML page
                    val html = """
                <!DOCTYPE html>
                <html>
                
                <head>
                
                <meta charset="UTF-8">
                
                <style>
                
                body{
                    font-family:Arial;
                    padding:30px;
                    background:#f5f5f5;
                }
                
                input,textarea{
                    width:100%;
                    padding:10px;
                    margin-top:10px;
                    margin-bottom:15px;
                    font-size:16px;
                }
               
                button{
                    width:100%;
                    padding:12px;
                    font-size:16px;
                    background:#1976D2;
                    color:white;
                    border:none;
                }
                
                </style>
                
                </head>
                
                <body>
                
                <h2>Contact Us</h2>
                
                <input id="name" placeholder="Name">
                
                <input id="email" placeholder="Email">
                
                <textarea
                id="message"
                rows="5"
                placeholder="Message"></textarea>
                
                <button onclick="submitForm()">
                Submit
                </button>
                
                <button onclick="setLightTheme()">
                Light Theme
                </button>

                <button onclick="setDarkTheme()">
                Dark Theme
                </button>
                
                <script>
                
                function submitForm(){
                
                    let name=document.getElementById("name").value;
                    let email=document.getElementById("email").value;
                    let message=document.getElementById("message").value;
                
                    if(name=="" || email=="" || message==""){
                
                        alert("Please fill all fields");
                        return;
                
                    }
                
                    Android.submitForm(
                        name,
                        email,
                        message
                    );
                
                    alert("Submitted Successfully");
                
                }
                
                function setDarkTheme(){

                    document.body.style.background = "#222222";
                    document.body.style.color = "white";

                }

                function setLightTheme(){

                    document.body.style.background = "#f5f5f5";
                    document.body.style.color = "black";

                }
                
                </script>
                
                </body>
                
                </html>
                """.trimIndent()

                    loadDataWithBaseURL(
                        null,
                        html,
                        "text/html",
                        "UTF-8",
                        null
                    )
                }
            },
            update = {
                // Do nothing for now
            }
        )
    }
}