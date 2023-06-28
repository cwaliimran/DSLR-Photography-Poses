package com.cwnextgen.quranislamicwallpaper.models

import java.io.Serializable
import java.util.Date

data class MainModel(
    var id: String? = "",
    var imageUrl: String? = "",
    var category: String? = "",
    var createdAt: Date? = Date(),
) :Serializable
