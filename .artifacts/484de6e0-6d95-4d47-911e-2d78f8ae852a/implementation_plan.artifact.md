# Implementation Plan - Migration to `evaluateJavascript`

This plan details the steps to replace URL-based data passing (query parameters) with a more secure and modern `evaluateJavascript` (Android) / `evaluateJavaScript` (iOS) approach for the "Contact Us" feature.

## Proposed Changes

### Common Module

#### [MODIFY] [WebView.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/WebView.kt)
- Ensure the `expect fun WebView` includes the `noteData: String? = null` parameter.

#### [MODIFY] [BrowserScreen.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/BrowserScreen.kt)
- Update `BrowserScreen` to accept `noteData: String?`.
- Pass `noteData` to the `WebView` component.

#### [MODIFY] [App.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/App.kt)
- Add a new state `var browserNoteData by remember { mutableStateOf<String?>(null) }`.
- Update the `openBrowser` lambda signature to `(String, String?) -> Unit` to accept both the URL and the note data.
- Pass `browserNoteData` to `BrowserScreen`.

#### [MODIFY] [NotesScreen.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/commonMain/kotlin/com/jayaram/tasktracker/NotesScreen.kt)
- Update the `openBrowser` parameter type to `(String, String?) -> Unit`.
- Ensure calls to `openBrowser` pass the note text as the second argument instead of appending it to the URL string.

### Android Implementation

#### [MODIFY] [WebView.android.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/androidMain/kotlin/com/jayaram/tasktracker/WebView.android.kt)
- Update the `actual fun WebView` signature to include `noteData: String?`.
- In `webViewClient.onPageFinished`, call `webView.evaluateJavascript("populateNoteData('$escapedNoteData')")` if `noteData` is present.
- Remove query parameter appending logic from the `update` block.

### iOS Implementation

#### [MODIFY] [WebView.ios.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/iosMain/kotlin/com/jayaram/tasktracker/WebView.ios.kt)
- Update the `actual fun WebView` signature to include `noteData: String?`.
- Use `evaluateJavaScript` to call `populateNoteData` after the page loads.
- Clean up URL construction logic to avoid parsing `note=` from the URL string.

### Web Assets

#### [MODIFY] [script.js](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/androidApp/src/main/assets/script.js)
- Add a global `populateNoteData(data)` function that updates the UI (page title and message field) when called by the native side.

## Verification Plan

### Automated Tests
- Build the project to ensure no compilation errors after signature changes.

### Manual Verification
1. Launch the app on Android and iOS.
2. Navigate to a note.
3. Click the note to open the "Contact Us" form.
4. Verify that the note's title appears as the header and the message field is pre-filled correctly.
5. Inspect the WebView URL (via logs) to ensure no sensitive data is present in the query parameters.
