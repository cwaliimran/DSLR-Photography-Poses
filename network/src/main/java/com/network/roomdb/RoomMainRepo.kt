package com.network.roomdb

import androidx.lifecycle.LiveData
import com.network.AppClass
import com.network.models.InAppPurchase
import com.network.models.WallpaperModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomMainRepo {

    ///////////////////////////////////////////////////////////////////////////
    // WALLPAPERS
    ///////////////////////////////////////////////////////////////////////////
    val daoWallpapers = AppDatabase.getDatabase(AppClass.instance).questionsDao()

    val getWallpapers: LiveData<List<WallpaperModel>> = daoWallpapers.getAllWallpapers()

    fun getWallpapersByCategory(
        category: String,
    ): LiveData<List<WallpaperModel>> {
        return daoWallpapers.getWallpapersByCategory(category)
    }

    suspend fun addWallpaper(question: WallpaperModel) {
        daoWallpapers.addWallpaper(question)
    }

    suspend fun insertWallpapers(question: List<WallpaperModel>) {
        daoWallpapers.insertWallpapers(question)
    }

    suspend fun updateWallpaper(question: WallpaperModel) {
        withContext(Dispatchers.IO) {
            daoWallpapers.updateWallpaper(question)
        }
    }

    suspend fun deleteWallpaper(question: WallpaperModel) {
        daoWallpapers.deleteWallpaper(question)
    }

    suspend fun deleteAllWallpapers() {
        daoWallpapers.deleteAllWallpapers()
    }

    val isDbEmpty: LiveData<Boolean> = daoWallpapers.isDbEmpty()


    ///////////////////////////////////////////////////////////////////////////
    // Purchases
    ///////////////////////////////////////////////////////////////////////////
    val daoPurchases = AppDatabase.getDatabase(AppClass.instance).purchasesDao()

    val getPurchases: LiveData<List<InAppPurchase>> = daoPurchases.getPurchases()

    suspend fun addInAppPurchase(purchase: InAppPurchase) {
        daoPurchases.addPurchase(purchase)
    }

    suspend fun updateInAppPurchase(purchase: InAppPurchase) {
        daoPurchases.updatePurchase(purchase)
    }

    suspend fun deleteInAppPurchase(purchase: InAppPurchase) {
        daoPurchases.deletePurchase(purchase)
    }

    suspend fun deleteAllPurchases() {
        daoPurchases.deleteAllPurchases()
    }


}