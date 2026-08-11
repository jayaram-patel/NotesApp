# Walkthrough - Platform-Specific Web Assets

I have moved the web assets to be platform-specific, which simplifies the loading paths and makes the implementation more robust.

## Changes Made

### Android Assets
- Moved `contact.html`, `script.js`, and `style.css` back to `androidApp/src/main/assets/`.
- Updated [WebView.android.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/androidMain/kotlin/com/jayaram/tasktracker/WebView.android.kt) to load from the standard `file:///android_asset/` path.

### iOS Assets
- Created identical `contact.html`, `script.js`, and `style.css` in `iosApp/iosApp/`.
- Updated [WebView.ios.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/iosMain/kotlin/com/jayaram/tasktracker/WebView.ios.kt) to load these files from the app's Main Bundle.
- Updated the iOS `script.js` to include the specific bridge logic for `window.webkit.messageHandlers.iosBridge`.

### Cleanup
- Removed the unused assets from the `shared` module to keep the project clean.

## Important Note for iOS

> [!IMPORTANT]
> Even though I have created the files in the `iosApp/iosApp/` directory, you must manually add them to your Xcode project for them to be included in the build:
> 1. Open your project in Xcode.
> 2. Right-click the `iosApp` group and select **"Add Files to 'iosApp'..."**.
> 3. Select the `contact.html`, `script.js`, and `style.css` files.
> 4. Ensure **"Copy items if needed"** is checked and the **"iosApp" target** is selected.

## Verification
- Verified that Android now uses the simpler, standard asset path which is less prone to "File Not Found" errors.
