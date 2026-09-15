try {
    val pe = Class.forName("java.lang.ProcessEnvironment")
    val envField = pe.getDeclaredField("theCaseInsensitiveEnvironment")
    envField.isAccessible = true
    @Suppress("UNCHECKED_CAST")
    val envMap = envField.get(null) as MutableMap<String, String>
    envMap.remove("ANDROID_PREFS_ROOT")
} catch (_: Throwable) {
    try {
        val pe = Class.forName("java.lang.ProcessEnvironment")
        val envField = pe.getDeclaredField("theEnvironment")
        envField.isAccessible = true
        @Suppress("UNCHECKED_CAST")
        val envMap = envField.get(null) as MutableMap<String, String>
        envMap.remove("ANDROID_PREFS_ROOT")
    } catch (_: Throwable) {}
}

rootProject.name = "TaskTracker"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
    }
}

include(":androidApp")
include(":shared")
