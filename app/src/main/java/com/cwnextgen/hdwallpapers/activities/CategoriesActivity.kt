package com.cwnextgen.hdwallpapers.activities

import android.content.Intent
import com.cwnextgen.hdwallpapers.R
import com.cwnextgen.hdwallpapers.activities.base.BaseActivity
import com.cwnextgen.hdwallpapers.adapters.CategoriesAdapter
import com.cwnextgen.hdwallpapers.databinding.ActivityCategoriesBinding
import com.cwnextgen.hdwallpapers.models.CategoriesModel
import com.cwnextgen.hdwallpapers.utils.AppConstants
import com.cwnextgen.hdwallpapers.utils.OnItemClick
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd

class CategoriesActivity : BaseActivity(), OnItemClick {
    private lateinit var binding: ActivityCategoriesBinding
    private val TAG = CategoriesActivity::class.java.simpleName
    private var mainModel = mutableListOf<CategoriesModel>()
    lateinit var adapter: CategoriesAdapter
    private var appOpenAd: AppOpenAd? = null

    override fun onCreate() {
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.app_name)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        loadAppOpenAd()
    }

    private fun loadAppOpenAd() {
        val adRequest = AdRequest.Builder().build()

        AppOpenAd.load(
            this@CategoriesActivity, getString(R.string.openapp_ad_unit_id), adRequest, object : AppOpenAd.AppOpenAdLoadCallback() {
                override fun onAdLoaded(ad: AppOpenAd) {
                    appOpenAd = ad
                    showAppOpenAd()
                }

                override fun onAdFailedToLoad(error: LoadAdError) {
                    appOpenAd = null
                }
            })
    }

    private fun showAppOpenAd() {
        appOpenAd?.fullScreenContentCallback = object : FullScreenContentCallback() {
            override fun onAdDismissedFullScreenContent() {
                appOpenAd = null
            }

            override fun onAdFailedToShowFullScreenContent(adError: AdError) {}

            override fun onAdShowedFullScreenContent() {
                appOpenAd = null
            }
        }

        appOpenAd?.show(this)
    }

    override fun clicks() {

    }

    override fun initData() {
      //  mainModel.add(CategoriesModel(0, "Available Offline", R.drawable.three_d))
        mainModel.add(CategoriesModel(1, "3D Wallpaper", R.drawable.three_d))
        mainModel.add(CategoriesModel(2, "Aerial and drone shorts", R.drawable.aerial))
        mainModel.add(CategoriesModel(3, "Animals Wallpaper", R.drawable.animals))
        mainModel.add(CategoriesModel(4, "Animations", R.drawable.animations))
        mainModel.add(CategoriesModel(5, "Apple laptops mobiles", R.drawable.apple_laptops_mobiles))
        mainModel.add(CategoriesModel(6, "Birds", R.drawable.birds))
        mainModel.add(CategoriesModel(8, "Black Wallpaper", R.drawable.black))
        mainModel.add(
            CategoriesModel(
                7, "Cars & Vehicles Wallpaper", R.drawable.cars_vehicles_walpaper
            )
        )
        mainModel.add(CategoriesModel(9, "Cricket Wallpaper", R.drawable.cricket))
        mainModel.add(CategoriesModel(10, "Dangerous Wallpaper", R.drawable.dangerous))
        mainModel.add(CategoriesModel(11, "Drawings Wallpaper", R.drawable.drawings))
        mainModel.add(CategoriesModel(12, "Foods", R.drawable.foods))
        mainModel.add(CategoriesModel(13, "Gadgets", R.drawable.gadgets))
        mainModel.add(CategoriesModel(14, "Galaxies", R.drawable.galaxes))
        mainModel.add(CategoriesModel(15, "Games Wallpaper", R.drawable.games))
        mainModel.add(
            CategoriesModel(
                16, "Graffiti and Street Art", R.drawable.graffiti_and_street_art
            )
        )
        mainModel.add(CategoriesModel(17, "Heavy Bikes", R.drawable.heavy_bikes))
        mainModel.add(CategoriesModel(18, "Holidays Wallpaper", R.drawable.holidays))
        mainModel.add(CategoriesModel(19, "Inspirational Quotes", R.drawable.inspirational_quotes))
        mainModel.add(CategoriesModel(20, "Long drive", R.drawable.long_drive))
        mainModel.add(CategoriesModel(21, "Macro Photography", R.drawable.macro_photography))
        mainModel.add(CategoriesModel(22, "Mobile wallpaper", R.drawable.mobile))
        mainModel.add(CategoriesModel(23, "Monochrome", R.drawable.mono_chorome))
        mainModel.add(CategoriesModel(24, "Mountain Wallpaper", R.drawable.mountain))
        mainModel.add(CategoriesModel(25, "Musics Wallpaper", R.drawable.musics))
        mainModel.add(CategoriesModel(26, "Nature Wallpaper", R.drawable.nature))
        mainModel.add(CategoriesModel(27, "NFTs Wallpaper", R.drawable.nfts))
        mainModel.add(CategoriesModel(28, "Recent Wallpaper", R.drawable.recent))
        mainModel.add(CategoriesModel(29, "Roads", R.drawable.roads))
        mainModel.add(CategoriesModel(30, "Sky Wallpaper", R.drawable.sky))
        mainModel.add(CategoriesModel(31, "Space Wallpaper", R.drawable.space))
        mainModel.add(CategoriesModel(32, "Sun Moon", R.drawable.sun_moon))
        mainModel.add(CategoriesModel(33, "Technology", R.drawable.technology))
        mainModel.add(CategoriesModel(34, "Textured (Brick, wood, etc.)", R.drawable.textured))
        mainModel.add(CategoriesModel(35, "Tropical", R.drawable.tropical))
        mainModel.add(CategoriesModel(36, "Under Water", R.drawable.under_water))
        mainModel.add(CategoriesModel(37, "Zen and tranquility", R.drawable.zen_tranquility))
        mainModel.add(CategoriesModel(38, "Other Wallpaper", R.drawable.other))
        mainModel.add(CategoriesModel(39, "Seasons", R.drawable.seaons))
        mainModel.add(
            CategoriesModel(
                40, "Flowers and Botanical", R.drawable.folowers_and_ontanical
            )
        )

    }

    override fun initAdapter() {
        adapter = CategoriesAdapter(mainModel, this)
        binding.recyclerView.adapter = adapter
    }


    override fun onClick(position: Int, type: String?, data: Any?) {
        startActivity(
            Intent(this, WallpapersActivity::class.java).putExtra(
                AppConstants.BUNDLE, mainModel[position]
            )
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