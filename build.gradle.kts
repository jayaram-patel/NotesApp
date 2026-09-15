plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.kotlinAndroid) apply false
    alias(libs.plugins.composeMultiplatform) apply false
    alias(libs.plugins.composeCompiler) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false

    alias(libs.plugins.sqldelight) apply false
}

allprojects {
    configurations.all {
        resolutionStrategy.eachDependency {
            if (requested.group == "androidx.compose.material3") {
                useVersion("1.3.0")
            } else if (requested.group.startsWith("androidx.compose")) {
                useVersion("1.7.0")
            }
            if (requested.group == "androidx.lifecycle") {
                useVersion("2.8.3")
            }
            if (requested.group == "androidx.annotation" && requested.name == "annotation") {
                useVersion("1.7.0")
            }
            if (requested.group == "androidx.core" && (requested.name == "core" || requested.name == "core-ktx")) {
                useVersion("1.13.1")
            }
        }
    }
}
