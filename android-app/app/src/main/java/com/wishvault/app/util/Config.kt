package com.wishvault.app.util

enum class Environment(val baseUrl: String) {
    DEV_LOCAL("http://10.0.2.2:8000"), // Android Emulator to Host localhost
    STAGING("https://staging-api.wishvault.app"),
    PRODUCTION("https://api.wishvault.app")
}

object Config {
    // Easily toggleable for now. Future architecture could use buildConfigField
    val currentEnvironment = Environment.DEV_LOCAL
}
