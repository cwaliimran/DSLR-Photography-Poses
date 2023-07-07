package com.cwnextgen.hdwallpapers.activities

import android.content.Intent
import android.widget.Space
import com.cwnextgen.hdwallpapers.R
import com.cwnextgen.hdwallpapers.activities.base.BaseActivity
import com.cwnextgen.hdwallpapers.adapters.CategoriesAdapter
import com.cwnextgen.hdwallpapers.databinding.ActivityCategoriesBinding
import com.cwnextgen.hdwallpapers.models.CategoriesModel
import com.cwnextgen.hdwallpapers.utils.AppConstants
import com.cwnextgen.hdwallpapers.utils.OnItemClick

class CategoriesActivity : BaseActivity(), OnItemClick {
    private lateinit var binding: ActivityCategoriesBinding
    private val TAG = CategoriesActivity::class.java.simpleName
    private var mainModel = mutableListOf<CategoriesModel>()
    lateinit var adapter: CategoriesAdapter
    override fun onCreate() {
        binding = ActivityCategoriesBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    override fun clicks() {

    }

    override fun initData() {
        mainModel.add(CategoriesModel(1, "3d Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Aerial and drone shorts", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Animals Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Animations", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Apple laptops mobiles", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Birds", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Cars & Vehicles Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Black Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Cricket Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Dangerous Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Drawings Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Followers and Botanical", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Foods", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Gadgets", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Galaxies", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Games Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Graffiti and Street Art", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Heavy Bikes", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Holidays Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Inspirational Quotes", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Long drive", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Macro Photography", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Mobile wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Mono chorome", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Mountain Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Musics Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Nature Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "NFTs Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Recent Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Roads", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Seasons", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Sky Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Space Wallpaper", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Sun Moon", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Technology", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Textured (Brick, wood, etc.)", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Tropical", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Under Water", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Zen and tranquility", R.drawable.logo))
        mainModel.add(CategoriesModel(1, "Other Wallpaper", R.drawable.logo))

    }

    override fun initAdapter() {
        adapter = CategoriesAdapter(mainModel, this)
        binding.recyclerView.adapter = adapter
    }


    override fun onClick(position: Int, type: String?, data: Any?) {
        startActivity(
            Intent(this, MainActivity::class.java).putExtra(
                AppConstants.BUNDLE, mainModel[position]
            )
        )
    }
}