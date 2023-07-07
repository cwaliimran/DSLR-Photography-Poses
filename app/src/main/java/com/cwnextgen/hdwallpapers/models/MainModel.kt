package com.cwnextgen.hdwallpapers.models

import java.io.Serializable
import java.util.Date

data class MainModel(
    var id: String? = "",
    var imageUrl: String? = "",
    var category: String? = "",
    var createdAt: Date? = Date(),
    var active: Boolean? = true
) : Serializable
