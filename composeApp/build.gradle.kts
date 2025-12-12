import org.jetbrains.compose.desktop.application.dsl.TargetFormat
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.googleGmsGoogleServices)
}

kotlin {
    androidTarget {
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_11)
        }
    }
    
    sourceSets {
        androidMain.dependencies {
            implementation(compose.preview)
            implementation(libs.androidx.activity.compose)
        }
        commonMain.dependencies {
            implementation(compose.runtime)
            implementation(compose.foundation)
            implementation(compose.material3)
            implementation(compose.ui)
            implementation(compose.components.resources)
            implementation(compose.components.uiToolingPreview)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "com.example.desde_mi_rincon_app_01"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "com.example.desde_mi_rincon_app_01"
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
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
}

dependencies {
    // --- TUS DEPENDENCIAS ORIGINALES (Firebase & Credenciales) ---
    implementation(libs.firebase.auth)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)
    implementation(libs.firebase.firestore)

    // --- SOLUCIÓN AL ERROR DE ÍCONOS ---
    // Esta es la librería que te falta para usar Icons.Filled.EmojiEvents, Star, etc.
    // Nota: Si usas BOM, puedes quitar la versión ":1.7.6" al final.
    implementation("androidx.compose.material:material-icons-extended:1.7.6")


    // --- INTERFAZ DE USUARIO (COMPOSE MATERIAL 3) ---
    // Asegúrate de tener estas para que funcionen los componentes de UI
    implementation(platform("androidx.compose:compose-bom:2024.12.01")) // BOM para gestionar versiones
    implementation("androidx.compose.ui:ui")
    implementation("androidx.compose.ui:ui-graphics")
    implementation("androidx.compose.ui:ui-tooling-preview")
    // Cambia la 1.2.1 por la 1.3.1
    implementation("androidx.compose.material3:material3:1.3.1")

    // --- NAVEGACIÓN ---
    // Has puesto la versión 2.9.6, pero la estable actual ronda la 2.8.5.
    // Si la 2.9.6 te da error, baja a la 2.8.5.
    implementation("androidx.navigation:navigation-compose:2.8.5")

    // --- HERRAMIENTAS DE DEBUG ---
    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")


    implementation("com.google.firebase:firebase-storage-ktx:21.0.0") // O la versión que estés usando

    implementation("io.coil-kt:coil-compose:2.6.0")

    // 1. IMPORTANTE: Iconos Extendidos
    // (Necesario para Icons.Outlined.Brush, Icons.Outlined.Edit, etc.)
    implementation("androidx.compose.material:material-icons-extended:1.6.0")

    // 2. ViewModel para Compose
    // (Necesario para la función: viewModel() dentro de los Composables)
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.7.0")

    // 3. Navegación
    // (Para el NavHost y pasar argumentos entre pantallas)
    implementation("androidx.navigation:navigation-compose:2.7.6")

    implementation("androidx.compose.animation:animation:1.10.0")

    implementation("com.pierfrancescosoffritti.androidyoutubeplayer:core:12.1.0")

    implementation("androidx.compose.foundation:foundation:1.8.0") // O la versión que uses


    // Paging 3 para Compose
    implementation("androidx.paging:paging-runtime-ktx:3.2.1")
    implementation("androidx.paging:paging-compose:3.2.1")

    // Para usar .await() con Firebase
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-play-services:1.7.3")
}

