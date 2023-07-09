package com.cwnextgen.dslrphotographyposes.activities

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity
import com.cwnextgen.dslrphotographyposes.BuildConfig
import com.cwnextgen.dslrphotographyposes.R
import com.cwnextgen.dslrphotographyposes.admin.UploadsActivity

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        supportActionBar?.hide()
        Handler(Looper.getMainLooper()).postDelayed({
            if (BuildConfig.FLAVOR == "admin") {

                val intent = Intent(this, UploadsActivity::class.java)
                startActivity(intent)
                finish()
            } else {

                val intent = Intent(this, CategoriesActivity::class.java)
                startActivity(intent)
                finish()
            }
        }, 1500)

    }
}