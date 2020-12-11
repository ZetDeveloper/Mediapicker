package com.jiren.mediapicker

import android.app.Application
import android.content.Context
import androidx.appcompat.app.AppCompatDelegate

class AppPicker: Application() {
        init {
            instance = this
        }

    override fun onCreate() {
        super.onCreate()
        //Deshabilito el modo noche para evitar que difiera de zepplin
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
    }


    companion object {
             var instance: AppPicker? = null

            fun applicationContext() : Context {
                return instance!!.applicationContext
            }
        }
}