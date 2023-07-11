package com.cwnextgen.dslrphotographyposes.activities

import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuItem
import androidx.browser.customtabs.CustomTabsIntent
import com.cwnextgen.dslrphotographyposes.BuildConfig
import com.cwnextgen.dslrphotographyposes.R
import com.cwnextgen.dslrphotographyposes.activities.base.BaseActivity
import com.cwnextgen.dslrphotographyposes.adapters.CategoriesAdapter
import com.cwnextgen.dslrphotographyposes.databinding.ActivityCategoriesBinding
import com.cwnextgen.dslrphotographyposes.utils.AppConstants
import com.cwnextgen.dslrphotographyposes.utils.OnItemClick
import com.cwnextgen.dslrphotographyposes.utils.openPlayStoreForMoreApps
import com.cwnextgen.dslrphotographyposes.utils.openPlayStoreForRating
import com.cwnextgen.dslrphotographyposes.utils.shareApp
import com.cwnextgen.dslrphotographyposes.utils.showToast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.appopen.AppOpenAd
import com.network.AppClass
import com.network.models.CategoriesModel

class CategoriesActivity : BaseActivity(), OnItemClick {
    private lateinit var binding: ActivityCategoriesBinding
    private val TAG = CategoriesActivity::class.java.simpleName
    private var mainModel = mutableListOf<CategoriesModel>()
    lateinit var adapter: CategoriesAdapter
    private var appOpenAd: AppOpenAd? = null
    var prevLang = AppClass.sharedPref.getString(AppConstants.APP_LANG, "en")

    override fun onCreate() {
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.title = getString(R.string.app_name)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)


        //only load app open add when app is live
        if (BuildConfig.FLAVOR != "dev") {
            loadAppOpenAd()
        }


    }

    private fun loadAppOpenAd() {
        val adRequest = AdRequest.Builder().build()

        AppOpenAd.load(this@CategoriesActivity,
            getString(R.string.openapp_ad_unit_id),
            adRequest,
            object : AppOpenAd.AppOpenAdLoadCallback() {
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
        mainModel.add(CategoriesModel(1, "Beaches", R.drawable.logo))
        mainModel.add(CategoriesModel(2, "Coffee shops and restaurants", R.drawable.logo))
        mainModel.add(CategoriesModel(3, "Deserts", R.drawable.logo))
        mainModel.add(CategoriesModel(4, "Farmland or vineyards", R.drawable.logo))
        mainModel.add(CategoriesModel(5, "Historical buildings Monuments", R.drawable.logo))
        mainModel.add(CategoriesModel(6, "Indoor Shot", R.drawable.logo))
        mainModel.add(CategoriesModel(7, "Mountain ranges", R.drawable.logo))
        mainModel.add(CategoriesModel(8, "On Road", R.drawable.logo))
        mainModel.add(CategoriesModel(9, "Outdoor Shot", R.drawable.logo))
        mainModel.add(CategoriesModel(10, "Park/Garden", R.drawable.logo))
        mainModel.add(CategoriesModel(11, "Studio", R.drawable.logo))

    }

    override fun initAdapter() {
        adapter = CategoriesAdapter(mainModel, this)
        binding.recyclerView.adapter = adapter
    }


    override fun onClick(position: Int, type: String?, data: Any?) {
        startActivity(
            Intent(this, ImagesActivity::class.java).putExtra(
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


    ///////////////////////////////////////////////////////////////////////////
    // Menu
    ///////////////////////////////////////////////////////////////////////////


    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.share -> {
                this@CategoriesActivity.shareApp()
                true
            }

            R.id.languages -> {
                // Show the language selection dialog
                showLanguageSelectionDialog()
                true
            }

            R.id.menu_rate_app -> {

                this@CategoriesActivity.openPlayStoreForRating()
                true
            }

            R.id.menu_more_apps -> {
                // Handle "More Apps" menu item click
                this@CategoriesActivity.openPlayStoreForMoreApps()
                return true
            }

            R.id.menu_contact_us -> {
                // Handle "Contact Us" menu item click

                try {
                    val intent = Intent(Intent.ACTION_SENDTO)
                    val subject = getString(R.string.app_name) + " Contact US"
                    intent.data = Uri.parse("mailto:princelumia143@gmail.com?subject=$subject")
                    startActivity(intent)
                } catch (e: Exception) {
                    showToast("Contact us on support@daretheapp.com")
                }

                return true
            }

            R.id.menu_privacy_policy -> {
                // Handle "Privacy Policy" menu item click
                val url =
                    "https://www.privacypolicies.com/live/dae1a231-843f-4097-b482-0281b4070d64"
                val builder = CustomTabsIntent.Builder()
                val customTabsIntent = builder.build()
                customTabsIntent.launchUrl(this@CategoriesActivity, Uri.parse(url))

                return true
            }

            else -> return super.onOptionsItemSelected(item)
        }

    }

    private fun showLanguageSelectionDialog() {
        val languages = arrayOf(
            getString(R.string.english), getString(R.string.urdu), getString(R.string.hindi), getString(R.string.arabic)
        )
        val languagesShort = arrayOf("en", "ur", "hi","ar")
        prevLang = AppClass.sharedPref.getString(AppConstants.APP_LANG, "en")

        val dialogBuilder = AlertDialog.Builder(this)
        dialogBuilder.setTitle("Choose Language")
        dialogBuilder.setSingleChoiceItems(
            languages, getSelectedLanguagePosition()
        ) { dialog, which ->
            prevLang = languagesShort[which]
        }
        dialogBuilder.setPositiveButton("OK") { dialog, which ->
            AppClass.sharedPref.storeString(AppConstants.APP_LANG, prevLang)
            AppClass.changeLocale(this, prevLang); // Change the app language
            recreate() // Refresh the activity to apply the language changes
        }
        dialogBuilder.setNegativeButton("Cancel", null)

        val dialog = dialogBuilder.create()
        dialog.show()
    }

    private fun getSelectedLanguagePosition(): Int {
        return when (prevLang) {
            "en" -> 0
            "ur" -> 1
            "hi" -> 2
            else -> 0 // Default to English if the selected language is not recognized
        }
    }
}
