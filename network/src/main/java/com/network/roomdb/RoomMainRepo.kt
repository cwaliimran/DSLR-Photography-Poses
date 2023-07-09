package com.network.roomdb

import androidx.lifecycle.LiveData
import com.network.AppClass
import com.network.models.ImagesModel
import com.network.models.InAppPurchase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomMainRepo {

    ///////////////////////////////////////////////////////////////////////////
    // Images
    ///////////////////////////////////////////////////////////////////////////
    val daoImages = AppDatabase.getDatabase(AppClass.instance).imagesDao()

    val getImages: LiveData<List<ImagesModel>> = daoImages.getAllImages()

    fun getImagesByCategory(
        category: String,
    ): LiveData<List<ImagesModel>> {
        return daoImages.getImagesByCategory(category)
    }

    suspend fun addImage(image: ImagesModel) {
        daoImages.addImage(image)
    }

    suspend fun insertImages(image: List<ImagesModel>) {
        daoImages.insertImages(image)
    }

    suspend fun updateImage(image: ImagesModel) {
        withContext(Dispatchers.IO) {
            daoImages.updateImage(image)
        }
    }

    suspend fun deleteImage(image: ImagesModel) {
        daoImages.deleteImage(image)
    }

    suspend fun deleteAllImages() {
        daoImages.deleteAllImages()
    }

    val isDbEmpty: LiveData<Boolean> = daoImages.isDbEmpty()


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