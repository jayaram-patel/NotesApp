# TaskTracker

This is a Kotlin Multiplatform project targeting Android and iOS.

## Project Structure

- [iosApp](./iosApp) contains the iOS application. Even if you share your UI with Compose Multiplatform, you need this entry point for your iOS app. This is also where you should add SwiftUI code for your project.
- [shared](./shared/src) contains the code shared across your Compose Multiplatform applications. It contains several subfolders:
  - [commonMain](./shared/src/commonMain/kotlin) is for code that is common across all targets.
  - Platform-specific folders contain Kotlin code compiled only for the platform indicated in the folder name:
    - [androidMain](./shared/src/androidMain/kotlin) is for Android-specific Kotlin code and platform calls.
    - [iosMain](./shared/src/iosMain/kotlin) is for iOS-specific Kotlin code and platform calls.

## Running the Apps

Use the run configurations provided by the run widget in your IDE's toolbar, or use these commands and options:

- **Android app:** `./gradlew :androidApp:assembleDebug`
- **iOS app:** Open the [iosApp](./iosApp) directory in Xcode and run it from there.

## Running Tests

Use the run button in your IDE's editor gutter, or run tests using Gradle tasks:

- **Android tests:** `./gradlew :shared:testAndroidHostTest`
- **iOS tests:** `./gradlew :shared:iosSimulatorArm64Test`

---

Learn more about [Kotlin Multiplatform](https://www.jetbrains.com/help/kotlin-multiplatform-dev/get-started.html).
