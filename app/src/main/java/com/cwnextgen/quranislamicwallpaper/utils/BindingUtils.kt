package com.cwnextgen.quranislamicwallpaper.utils

import android.widget.ImageView
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade
import com.cwnextgen.quranislamicwallpaper.R


class BindingUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("android:loadImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context).load(imageUrl)
                .transition(withCrossFade())
                .placeholder(R.drawable.placeholder)
                .error(R.drawable.ic_launcher_background)
                .into(view)
        }
    }
}
