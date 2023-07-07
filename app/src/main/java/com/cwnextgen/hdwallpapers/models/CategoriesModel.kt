package com.cwnextgen.hdwallpapers.models

import java.io.Serializable

data class CategoriesModel(
    var id: Int? = 0,
    var categoryTitle: String? = "",
    var image: Int? = null,
    var active: Boolean? = true
) : Serializable
