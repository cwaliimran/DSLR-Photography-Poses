package com.cwnextgen.dslrphotographyposes.utils

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Canvas
import android.view.View
import androidx.core.content.FileProvider
import com.cwnextgen.dslrphotographyposes.BuildConfig
import com.cwnextgen.dslrphotographyposes.R
import java.io.File
import java.io.FileOutputStream
import java.io.IOException

object ShareImage {

    ///////////////////////////////////////////////////////////////////////////
    // CARD SHARING
    ///////////////////////////////////////////////////////////////////////////
    fun Activity.getBitmapFromView(view: View) {
        val bitmap = Bitmap.createBitmap(
            view.width, view.height, Bitmap.Config.ARGB_8888
        )
        val canvas = Canvas(bitmap)
        view.draw(canvas)
        saveImage(bitmap, this)
    }

    fun saveImage(finalBitmap: Bitmap, context: Context) {
        // save bitmap to cache directory
        try {
            val cachePath = File(context.cacheDir, "images")
            cachePath.mkdirs() // don't forget to make the directory
            val stream =
                FileOutputStream("$cachePath/image.png") // overwrites this image every time
            finalBitmap.compress(Bitmap.CompressFormat.PNG, 50, stream)
            stream.close()
            send(context)
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    fun send(context: Context) {
        val imagePath = File(context.cacheDir, "images")
        val newFile = File(imagePath, "image.png")

        val contentUri =
            FileProvider.getUriForFile(context, "${BuildConfig.APPLICATION_ID}.fileprovider", newFile)

        if (contentUri != null) {
            val shareIntent = Intent()
            shareIntent.action = Intent.ACTION_SEND
            shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION) // temp permission for receiving app to read this file
            shareIntent.setDataAndType(contentUri, context.contentResolver.getType(contentUri))
            shareIntent.putExtra(Intent.EXTRA_STREAM, contentUri)
            shareIntent.putExtra(
                Intent.EXTRA_TEXT,
                context.getString(R.string.share_text) + " ⬇️\n https://play.google.com/store/apps/details?id=${BuildConfig.APPLICATION_ID}"
            )
            context.startActivity(Intent.createChooser(shareIntent, "Share via"))
        }
    }

}