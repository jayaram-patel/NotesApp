# Implementation Plan - WebView Redesign and JS Bridge

The goal is to clean up the `BrowserScreen` and `WebView` implementation, removing hardcoded "useless" UI/HTML, and implementing a clean bidirectional communication bridge between Native and Web.

## User Review Required

> [!IMPORTANT]
> The current local version has uncommitted changes and is **not** identical to the version on GitHub. I will proceed with the redesign on top of your local changes unless you want me to reset them first.

> [!NOTE]
> I will implement a standard communication bridge. If you have specific JS function names in mind, please let me know. Otherwise, I will use `postMessageToWeb` (Native -> Web) and `onMessageReceived` (Web -> Native).

## Proposed Changes

### [Component] shared:shared

#### [MODIFY] [WebView.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/WebView.kt)
- Update the `WebView` signature to include a `WebViewController` or similar for Native-to-Web communication.
- Add an `onMessageReceived` callback for Web-to-Native communication.

#### [MODIFY] [WebView.android.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/androidMain/kotlin/com/jayaram/tasktracker/WebView.android.kt)
- Remove hardcoded HTML and internal `Column`/`Button`.
- Implement `WebViewController` for Android using `webView.evaluateJavascript`.
- Set up `WebAppInterface` to trigger the `onMessageReceived` callback.

#### [MODIFY] [BrowserScreen.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/BrowserScreen.kt)
- Redesign the layout to be less cluttered.
- Use the updated `WebView` component.
- Example usage of the JS bridge (e.g., a button in the TopAppBar to send a message).

## Verification Plan

### Automated Tests
- N/A for UI components, but will verify build stability.

### Manual Verification
- Deploy the `:androidApp`.
- Verify that the WebView loads the provided URL.
- Test bidirectional communication (if possible with a test URL or by injecting JS).
