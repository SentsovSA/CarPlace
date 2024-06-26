plugins {
    // this is necessary to avoid the plugins to be loaded multiple times
    // in each subproject's classloader
    alias(libs.plugins.androidApplication) apply false
    alias(libs.plugins.androidLibrary) apply false
    alias(libs.plugins.jetbrainsCompose) apply false
    alias(libs.plugins.kotlinMultiplatform) apply false
    id("org.jetbrains.kotlin.android") version "1.9.21" apply false
    kotlin("plugin.serialization") version "1.9.0" apply false
    id("com.google.gms.google-services") version "4.4.0" apply false
}