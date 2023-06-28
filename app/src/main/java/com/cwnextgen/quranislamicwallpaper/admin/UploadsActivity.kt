package com.cwnextgen.quranislamicwallpaper.admin

import android.app.Activity
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.cwnextgen.quranislamicwallpaper.databinding.ActivityUploadsBinding
import com.cwnextgen.quranislamicwallpaper.models.MainModel
import com.cwnextgen.quranislamicwallpaper.utils.AppConstants
import com.cwnextgen.quranislamicwallpaper.utils.ProgressLoading.displayLoading
import com.cwnextgen.quranislamicwallpaper.utils.firestore
import com.cwnextgen.quranislamicwallpaper.utils.generateUUID
import com.cwnextgen.quranislamicwallpaper.utils.getPicker
import com.cwnextgen.quranislamicwallpaper.utils.showToast
import com.cwnextgen.quranislamicwallpaper.utils.storage
import com.github.dhaval2404.imagepicker.ImagePicker
import com.google.firebase.storage.StorageReference
import java.io.File

class UploadsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityUploadsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityUploadsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.button1.setOnClickListener {

            getPicker().galleryOnly().createIntent { intent ->
                    startForProfileImageResult.launch(intent)
                }
        }
    }


    private val startForProfileImageResult =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result: ActivityResult ->
            val resultCode = result.resultCode
            val data = result.data
            if (resultCode == Activity.RESULT_OK) {
                val uri = data?.data
                val filePath = uri?.path

                Log.d("TAGRESULT", "onActivityResult: $filePath")
                filePath?.let { uploadImage(it) }
            } else if (resultCode == ImagePicker.RESULT_ERROR) {
                showToast(ImagePicker.getError(data))
            }
        }

    private fun uploadImage(filePath: String) {
        displayLoading()

// Create a reference to the desired location in Firebase Storage
        val imageRef: StorageReference =
            storage().reference.child("wallpapers/${generateUUID()}.jpg")

// Upload the image to Firebase Storage
        val file = File(filePath)
        val uploadTask = imageRef.putFile(Uri.fromFile(file))
        uploadTask.addOnSuccessListener {

            displayLoading(false)
            // Image uploaded successfully
            // Retrieve the download URL
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
            showToast(exception.localizedMessage?.toString() ?: exception.toString())
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

}