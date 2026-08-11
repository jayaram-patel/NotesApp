package com.jayaram.tasktracker

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.jayaram.tasktracker.repository.ContactRepository
import platform.WebKit.*
import platform.Foundation.*
import platform.darwin.NSObject

// This class handles the messages sent from JavaScript
class LoggerScriptMessageHandler(
    private val repository: ContactRepository
) : NSObject(), WKScriptMessageHandlerProtocol {
    override fun userContentController(userContentController: WKUserContentController, didReceiveScriptMessage: WKScriptMessage) {
        val body = didReceiveScriptMessage.body as? Map<String, Any> ?: return
        val action = body["action"] as? String

        when (action) {
            "submitForm" -> {
                val name = body["name"] as? String ?: ""
                val email = body["email"] as? String ?: ""
                val message = body["message"] as? String ?: ""
                repository.insertSubmission(name, email, message)
            }
            "showSuccess" -> println("Contact form submitted successfully!")
            "showError" -> println("Please fill all fields!")
        }
    }
}

@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onTitleChanged: (String) -> Unit
) {
    val database = DatabaseModule.provideDatabase()
    val repository = ContactRepository(database)

    UIKitView(
        factory = {
            val config = WKWebViewConfiguration().apply {
                val controller = WKUserContentController()
                // Register the bridge
                controller.addScriptMessageHandler(LoggerScriptMessageHandler(repository), "iosBridge")
                userContentController = controller
            }

            WKWebView(frame = platform.CoreGraphics.CGRectZero.readValue(), configuration = config).apply {
                // Inject a "shim" so your existing JS 'Android.submitForm' calls work on iOS
                val shim = """
                    window.Android = {
                        submitForm: function(n, e, m) {
                            window.webkit.messageHandlers.iosBridge.postMessage({action: 'submitForm', name: n, email: e, message: m});
                        },
                        showSuccess: function() {
                            window.webkit.messageHandlers.iosBridge.postMessage({action: 'showSuccess'});
                        },
                        showError: function() {
                            window.webkit.messageHandlers.iosBridge.postMessage({action: 'showError'});
                        }
                    };
                """.trimIndent()

                evaluateJavaScript(shim, null)

                // Logic to load local HTML or remote URL
                if (url.startsWith("contact_us")) {
                    val noteText = url.substringAfter("note=", "")
                    val bundle = NSBundle.mainBundle
                    val path = bundle.pathForResource("contact", "html")
                    if (path != null) {
                        val fileUrl = NSURL.fileURLWithPath(path)
                        val urlWithParams = NSURL(string = "?note=" + noteText, relativeToURL = fileUrl)
                        loadFileURL(urlWithParams, allowingReadAccessToURL = fileUrl.URLByDeletingLastPathComponent()!!)
                    }
                } else if (url.startsWith("http")) {
                    loadRequest(NSURLRequest(NSURL(string = url)))
                }
            }
        },
        modifier = modifier
    )
}