package com.cwnextgen.quranislamicwallpaper.utils

import android.view.View
import android.widget.ImageView
import androidx.constraintlayout.widget.Group
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.cwnextgen.quranislamicwallpaper.R


class BindingUtils {
    companion object {
        @JvmStatic
        @BindingAdapter("android:loadImage")
        fun loadImage(view: ImageView, imageUrl: String?) {
            Glide.with(view.context).load(imageUrl)
                .placeholder(R.drawable.loader)
                .error(R.drawable.ic_launcher_background)
                .into(view)
        }

        fun Group.setAllOnClickListener(listener: View.OnClickListener?) {
            referencedIds.forEach { id ->
                rootView.findViewById<View>(id).setOnClickListener(listener)
            }
        }
    }
}
