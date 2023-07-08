package com.cwnextgen.hdwallpapers.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import com.cwnextgen.hdwallpapers.BuildConfig
import com.cwnextgen.hdwallpapers.R
import com.cwnextgen.hdwallpapers.activities.base.BaseActivity
import com.cwnextgen.hdwallpapers.adapters.HomeAdapter
import com.cwnextgen.hdwallpapers.admin.UploadsActivity
import com.cwnextgen.hdwallpapers.databinding.ActivityWallpapersBinding
import com.cwnextgen.hdwallpapers.models.CategoriesModel
import com.cwnextgen.hdwallpapers.models.WallpaperModel
import com.cwnextgen.hdwallpapers.utils.AppConstants
import com.cwnextgen.hdwallpapers.utils.OnItemClick
import com.cwnextgen.hdwallpapers.utils.ProgressLoading.displayLoading
import com.cwnextgen.hdwallpapers.utils.firestore
import com.cwnextgen.hdwallpapers.utils.isShow
import com.cwnextgen.hdwallpapers.utils.showToast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback

class WallpapersActivity : BaseActivity(), OnItemClick {
    private lateinit var binding: ActivityWallpapersBinding
    private val TAG = WallpapersActivity::class.java.simpleName
    private var wallpaperModel = mutableListOf<WallpaperModel>()
    private var category = CategoriesModel()
    lateinit var adapter: HomeAdapter
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest
    lateinit var adRequest1: AdRequest
    override fun onCreate() {
        binding = ActivityWallpapersBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MobileAds.initialize(this@WallpapersActivity)
        adRequest = AdRequest.Builder().build()
        adRequest1 = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        askNotificationPermission()

        loadAdRequest()
    }

    private fun loadAdRequest() {

        InterstitialAd.load(this@WallpapersActivity,
            getString(R.string.interstitial_ad_unit_id),
            adRequest1,
            object : InterstitialAdLoadCallback() {
                override fun onAdFailedToLoad(adError: LoadAdError) {
                    adError?.message?.let { Log.d(TAG, it) }
                    mInterstitialAd = null
                }

                override fun onAdLoaded(interstitialAd: InterstitialAd) {
                    Log.d(TAG, "Ad was loaded.")
                    mInterstitialAd = interstitialAd
                }
            })
    }

    override fun clicks() {
        binding.floatingActionButton.setOnClickListener {
            startActivity(
                Intent(this, UploadsActivity::class.java).putExtra(
                    AppConstants.BUNDLE, category
                )
            )
        }

        if (BuildConfig.FLAVOR == "dev") {
            binding.floatingActionButton.isShow()
        } else {
            binding.floatingActionButton.isShow(false)
        }

    }

    override fun initAdapter() {
        adapter = HomeAdapter(wallpaperModel, this)
        binding.recyclerView.adapter = adapter
    }

    override fun apiAndArgs() {
        bundle = intent.extras
        if (bundle != null) {
            category = bundle!!.getSerializable(AppConstants.BUNDLE) as CategoriesModel
            supportActionBar?.title = category.categoryTitle?.uppercase()
            fetchData()
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

    private fun askNotificationPermission() {
        // This is only necessary for API level >= 33 (TIRAMISU)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
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
        wallpaperModel.clear()
        displayLoading()
        val postsCollection = firestore().collection(category.id.toString())

// Perform a query to retrieve the posts
        postsCollection.get().addOnSuccessListener { querySnapshot ->
            for (documentSnapshot in querySnapshot.documents) {
                // Retrieve post data
                val model = documentSnapshot.toObject(WallpaperModel::class.java)
                if (model!!.active!!) {
                    wallpaperModel.add(model)
                }
            }
            adapter.updateData(wallpaperModel)
            displayLoading(false)
        }.addOnFailureListener { e ->
            displayLoading(false)
            // Handle any errors that occurred while querying the posts collection
            e.localizedMessage?.let { showToast(it) }
        }

    }

    override fun onClick(position: Int, type: String?, data: Any?) {
        var model = wallpaperModel[position]
        openDetailPage(model)
        // TODO: check why ads shows in on resume rather than button click
   //     showInterstitial(model)
    }

    private fun openDetailPage(model: WallpaperModel) {
        mInterstitialAd = null;
        startActivity(
            Intent(this, DetailActivity::class.java).putExtra(
                AppConstants.BUNDLE, model
            )
        )

    }

    private fun showInterstitial(model: WallpaperModel) {
        if (mInterstitialAd != null) {
            mInterstitialAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
                override fun onAdDismissedFullScreenContent() {
                    Log.d(TAG, "Ad was dismissed.")
                    openDetailPage(model)
                }

                override fun onAdFailedToShowFullScreenContent(p0: AdError) {
                    Log.d(TAG, "Ad failed to show.")
                    openDetailPage(model)
                }

                override fun onAdShowedFullScreenContent() {
                    Log.d(TAG, "Ad showed fullscreen content.")
                    mInterstitialAd = null;
                    openDetailPage(model)
                }
            }
            mInterstitialAd?.show(this)
        } else {
            Log.d("TAG", "'The interstitial ad wasn\'t ready yet.'")
            openDetailPage(model)
        }
    }
}