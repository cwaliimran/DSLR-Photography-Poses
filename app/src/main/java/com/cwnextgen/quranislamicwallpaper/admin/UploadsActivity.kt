package com.cwnextgen.quranislamicwallpaper.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import androidx.activity.OnBackPressedCallback
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cwnextgen.quranislamicwallpaper.databinding.ActivityUploadsBinding
import com.cwnextgen.quranislamicwallpaper.models.MainModel
import com.cwnextgen.quranislamicwallpaper.utils.AppConstants
import com.cwnextgen.quranislamicwallpaper.utils.ProgressLoading.displayLoading
import com.cwnextgen.quranislamicwallpaper.utils.auth
import com.cwnextgen.quranislamicwallpaper.utils.firestore
import com.cwnextgen.quranislamicwallpaper.utils.generateUUID
import com.cwnextgen.quranislamicwallpaper.utils.getPicker
import com.cwnextgen.quranislamicwallpaper.utils.showToast
import com.cwnextgen.quranislamicwallpaper.utils.storage
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageException
import com.google.firebase.storage.StorageReference
import java.io.File
import java.util.Date

class UploadsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadsBinding
    private val TAG = "UploadsActivityTAG"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityUploadsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        //  signUpOrLogin("quranwallpaper@admin.com", "quran6666wallpaper")
        binding.button1.setOnClickListener {
//            getPicker().galleryOnly().createIntent { intent ->
//                startForProfileImageResult.launch(intent)
//            }

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            uploadImageResultLauncher.launch(intent)

        }


    }


    private val uploadImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            if (resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                uploadImageToFirebaseStorage(imageUri)
            }
        }

    private fun uploadImageToFirebaseStorage(imageUri: Uri?) {

        if (imageUri != null) {
            displayLoading()

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("wallpapers/${generateUUID()}+.jpg")

            imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
                displayLoading(false)

                // Image upload successful
                val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    Glide.with(this).load(imageUrl).into(binding.imageView)
                    // Do something with the image URL, like saving it to a database
                    val mainModel = MainModel(generateUUID(), imageUrl, "")
                    saveData(mainModel)
                }
            }.addOnFailureListener { exception ->
                displayLoading(false)
                // Handle the upload failure
                Log.d(TAG, "uploadImage: " + exception)


            }

                .addOnFailureListener { exception ->
                    displayLoading(false)
                    Log.d(TAG, "uploadImageToFirebaseStorage: " + exception.localizedMessage)
                    // Image upload failed
                    // Handle the error accordingly
                }
        }
    }


    private fun saveData(mainModel: MainModel) {
        displayLoading()
        firestore().collection(AppConstants.TBL_WALLPAPERS).document(mainModel.id!!).set(mainModel)
            .addOnCompleteListener {
                displayLoading(false)
                if (it.isSuccessful) {
                    showToast("Uploaded successfully")
//                    Glide.with(this).load(ContextCompat.getDrawable(this, R.drawable.placeholder))
//                        .into(binding.imageView)

                } else {
                    showToast(it.exception.toString())
                }
            }.addOnFailureListener {
                displayLoading(false)
                showToast(it.toString())
            }
    }


    // Define a function for signing up or logging in
    fun signUpOrLogin(email: String, password: String) {
        displayLoading()
        auth().createUserWithEmailAndPassword(email, password).addOnCompleteListener { signUpTask ->

            if (signUpTask.isSuccessful) {
                // User signed up successfully
                displayLoading(false)

            } else {
                // User already signed up or sign-up failed
                // Attempt to log in instead
                auth().signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener { loginTask ->
                        if (loginTask.isSuccessful) {
                            displayLoading(false)

                            // User logged in successfully
                            val user = auth().currentUser
                            val userId = user?.uid
                            if (userId != null) {
                                //saveAdmin(userId)
                            }
                            // Proceed with your app flow
                        } else {
                            // Login failed
                            val exception = loginTask.exception
                            // Handle the failure case
                            showToast(exception.toString())
                            displayLoading(false)
                        }
                    }
            }
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

    override fun onBackPressed() {
        finish()
        super.onBackPressed()
    }
    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                onBackPressed()  // Call onBackPressed() when the back button is pressed
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

}
