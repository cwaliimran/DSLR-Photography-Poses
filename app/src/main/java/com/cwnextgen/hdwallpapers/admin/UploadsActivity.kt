package com.cwnextgen.hdwallpapers.admin


import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.MenuItem
import androidx.activity.result.contract.ActivityResultContracts
import androidx.documentfile.provider.DocumentFile
import com.bumptech.glide.Glide
import com.cwnextgen.hdwallpapers.BuildConfig
import com.cwnextgen.hdwallpapers.activities.base.BaseActivity
import com.cwnextgen.hdwallpapers.databinding.ActivityUploadsBinding
import com.cwnextgen.hdwallpapers.models.CategoriesModel
import com.cwnextgen.hdwallpapers.models.WallpaperModel
import com.cwnextgen.hdwallpapers.utils.AppConstants
import com.cwnextgen.hdwallpapers.utils.ProgressLoading.displayLoading
import com.cwnextgen.hdwallpapers.utils.auth
import com.cwnextgen.hdwallpapers.utils.firestore
import com.cwnextgen.hdwallpapers.utils.generateUUID
import com.cwnextgen.hdwallpapers.utils.isShow
import com.cwnextgen.hdwallpapers.utils.showToast
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.MobileAds
import com.google.firebase.storage.FirebaseStorage

class UploadsActivity : BaseActivity() {
    private lateinit var binding: ActivityUploadsBinding
    private val TAG = "UploadsActivityTAG"
    var imageUris = mutableListOf<Uri>()
    private var category = CategoriesModel()

    var totalFolderImages = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding = ActivityUploadsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        MobileAds.initialize(this) {}
        val adRequest = AdRequest.Builder().build()
        binding.adView.loadAd(adRequest)

        binding.button1.setOnClickListener {


            if (BuildConfig.FLAVOR == "dev") {
                binding.textViewUploadCount.isShow()
                //select complete folder and then it fetches all images from folder and then we upload one by one
                folderSelectionLauncher.launch(null)
            } else { //users can upload single image at a time
                val intent =
                    Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
                uploadImageResultLauncher.launch(intent)
            }


        }


    }

    override fun onCreate() {

    }

    override fun clicks() {

    }

    override fun apiAndArgs() {
        bundle = intent.extras
        if (bundle != null) {
            category = bundle!!.getSerializable(AppConstants.BUNDLE) as CategoriesModel
            supportActionBar?.title = category.categoryTitle
//            fetchData()
        }
    }

    private val folderSelectionLauncher =
        registerForActivityResult(ActivityResultContracts.OpenDocumentTree()) { uri: Uri? ->
            // Handle the selected folder URI here
            if (uri != null) {
                val folderUri = uri
                imageUris = getAllImageUrisFromFolder(folderUri)
                totalFolderImages = imageUris.size
                // Process the image URIs as needed
//            for (imageUri in imageUris) {
//                Log.d(TAG, "imageUri: $imageUri")
//            }
                if (imageUris.isNotEmpty()) {
                    uploadImageToFirebaseStorage(imageUris.last())
                } else {
                    showToast("No images found")
                }
            }
        }

    private val uploadImageResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
            val resultCode = result.resultCode
            if (resultCode == Activity.RESULT_OK && result.data != null) {
                val imageUri = result.data?.data
                Log.d(TAG, "$imageUri: ")
                  uploadImageToFirebaseStorage(imageUri)
            }
        }

    private fun uploadImageToFirebaseStorage(imageUri: Uri?) {

        if (imageUri != null) {
            displayLoading()

            val storageRef = FirebaseStorage.getInstance().reference
            val imageRef = storageRef.child("appwallpapers/${generateUUID()}+.jpg")

            imageRef.putFile(imageUri).addOnSuccessListener { taskSnapshot ->
                // Image upload successful
                val downloadUrl = taskSnapshot.metadata?.reference?.downloadUrl
                imageRef.downloadUrl.addOnSuccessListener { downloadUri ->
                    val imageUrl = downloadUri.toString()
                    Glide.with(this).load(imageUrl).into(binding.imageView)
                    // Do something with the image URL, like saving it to a database
                    val wallpaperModel = WallpaperModel(generateUUID(), imageUrl, "")
                    saveData(wallpaperModel)
                }
            }.addOnFailureListener { exception ->
                displayLoading(false)
                // Handle the upload failure
                Log.d(TAG, "uploadImage: " + exception)

                checkAndUploadFolderImages()

            }

                .addOnFailureListener { exception ->
                    displayLoading(false)
                    Log.d(TAG, "uploadImageToFirebaseStorage: " + exception.localizedMessage)
                    // Image upload failed
                    // Handle the error accordingly
                    checkAndUploadFolderImages()
                }
        }
    }


    private fun saveData(wallpaperModel: WallpaperModel) {
        firestore().collection(category.id.toString()).document(wallpaperModel.id!!).set(wallpaperModel)
            .addOnCompleteListener {
                displayLoading(false)
                if (it.isSuccessful) {
                    showToast("Uploaded successfully")
                    checkAndUploadFolderImages()

                } else {
                    showToast(it.exception.toString())
                    checkAndUploadFolderImages()
                }
            }.addOnFailureListener {
                displayLoading(false)
                showToast(it.toString())
                checkAndUploadFolderImages()
            }
    }

    private fun checkAndUploadFolderImages() {
        if (BuildConfig.FLAVOR == "dev") {
            if (imageUris.isNotEmpty()) {
                imageUris.removeLast()
                if (imageUris.isNotEmpty()) {
                    uploadImageToFirebaseStorage(imageUris.last())
                }else {
                    showToast("All images uploaded")
                }
            } else {
                showToast("All images uploaded")
            }
            binding.textViewUploadCount.text = "total Images: $totalFolderImages \n" +
                    "remaining uploads: ${imageUris.size}"
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

    private fun getAllImageUrisFromFolder(folderUri: Uri): MutableList<Uri> {
        val imageUris = mutableListOf<Uri>()

        val folder = DocumentFile.fromTreeUri(this, folderUri)
        folder?.listFiles()?.forEach { file ->
            if (file.isFile && isImageFile(file.uri)) {
                imageUris.add(file.uri)
            }
        }

        return imageUris
    }

    private fun isImageFile(uri: Uri): Boolean {
        val fileType = contentResolver.getType(uri)
        return fileType?.startsWith("image/") ?: false
    }


}
