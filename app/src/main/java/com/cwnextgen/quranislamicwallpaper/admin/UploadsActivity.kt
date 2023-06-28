package com.cwnextgen.quranislamicwallpaper.admin

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
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

        binding = ActivityUploadsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        //  signUpOrLogin("quranwallpaper@admin.com", "quran6666wallpaper")
        binding.button1.setOnClickListener {
//            getPicker().galleryOnly().createIntent { intent ->
//                startForProfileImageResult.launch(intent)
//            }

            val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(intent, 300)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 300 && resultCode == Activity.RESULT_OK && data != null) {
            val imageUri = data.data
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
            Log.d(TAG, "uploadImage: " + exception)


            try {
                // Your Firebase Storage operation
            } catch (e: StorageException) {
                val errorCode = e.errorCode
                // Log or handle the errorCode as needed

                Log.d(TAG, "uploadImage: " + errorCode)
                val innerException = e.cause
                if (innerException != null) {

                    Log.d(TAG, "uploadImage: " + innerException)
                    // Log or handle the innerException as needed
                }
            }

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

}
