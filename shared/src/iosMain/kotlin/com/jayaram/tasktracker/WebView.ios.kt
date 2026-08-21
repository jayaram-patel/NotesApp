package com.jayaram.tasktracker

import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.interop.UIKitView
import com.jayaram.tasktracker.repository.ContactRepository
import kotlinx.cinterop.ExperimentalForeignApi
import kotlinx.cinterop.readValue
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

//(Kotlin->js) implement WKNavigationDelegateProtocol to handle lifecycle
class WebViewDelegate(
    private val onTitleChanged: (String) -> Unit,
    private var noteData: String?
) : NSObject(), WKNavigationDelegateProtocol {

    fun updateNoteData(newNoteData: String?) {
        noteData = newNoteData
    }

    override fun webView(webView: WKWebView, didFinishNavigation: WKNavigation?) {
        onTitleChanged(webView.title ?: "Web Page")
        noteData?.let {
            val escaped = it.replace("'", "\\'")
            webView.evaluateJavaScript("populateNoteData('$escaped')", null)
        }
    }
}

@OptIn(ExperimentalForeignApi::class)
@Composable
actual fun WebView(
    url: String,
    modifier: Modifier,
    onTitleChanged: (String) -> Unit,
    noteData: String?
) {
    val database = DatabaseModule.provideDatabase()
    val repository = ContactRepository(database)

    // IMPORTANT: WKUserContentController holds a weak reference to its script message handlers.
    // We must remember it so it doesn't get garbage collected.
    val scriptMessageHandler = remember {
        LoggerScriptMessageHandler(repository)
    }
            
    val delegate = remember {
        WebViewDelegate(onTitleChanged, noteData)
    }

    SideEffect {
        delegate.updateNoteData(noteData)
    }

    UIKitView(
        factory = { //runs once
            val config = WKWebViewConfiguration().apply {
                val controller = WKUserContentController()
                // Register the bridge (js->kotlin)_
                controller.addScriptMessageHandler(scriptMessageHandler, "iosBridge")

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

                val script = WKUserScript(
                    source = shim,
                    injectionTime = WKUserScriptInjectionTime.WKUserScriptInjectionTimeAtDocumentStart,
                    forMainFrameOnly = true
                )
                controller.addUserScript(script)
                userContentController = controller
            }

            WKWebView(frame = platform.CoreGraphics.CGRectZero.readValue(), configuration = config).apply {
                navigationDelegate = delegate
            }
        },
        modifier = modifier,
        update = { webView ->
            val targetUrl = if (url == "contact_us") {
                val bundle = NSBundle.mainBundle
                bundle.pathForResource("contact", "html")?.let { "file://$it" }
            } else {
                url
            }

            if (targetUrl != null && webView.URL?.absoluteString != targetUrl) {
                if (targetUrl.startsWith("file://")) {
                    val fileUrl = NSURL.fileURLWithPath(targetUrl.removePrefix("file://"))
                    webView.loadFileURL(fileUrl, allowingReadAccessToURL = fileUrl.URLByDeletingLastPathComponent()!!)
                } else {
                    webView.loadRequest(NSURLRequest(NSURL(string = targetUrl)))
                }
            }
        }
    )
}
