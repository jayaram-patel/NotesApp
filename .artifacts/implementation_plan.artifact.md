# Fix AGP 9.0 Compatibility and Ensure "Android Studio 2025" Compatibility

The project is currently using an experimental version of the Android Gradle Plugin (AGP 9.3.1) and Kotlin (2.4.0). This has led to a compatibility error in the `shared` module, where the `org.jetbrains.kotlin.multiplatform` plugin is conflicting with the Android plugin. Additionally, "Android Studio 2025" (Ladybug/Meerkat) typically uses stable AGP 8.x or early 9.x versions, making the current setup incompatible with more stable environments.

## User Review Required

> [!IMPORTANT]
> This plan involves downgrading AGP, Kotlin, and Compose Multiplatform to stable versions. This is recommended for team collaboration and compatibility with standard Android Studio releases (like the "2025" versions mentioned).

> [!WARNING]
> We will switch the `shared` module to use the standard `com.android.library` plugin if we downgrade to AGP 8.7, as it is more stable and widely supported in that version. If you prefer to keep using the new `com.android.kotlin.multiplatform.library` plugin, we must ensure it is correctly configured for the target AGP version.

## Proposed Changes

### Version Management

#### [MODIFY] [libs.versions.toml](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/gradle/libs.versions.toml)
- Downgrade `agp` to `8.7.3` (Stable).
- Downgrade `kotlin` to `2.1.0` (Stable).
- Downgrade `composeMultiplatform` to `1.7.3` (Stable).
- Add `androidLibrary = { id = "com.android.library", version.ref = "agp" }` to the `[plugins]` section.

---

### Shared Module Configuration

#### [MODIFY] [build.gradle.kts](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/shared/build.gradle.kts)
- Replace `alias(libs.plugins.androidMultiplatformLibrary)` with `alias(libs.plugins.androidLibrary)`.
- Reconfigure the `kotlin` block to use `androidTarget()` instead of the new `android { ... }` DSL (which is specific to the Multiplatform Android plugin).
- Move the `android { ... }` configuration to the top level, as required by the standard `com.android.library` plugin.

---

### Root Project Configuration

#### [MODIFY] [build.gradle.kts](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/build.gradle.kts)
- Add `alias(libs.plugins.androidLibrary) apply false` to the `plugins` block.

---

### Global Properties

#### [MODIFY] [gradle.properties](file:///C:/Users/jayarampatel/AndroidStudioProjects/NotesApp/gradle.properties)
- Add flags to ensure compatibility if needed, though downgrading should solve the main issue.

## Verification Plan

### Automated Tests
- Run `./gradlew :shared:assembleDebug` to verify the shared module builds.
- Run `./gradlew :androidApp:assembleDebug` to verify the entire app builds.

### Manual Verification
- Perform a Gradle Sync in Android Studio to ensure the IDE correctly recognizes the new configuration.
- Verify that the error regarding AGP 9.0 compatibility is resolved.
