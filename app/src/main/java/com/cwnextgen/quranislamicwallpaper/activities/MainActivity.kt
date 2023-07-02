package com.cwnextgen.quranislamicwallpaper.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.cwnextgen.quranislamicwallpaper.activities.base.BaseActivity
import com.cwnextgen.quranislamicwallpaper.adapters.HomeAdapter
import com.cwnextgen.quranislamicwallpaper.admin.UploadsActivity
import com.cwnextgen.quranislamicwallpaper.databinding.ActivityMainBinding
import com.cwnextgen.quranislamicwallpaper.models.MainModel
import com.cwnextgen.quranislamicwallpaper.utils.AppConstants
import com.cwnextgen.quranislamicwallpaper.utils.OnItemClick
import com.cwnextgen.quranislamicwallpaper.utils.ProgressLoading.displayLoading
import com.cwnextgen.quranislamicwallpaper.utils.firestore
import com.cwnextgen.quranislamicwallpaper.utils.showToast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds

class MainActivity : BaseActivity(), OnItemClick {
    private lateinit var binding: ActivityMainBinding
    private val TAG = MainActivity::class.java.simpleName
    private var mainModel = mutableListOf<MainModel>()
    lateinit var adapter: HomeAdapter

    override fun onCreate() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        askNotificationPermission()
        fetchData()
    }

    override fun clicks() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(Intent(this, UploadsActivity::class.java))
        }
    }

    override fun initAdapter() {
        adapter = HomeAdapter(mainModel, this)
        binding.recyclerView.adapter = adapter
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

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) == PackageManager.PERMISSION_GRANTED
            ) {
                // FCM SDK (and your app) can post notifications.
            } else if (shouldShowRequestPermissionRationale(Manifest.permission.POST_NOTIFICATIONS)) {
                // denyExplanation()
            } else {
                // Directly ask for the permission
                requestPermissionLauncher.launch(Manifest.permission.POST_NOTIFICATIONS)
            }
        }
    }

    // Declare the launcher at the top of your Activity/Fragment:
    private val requestPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission(),
    ) { isGranted: Boolean ->
        if (isGranted) {
            // FCM SDK (and your app) can post notifications.
        } else {
            // denyExplanation()
        }
    }


    private fun fetchData() {
        mainModel.clear()
        displayLoading()
        val postsCollection = firestore().collection(AppConstants.TBL_WALLPAPERS)

// Perform a query to retrieve the posts
        postsCollection.get()
            .addOnSuccessListener { querySnapshot ->
                for (documentSnapshot in querySnapshot.documents) {
                    // Retrieve post data
                    val model = documentSnapshot.toObject(MainModel::class.java)
                    if (model!!.active!!){
                        mainModel.add(model)
                    }
                }
                adapter.updateData(mainModel)
                displayLoading(false)
            }
            .addOnFailureListener { e ->
                displayLoading(false)
                // Handle any errors that occurred while querying the posts collection
                e.localizedMessage?.let { showToast(it) }
            }

    }

    override fun onClick(position: Int, type: String?, data: Any?) {
        startActivity(
            Intent(this, DetailActivity::class.java).putExtra(
                AppConstants.BUNDLE,
                mainModel[position]
            )
        )
    }
}