package com.cwnextgen.dslrphotographyposes.activities

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.cwnextgen.dslrphotographyposes.BuildConfig
import com.cwnextgen.dslrphotographyposes.R
import com.cwnextgen.dslrphotographyposes.activities.base.BaseActivity
import com.cwnextgen.dslrphotographyposes.adapters.ImagesAdapter
import com.cwnextgen.dslrphotographyposes.admin.UploadsActivity
import com.cwnextgen.dslrphotographyposes.databinding.ActivityImagesBinding
import com.cwnextgen.dslrphotographyposes.utils.AppConstants
import com.cwnextgen.dslrphotographyposes.utils.OnItemClick
import com.cwnextgen.dslrphotographyposes.utils.ProgressLoading.displayLoading
import com.cwnextgen.dslrphotographyposes.utils.firestore
import com.cwnextgen.dslrphotographyposes.utils.isShow
import com.cwnextgen.dslrphotographyposes.utils.showToast
import com.google.android.gms.ads.AdError
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.FullScreenContentCallback
import com.google.android.gms.ads.LoadAdError
import com.google.android.gms.ads.MobileAds
import com.google.android.gms.ads.interstitial.InterstitialAd
import com.google.android.gms.ads.interstitial.InterstitialAdLoadCallback
import com.network.models.CategoriesModel
import com.network.models.ImagesModel
import com.network.roomdb.ImagesViewModel
import kotlinx.coroutines.launch

class ImagesActivity : BaseActivity(), OnItemClick {
    private lateinit var binding: ActivityImagesBinding
    private val TAG = ImagesActivity::class.java.simpleName
    lateinit var adapter: ImagesAdapter
    private var imagesModel = mutableListOf<ImagesModel>()
    private val viewModelRoom: ImagesViewModel by viewModels()
    private var category = CategoriesModel()
    private var mInterstitialAd: InterstitialAd? = null
    lateinit var adRequest: AdRequest

    lateinit var adRequest1: AdRequest
    private var isFetchedOnline = false
    override fun onCreate() {

        binding = ActivityImagesBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        MobileAds.initialize(this@ImagesActivity)
        adRequest = AdRequest.Builder().build()
        adRequest1 = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)
        askNotificationPermission()

        loadAdRequest()

    }

    private fun loadAdRequest() {

        InterstitialAd.load(this@ImagesActivity,
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
        adapter = ImagesAdapter(imagesModel, this)
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
        imagesModel.clear()
        displayLoading()
        //get data from room database if available against current category
        lifecycleScope.launch {
            viewModelRoom.getImagesByCategory(category.id.toString()).observe(this@ImagesActivity) {
                    if (it != null) {
                        if (!isFetchedOnline) {
                            imagesModel.addAll(it as MutableList<ImagesModel>)
                            Log.d(
                                TAG, "fetchData: local found and completed ${imagesModel.size}"
                            )
                            if (imagesModel.isNotEmpty()) {
                                adapter.updateData(imagesModel)
                                displayLoading(false)
                            } else {
                                fetchDataFromServer()
                            }
                        }
                    } else {
                        Log.d(TAG, "fetchData: local not found")
//                        fetchDataFromServer()
                    }
                }
        }

    }

    private fun fetchDataFromServer() {
        Log.d(TAG, "fetchData: live")
        val postsCollection = firestore().collection(category.id.toString())

// Perform a query to retrieve the posts
        postsCollection.get().addOnSuccessListener { querySnapshot ->
            for (documentSnapshot in querySnapshot.documents) {
                // Retrieve post data
                val model = documentSnapshot.toObject(ImagesModel::class.java)
                if (model!!.active!!) {
                    imagesModel.add(model)
                }
            }
            isFetchedOnline =
                true //change value to true before inserting in room database so listener will not duplicate data
            if (imagesModel.isEmpty()) {
                showToast(getString(R.string.no_data_found))
            }
            adapter.updateData(imagesModel)
            viewModelRoom.insertImages(imagesModel)
            displayLoading(false)

        }.addOnFailureListener { e ->
            displayLoading(false)
            // Handle any errors that occurred while querying the posts collection
            e.localizedMessage?.let { showToast(it) }
        }
    }

    override fun onClick(position: Int, type: String?, data: Any?) {
        var model = imagesModel[position]
        openDetailPage(model)
        // TODO: check why ads shows in on resume rather than button click
        //     showInterstitial(model)
    }

    private fun openDetailPage(model: ImagesModel) {
        mInterstitialAd = null;
        startActivity(
            Intent(this, DetailActivity::class.java).putExtra(
                AppConstants.BUNDLE, model
            )
        )

    }

    private fun showInterstitial(model: ImagesModel) {
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