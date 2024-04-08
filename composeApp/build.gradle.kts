import org.apache.tools.ant.util.JavaEnvUtils.VERSION_11
import org.jetbrains.compose.ExperimentalComposeLibrary
import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.jetbrainsCompose)
    kotlin("plugin.serialization")
    id("com.google.gms.google-services")
    id("com.google.relay") version "0.3.04"
}

kotlin {
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }
    
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "ComposeApp"
            isStatic = true
        }
    }
    
    sourceSets {
        
        androidMain.dependencies {
            implementation(libs.compose.ui.tooling.preview)
            implementation(libs.androidx.activity.compose)
            implementation("androidx.fragment:fragment-ktx:1.6.2")
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:32.7.1"))
            implementation(project.dependencies.platform("com.google.firebase:firebase-bom:32.7.1"))
            implementation ("com.microsoft.appcenter:appcenter-analytics:5.0.4")
            implementation ("com.microsoft.appcenter:appcenter-crashes:5.0.4")
            implementation ("com.microsoft.appcenter:appcenter-distribute:5.0.4")
            implementation ("com.microsoft.appcenter:appcenter-distribute-play:5.0.4")
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material)
            implementation(compose.ui)
            @OptIn(ExperimentalComposeLibrary::class)
            implementation(compose.components.resources)
            implementation("media.kamel:kamel-image:0.8.3")
            implementation("io.ktor:ktor-client-content-negotiation:2.3.5")
            implementation("io.ktor:ktor-serialization-kotlinx-json:2.3.5")
            implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.5.1")
            implementation("cafe.adriel.voyager:voyager-tab-navigator:1.0.0")
            implementation("cafe.adriel.voyager:voyager-bottom-sheet-navigator:1.0.0")
            implementation("cafe.adriel.voyager:voyager-transitions:1.0.0")
            api("dev.icerock.moko:mvvm-core:0.16.1") // only ViewModel, EventsDispatcher, Dispatchers.UI
            api("dev.icerock.moko:mvvm-compose:0.16.1")
            api("org.lighthousegames:logging:1.3.0")
            api("io.github.kevinnzou:compose-webview-multiplatform:1.8.6")
            implementation("dev.icerock.moko:biometry:0.4.0")
            implementation("dev.icerock.moko:biometry-compose:0.4.0")
        }
        iosMain.dependencies {
            implementation("io.ktor:ktor-client-darwin:2.3.5")
        }
    }
}

android {
    namespace = "org.example.carplace"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    sourceSets["main"].manifest.srcFile("src/androidMain/AndroidManifest.xml")
    sourceSets["main"].res.srcDirs("src/androidMain/res")
    sourceSets["main"].resources.srcDirs("src/commonMain/resources")

    defaultConfig {
        applicationId = "org.example.carplace"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
    flavorDimensions.add("distribute")
    productFlavors {
        create("appCenter") {
            dimension = "distribute"
        }
        create("googlePlay") {
            dimension = "distribute"
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    dependencies {
        debugImplementation(libs.compose.ui.tooling)
        implementation("io.ktor:ktor-client-android:2.3.5")
        implementation ("androidx.compose.material3:material3:1.1.2")
    }
}
dependencies {
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.core:core-ktx:+")
    implementation("androidx.core:core-ktx:+")
}

