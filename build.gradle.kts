// Top-level build file
plugins {
    // Change 8.2.0 to 8.4.0 to match the classpath
    id("com.android.application") version "8.4.0" apply false
    id("com.android.library") version "8.4.0" apply false

    // Use 1.9.22 for Kotlin to stay compatible with AGP 8.4.0
    id("org.jetbrains.kotlin.android") version "1.9.22" apply false
}