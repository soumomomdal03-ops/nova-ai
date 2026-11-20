package com.nova.ai

import android.app.Application
import com.nova.ai.di.NovaAppContainer

class NovaApplication : Application() {
    lateinit var container: NovaAppContainer
        private set

    override fun onCreate() {
        super.onCreate()
        container = NovaAppContainer(this)
    }
}
