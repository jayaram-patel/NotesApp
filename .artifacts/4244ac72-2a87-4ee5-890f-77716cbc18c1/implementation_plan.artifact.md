# Implementation Plan - Fix WebView.ios.kt Compilation and Runtime Issues

Verify and fix `WebView.ios.kt` to ensure it compiles and works correctly on iOS.

## User Review Required

> [!IMPORTANT]
> The `WKUserContentController` on iOS holds a **weak reference** to its script message handlers. The current implementation creates `LoggerScriptMessageHandler` inside the `factory` block without storing it, which will lead to it being deallocated and the JavaScript-to-Kotlin bridge failing at runtime. I will move it to a `remember` block.

## Proposed Changes

### Shared Module

#### [MODIFY] [WebView.ios.kt](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/src/iosMain/kotlin/com/jayaram/tasktracker/WebView.ios.kt)

- Add necessary imports: `kotlinx.cinterop.*`.
- Add `@OptIn(ExperimentalForeignApi::class)` to the `WebView` composable.
- Use `remember` to persist `LoggerScriptMessageHandler` so it doesn't get garbage collected.
- Fix the `CGRectZero.readValue()` usage by ensuring the import is present.
- (Optional) Update `UIKitView` to a non-deprecated API if possible, or at least suppress the warning if the current usage is intentional.

## Verification Plan

### Automated Tests
- Run `analyze_file` again to ensure all errors and warnings are resolved.
- Attempt a gradle build for the iOS target (if possible in this environment).

### Manual Verification
- The user should run the app on an iOS simulator and verify that:
    1. The WebView loads (local "contact_us" and external URLs).
    2. The JavaScript bridge works (submitting the form in `contact.html` calls the Kotlin code).
    3. The title updates correctly.
