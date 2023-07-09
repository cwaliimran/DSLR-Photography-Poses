package com.cwnextgen.dslrphotographyposes.activities

import android.Manifest
import android.app.Activity
import android.content.ContentValues
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.TransitionDrawable
import android.os.Build
import android.os.Environment
import android.provider.MediaStore
import android.util.Log
import android.widget.Toast
import androidx.core.app.ActivityCompat
import com.cwnextgen.dslrphotographyposes.R
import com.cwnextgen.dslrphotographyposes.activities.base.BaseActivity
import com.cwnextgen.dslrphotographyposes.databinding.ActivityDetailBinding
import com.cwnextgen.dslrphotographyposes.utils.AppConstants
import com.cwnextgen.dslrphotographyposes.utils.ShareImage.getBitmapFromView
import com.cwnextgen.dslrphotographyposes.utils.showToast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.OnUserEarnedRewardListener
import com.google.android.gms.ads.rewarded.RewardItem
import com.google.android.gms.ads.rewarded.RewardedAd
import com.google.android.gms.ads.rewarded.RewardedAdLoadCallback
import com.network.models.ImagesModel
import java.io.File
import java.io.FileOutputStream


class DetailActivity : BaseActivity() {
    private lateinit var binding: ActivityDetailBinding
    private val TAG = "DetailActivity"
    private var imagesModel = ImagesModel()
    lateinit var bitmap: Bitmap
    private val REQUEST_CODE_PERMISSIONS = 123
    private val permissions = arrayOf(
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private var mRewardedAd: RewardedAd? = null


    override fun onCreate() {
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        loadRewardApp()

    }

    override fun clicks() {

        binding.ivShare.setOnClickListener {
            getBitmapFromView(binding.cardView)
        }

        binding.saveImageButton.setOnClickListener {

            // Request permissions if needed
            if (!hasPermissions()) {
                ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_PERMISSIONS)
            } else {
                showRewardedVideo("saveImg")
            }

        }
    }

    override fun apiAndArgs() {
        bundle = intent.extras
        if (bundle != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                imagesModel =
                    bundle!!.getSerializable(AppConstants.BUNDLE, ImagesModel::class.java)!!
            } else {
                imagesModel = bundle!!.getSerializable(AppConstants.BUNDLE) as ImagesModel
            }
            binding.data = imagesModel

        }
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


    private fun hasPermissions(): Boolean {
        for (permission in permissions) {
            if (ActivityCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                return false
            }
        }
        return true
    }

    private fun saveImageToGallery() {
        var bitmap: Bitmap? = null


        val drawable = binding.imageView.drawable

        if (drawable is BitmapDrawable) {
            // Handle BitmapDrawable
            val bitmapDrawable = drawable
            bitmap = bitmapDrawable.bitmap
            // Use the bitmap or perform any necessary operations
        } else if (drawable is TransitionDrawable) {
            // Handle TransitionDrawable
            val transitionDrawable = drawable
            val finalDrawable =
                transitionDrawable.getDrawable(transitionDrawable.numberOfLayers - 1)
            if (finalDrawable is BitmapDrawable) {
                val bitmapDrawable = finalDrawable
                bitmap = bitmapDrawable.bitmap
                // Use the bitmap or perform any necessary operations
            }
        } else {
            // Handle other drawable types
        }


        val contentValues = ContentValues().apply {
            put(MediaStore.MediaColumns.DISPLAY_NAME, "${System.currentTimeMillis()}.jpg")
            put(MediaStore.MediaColumns.MIME_TYPE, "image/jpeg")
            put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES)
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Use MediaStore API for Android Q and above
            val resolver = contentResolver
            val imageUri =
                resolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues)

            imageUri?.let { uri ->
                val outputStream = resolver.openOutputStream(uri)
                outputStream?.use {
                    bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                    Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            // Use the deprecated method for Android versions below Q
            val imagesDir =
                Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES)
            val image =
                File(imagesDir, contentValues.get(MediaStore.MediaColumns.DISPLAY_NAME) as String)
            val outputStream = FileOutputStream(image)

            outputStream.use {
                bitmap?.compress(Bitmap.CompressFormat.JPEG, 100, it)
                Toast.makeText(this, "Image saved to gallery", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == REQUEST_CODE_PERMISSIONS) {
            if (hasPermissions()) {
                // Permissions granted
                // Continue with your code logic here
                showRewardedVideo("saveImg")
            } else {
                // Permissions denied
                // Handle the situation accordingly (e.g., show a message or disable functionality)
                showToast("Gallery permissions needed")
            }
        }
    }

    ///////////////////////////////////////////////////////////////////////////
    // ads
    ///////////////////////////////////////////////////////////////////////////


    private fun loadRewardApp() {

        MobileAds.initialize(this) {}

        val adRequest = AdRequest.Builder().build()

        RewardedAd.load(
            this,
            getString(R.string.reward_ad_unit_id),
            adRequest,
            object : RewardedAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError?.message?.let { Log.d(TAG, it) }
                    mRewardedAd = null
                }

                override fun onAdLoaded(rewardedAd: RewardedAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mRewardedAd = rewardedAd
                }
            })
    }

    private fun showRewardedVideo(type: String) {
        if (mRewardedAd != null) {
            val activityContext: Activity = this
            mRewardedAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    // Don't forget to set the ad reference to null so you don't show the ad a second time.
                    mRewardedAd = null
                    userReward(type)
                }

                override fun onAdFailedToShowFullScreenContent(adError: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    userReward(type)
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    // Called when ad is dismissed.
                    userReward(type)
                }
            }

            mRewardedAd?.show(activityContext, OnUserEarnedRewardListener() {
                fun onUserEarnedReward(rewardItem: RewardItem) {
                    // Handle the reward
                    Log.d(TAG, "The user was rewarded with ${rewardItem.amount} ${rewardItem.type}")
                    userReward(type)
                }
            })
        } else {
            Log.d("TAG", "The rewarded ad wasn't ready yet.")
            userReward(type)
        }
    }

    private fun userReward(type: String) {
        when (type) {
            "saveImg" -> {
                saveImageToGallery()
            }

            else -> {}
        }

    }


}