package com.cwnextgen.hdwallpapers.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import com.cwnextgen.hdwallpapers.BuildConfig
import com.cwnextgen.hdwallpapers.R
import com.cwnextgen.hdwallpapers.admin.UploadsActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            if (BuildConfig.FLAVOR== "admin"){

                val intent = Intent(this, UploadsActivity::class.java)
                startActivity(intent)
                finish()
            }else{

                val intent = Intent(this, MainActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1000)

    }
}