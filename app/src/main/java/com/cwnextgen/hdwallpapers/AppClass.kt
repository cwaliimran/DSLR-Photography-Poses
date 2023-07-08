package com.cwnextgen.hdwallpapers

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.res.Configuration
import android.content.res.Resources
import android.os.Build
import com.cwnextgen.hdwallpapers.utils.SharedPref
import java.util.Locale


class AppClass : Application() {
    override fun onCreate() {
        super.onCreate()
        myApp = this
        sharedPref = SharedPref(this)

        createNotificationChannel()
    }


    companion object {
        private const val TAG = "AppClass"
        var myApp: AppClass? = null
        val instance get() = myApp!!
        lateinit var sharedPref: SharedPref

        fun changeLocale(context: Context, locale: String) {
            val res: Resources = context.resources
            val conf: Configuration = res.configuration
            conf.setLocale(Locale(locale))
            res.updateConfiguration(conf, res.displayMetrics)
        }
    }

    // Customize the notification channel as you wish. This is only for a bare minimum example
    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "WLP", getString(R.string.app_name), NotificationManager.IMPORTANCE_HIGH
            )
            val manager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }


}