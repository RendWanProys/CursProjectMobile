package com.example.cursprojectmobile

import android.app.Application

class FoodCouriersApp : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        lateinit var instance: FoodCouriersApp
            private set
    }
}
