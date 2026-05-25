package com.wishvault.app

import android.app.Application
import com.wishvault.app.util.WishVaultLogger

class WishVaultApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        
        // Initialize the persistent file logger
        WishVaultLogger.init(this)
    }
}
