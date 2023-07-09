package com.network.roomdb

import android.content.Context
import androidx.room.*
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.network.models.InAppPurchase
import com.network.models.WallpaperModel
import com.network.roomdb.dao.DaoInAppPurchases
import com.network.roomdb.dao.DaoWallpapers

@Database(
    entities = [WallpaperModel::class, InAppPurchase::class],
    version = 1,
    exportSchema = true,
     autoMigrations = [
//         AutoMigration(from = 1, to = 2),
//         AutoMigration(from = 2, to = 3),
//         AutoMigration(from = 3, to = 4)

     ]
)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun questionsDao(): DaoWallpapers
//    abstract fun creatorQuestionsDao(): DaoCreatorModeQuestions
    abstract fun purchasesDao(): DaoInAppPurchases

    companion object {

       //for custom migrations
      val migration_1_2 = object : Migration(1, 2) {
      override fun migrate(database: SupportSQLiteDatabase) {
          database.execSQL("ALTER TABLE wallpapers ADD COLUMN category_type TEXT")
      }
  }

 @Volatile
 private var INSTANCE: AppDatabase? = null

 fun getDatabase(context: Context): AppDatabase {
     if (INSTANCE == null) {
         synchronized(this) {
             INSTANCE = Room.databaseBuilder(
                 context.applicationContext,
                 AppDatabase::class.java, "appDatabase"
             )
                 //  .createFromAsset("database/appDatabase.db")
                // .allowMainThreadQueries()
                // .addMigrations(migration_1_2)
                 .build()
         }
     }
     return INSTANCE!!
 }
}
}