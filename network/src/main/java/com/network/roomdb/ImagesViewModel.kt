package com.network.roomdb

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.network.models.ImagesModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImagesViewModel constructor(application: Application) : AndroidViewModel(application) {
    private val repository: RoomMainRepo = RoomMainRepo()

    val getImages: LiveData<List<ImagesModel>>
        get() = repository.getImages


    fun getImagesByCategory(category : String): LiveData<List<ImagesModel>> {
        return repository.getImagesByCategory(category)

    }

    fun addImage(image: ImagesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.addImage(image)
        }
    }

    private val _insertionComplete = MutableLiveData<Boolean>()
    val insertionComplete: LiveData<Boolean> get() = _insertionComplete

    fun insertImages(image: List<ImagesModel>) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.insertImages(image)
            _insertionComplete.postValue(true)
        }
    }


    fun updateImage(image: ImagesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.updateImage(image)
        }
    }

    fun deleteImage(image: ImagesModel) {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteImage(image)
        }
    }

    fun deleteAllImages() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllImages()
        }
    }

    val isDbEmpty: LiveData<Boolean>
        get() = repository.isDbEmpty





}