package com.cwnextgen.quranislamicwallpaper.utils

interface OnItemClick {
    fun onClick(position: Int, type: String? = "", data: Any? = null) {}
}