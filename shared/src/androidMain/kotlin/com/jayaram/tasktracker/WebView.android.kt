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
            //Do nothing for now
        }
    )
}