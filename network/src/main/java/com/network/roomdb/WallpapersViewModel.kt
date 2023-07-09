package com.network.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.network.models.WallpaperModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class WallpapersViewModel constructor(application: Application) : AndroidViewModel(application) {
    private val TAG = "QuestionsViewModelTAG"
    private val repository: RoomMainRepo = RoomMainRepo()

    val getWallpapers: LiveData<List<WallpaperModel>>
        get() = repository.getWallpapers


    fun getWallpapersByCategory(category : String): LiveData<List<WallpaperModel>> {
        return repository.getWallpapersByCategory(category)

    }

    fun addWallpaper(wallpaper: WallpaperModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addWallpaper(wallpaper)
        }
    }

    private val _insertionComplete = MutableLiveData<Boolean>()
    val insertionComplete: LiveData<Boolean> get() = _insertionComplete

    fun insertWallpapers(wallpaper: List<WallpaperModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertWallpapers(wallpaper)
            _insertionComplete.postValue(true)
        }
    }


    fun updateWallpaper(wallpaper: WallpaperModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateWallpaper(wallpaper)
        }
    }

    fun deleteWallpaper(wallpaper: WallpaperModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteWallpaper(wallpaper)
        }
    }

    fun deleteAllWallpapers() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllWallpapers()
        }
    }

    val isDbEmpty: LiveData<Boolean>
        get() = repository.isDbEmpty





}