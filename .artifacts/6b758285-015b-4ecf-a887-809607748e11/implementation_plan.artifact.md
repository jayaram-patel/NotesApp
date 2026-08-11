# Implementation Plan - Platform-Specific Web Assets

The goal is to move away from shared resources and instead use platform-specific web assets for Android and iOS. This simplifies path management and allows for platform-specific tweaks to the HTML/JS.

## User Review Required

> [!NOTE]
> This approach involves maintaining two sets of identical (or similar) files. While easier to debug paths, you will need to update both if you change the form's logic.

## Proposed Changes

### [Component] Assets Management

#### [MOVE] Assets to Android
- Move `contact.html`, `script.js`, and `style.css` from `shared/src/commonMain/composeResources/files/` back to `androidApp/src/main/assets/`.

#### [NEW] Assets for iOS
- Copy/Create `contact.html`, `script.js`, and `style.css` in `iosApp/iosApp/` (and ensure they are added to the Xcode project's Resources).

### [Component] shared:androidMain

#### [MODIFY] [WebView.android.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/androidMain/kotlin/com/jayaram/tasktracker/WebView.android.kt)
- Revert the `loadUrl` path to use the standard Android assets prefix: `file:///android_asset/contact.html`.

### [Component] shared:iosMain

#### [MODIFY] [WebView.ios.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/iosMain/kotlin/com/jayaram/tasktracker/WebView.ios.kt)
- Update the `loadFileURL` logic to look for the files directly in the main app bundle instead of the shared framework bundle.

## Verification Plan

### Manual Verification
- **Android**: Deploy and verify the form loads from `androidApp/assets`.
- **iOS**: Open in Xcode, ensure files are in the "Copy Bundle Resources" phase, and verify loading.
