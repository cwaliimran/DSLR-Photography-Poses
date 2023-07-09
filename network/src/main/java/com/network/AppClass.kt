package com.network

import android.app.Application
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import java.util.Locale


class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
        sharedPref = SharedPref(this)

    }


    companion object {
        private const val TAG = "AppClass"
        lateinit var instance: AppClass
            private set
        lateinit var sharedPref: SharedPref

        fun changeLocale(context: Context, locale: String) {
            val res: Resources = context.resources
            val conf: Configuration = res.configuration
            conf.setLocale(Locale(locale))
            res.updateConfiguration(conf, res.displayMetrics)
        }
    }

}