package com.cwnextgen.hdwallpapers.utils

import android.app.Activity
import com.github.dhaval2404.imagepicker.ImagePicker

fun Activity.getPicker(): ImagePicker.Builder {
    return ImagePicker.with(this)
        //.crop()
        //.compress(1024)         //Final image size will be less than 1 MB(Optional)
       // .maxResultSize(1080, 1080)  //Final image resolution will be less than 1080 x 1080(Optional)
}