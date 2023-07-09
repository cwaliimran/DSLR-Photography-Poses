package com.network.roomdb.dao

import androidx.lifecycle.LiveData
import androidx.room.*
import androidx.room.OnConflictStrategy.Companion.REPLACE
import com.network.models.InAppPurchase

@Dao
interface DaoInAppPurchases {
    @Insert(onConflict = REPLACE)
    suspend fun addPurchase(purchase: InAppPurchase)

    @Update
    suspend fun updatePurchase(purchase: InAppPurchase)

    @Delete
    suspend fun deletePurchase(purchase: InAppPurchase)

    @Query("SELECT * FROM purchases")
    fun getPurchases(): LiveData<List<InAppPurchase>>

    @Query("DELETE FROM purchases")
    fun deleteAllPurchases()

}