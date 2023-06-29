package com.cwnextgen.quranislamicwallpaper.activities

import android.Manifest
import android.app.WallpaperManager
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.cwnextgen.quranislamicwallpaper.R
import com.cwnextgen.quranislamicwallpaper.activities.base.BaseActivity
import com.cwnextgen.quranislamicwallpaper.databinding.ActivityDetailBinding
import com.cwnextgen.quranislamicwallpaper.models.MainModel
import com.cwnextgen.quranislamicwallpaper.utils.AlertDialogListener
import com.cwnextgen.quranislamicwallpaper.utils.AppConstants
import com.cwnextgen.quranislamicwallpaper.utils.ShareImage.getBitmapFromView
import com.cwnextgen.quranislamicwallpaper.utils.genericDialog
import com.cwnextgen.quranislamicwallpaper.utils.openAppSettings
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.io.InputStream
import java.net.HttpURLConnection
import java.net.URL


class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding

    private var mainModel = MainModel()
    lateinit var bitmap: Bitmap


    override fun onCreate() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        askWallpaperPermission()
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
    }

    override fun clicks() {
        binding.btnSetwallpaper.setOnClickListener {
            setWallpaper()
        }
        binding.btnSetwallpaperLock.setOnClickListener {
            setLockScreenWallpaper()
        }
        binding.ivShare.setOnClickListener {
            getBitmapFromView(binding.cardView)
        }
    }

    override fun apiAndArgs() {
        bundle = intent.extras
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                mainModel = bundle!!.getSerializable(AppConstants.BUNDLE, MainModel::class.java)!!
            } else {
                mainModel = bundle!!.getSerializable(AppConstants.BUNDLE) as MainModel
            }
            binding.data = mainModel
        }
    }

    private fun setWallpaper() {
        val drawable = binding.imageView.drawable

        if (drawable is BitmapDrawable) {
            // Handle BitmapDrawable
            val bitmapDrawable = drawable as BitmapDrawable
            bitmap = bitmapDrawable.bitmap
            // Use the bitmap or perform any necessary operations
        } else if (drawable is TransitionDrawable) {
            // Handle TransitionDrawable
            val transitionDrawable = drawable as TransitionDrawable
            val finalDrawable =
                transitionDrawable.getDrawable(transitionDrawable.numberOfLayers - 1)
            if (finalDrawable is BitmapDrawable) {
                val bitmapDrawable = finalDrawable as BitmapDrawable
                bitmap = bitmapDrawable.bitmap
                // Use the bitmap or perform any necessary operations
            }
        } else {
            // Handle other drawable types
        }
        try {
            val wallpaperManager = WallpaperManager.getInstance(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_SYSTEM)
            } else {
                wallpaperManager.setBitmap(bitmap)
            }
            Toast.makeText(this, "Home screen wallpaper set successfully", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to set home screen wallpaper", Toast.LENGTH_SHORT).show()
        }


    }

    private fun setLockScreenWallpaper() {

        val drawable = binding.imageView.drawable

        if (drawable is BitmapDrawable) {
            // Handle BitmapDrawable
            val bitmapDrawable = drawable as BitmapDrawable
            bitmap = bitmapDrawable.bitmap
            // Use the bitmap or perform any necessary operations
        } else if (drawable is TransitionDrawable) {
            // Handle TransitionDrawable
            val transitionDrawable = drawable as TransitionDrawable
            val finalDrawable =
                transitionDrawable.getDrawable(transitionDrawable.numberOfLayers - 1)
            if (finalDrawable is BitmapDrawable) {
                val bitmapDrawable = finalDrawable as BitmapDrawable
                bitmap = bitmapDrawable.bitmap
                // Use the bitmap or perform any necessary operations
            }
        } else {
            // Handle other drawable types
        }


        try {
            val wallpaperManager = WallpaperManager.getInstance(this)
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                wallpaperManager.setBitmap(bitmap, null, true, WallpaperManager.FLAG_LOCK)
            } else {
                Toast.makeText(
                    this,
                    "Lock screen wallpaper can only be set on Android N and above",
                    Toast.LENGTH_SHORT
                ).show()
            }
            Toast.makeText(this, "Lock screen wallpaper set successfully", Toast.LENGTH_SHORT)
                .show()
        } catch (e: Exception) {
            Toast.makeText(this, "Failed to set lock screen wallpaper", Toast.LENGTH_SHORT).show()
        }

    }

    private fun setWallpaperFromUrl() {


        /*   GlobalScope.launch(Dispatchers.IO) {
               val url = URL(mainModel.imageUrl)
               val connection: HttpURLConnection = url.openConnection() as HttpURLConnection
               connection.doInput = true
               connection.connect()

               val inputStream: InputStream = connection.inputStream
               val bitmap: Bitmap = BitmapFactory.decodeStream(inputStream)

               val wallpaperManager = WallpaperManager.getInstance(this@DetailActivity)
               try {
                   wallpaperManager.setBitmap(bitmap)
                   // Alternatively, you can use wallpaperManager.setResource(R.drawable.my_wallpaper)
                   // if you want to set the wallpaper from a local drawable resource

                   // Show a success message or perform any other actions after setting the wallpaper
               } catch (e: Exception) {
                   e.printStackTrace()
                   // Show an error message or handle the exception
               } finally {
                   inputStream.close()
                   connection.disconnect()
               }
           }*/
    }

    private fun askWallpaperPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.SET_WALLPAPER
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            // FCM SDK (and your app) can post notifications.
        } else if (shouldShowRequestPermissionRationale(Manifest.permission.SET_WALLPAPER)) {
            denyExplanation()
        } else {
            // Directly ask for the permission
            requestPermissionLauncher.launch(Manifest.permission.SET_WALLPAPER)
        }

    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            //set wallpaper
            setWallpaperFromUrl()
        } else {
            denyExplanation()
        }
    }

    private fun denyExplanation() {
        genericDialog(
            object : AlertDialogListener {
                override fun onYesClick(data: Any?) {
                    openAppSettings()
                }
            },
            getString(R.string.permissions),
            getString(R.string.permission_needed),
            null,
            cancelable = true,
            hideNoBtn = false,
            getString(R.string.open_settings),
            getString(R.string.cancel)
        )
    }


    // Called when leaving the activity
    public override fun onPause() {
        binding.adView.pause()
        super.onPause()
    }

    // Called when returning to the activity
    public override fun onResume() {
        super.onResume()
        binding.adView.resume()
    }

    // Called before the activity is destroyed
    public override fun onDestroy() {
        binding.adView.destroy()
        super.onDestroy()
    }
}