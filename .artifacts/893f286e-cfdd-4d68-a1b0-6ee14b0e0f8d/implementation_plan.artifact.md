# Implementation Plan - iOS Platform Completion

This plan outlines the exact changes needed to make the TaskTracker app fully functional on iOS, specifically focusing on the `WKWebView` implementation and asset synchronization.

## Proposed Changes

### [Shared Module (iosMain)]

#### [MODIFY] [WebView.ios.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/iosMain/kotlin/com/jayaram/tasktracker/WebView.ios.kt)
The current implementation only sets up the `WKWebView` once in the `factory` block. It needs to handle URL updates and page load events for `noteData` injection.

- **Add `update` block**: Use `loadRequest` or `loadFileURL` when the `url` parameter changes.
- **`WKNavigationDelegate`**: Implement a delegate to handle `didFinishNavigation` so that `populateNoteData` is called at the right time (after the page is ready).
- **`WKUserScript`**: Inject the `Android` shim using `WKUserScript` with `atDocumentStart` for better reliability.

### [iOS App Module]

#### [MODIFY] [script.js](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/iosApp/iosApp/script.js)
The iOS version of `script.js` is missing the `populateNoteData` function which is used by the shared code to pass note content to the contact form.

- **Add `populateNoteData(text)`**: Implement this function to update the DOM elements (`page-title` and `message`) with the provided note text.

## Verification Plan

### Automated Tests
- Not applicable for UI/Platform integration at this stage.

### Manual Verification
1. **Launch iOS App**: Run the app on an iOS Simulator.
2. **Open Folder**: Navigate into a folder.
3. **Open Contact Form**: Click on a note to open the contact form.
4. **Verify Note Data**: Ensure the contact form is pre-filled with "Regarding my note: [note text]".
5. **Submit Form**: Fill the form and submit. Verify that the submission count increases in the main screen (after returning and refreshing).
6. **External URLs**: Test opening a regular URL (e.g., https://www.google.com) to ensure it loads in the `WKWebView`.
