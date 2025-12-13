package com.example.desde_mi_rincon_app_01
import androidx.annotation.Keep

@Keep
class Greeting {
    private val platform = getPlatform()

    fun greet(): String {
        return "Hello, ${platform.name}!"
    }
}