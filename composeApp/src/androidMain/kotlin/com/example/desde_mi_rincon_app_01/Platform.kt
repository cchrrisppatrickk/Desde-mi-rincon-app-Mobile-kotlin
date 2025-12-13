package com.example.desde_mi_rincon_app_01

import android.os.Build
import androidx.annotation.Keep

@Keep
class AndroidPlatform {
    val name: String = "Android ${Build.VERSION.SDK_INT}"
}

fun getPlatform() = AndroidPlatform()