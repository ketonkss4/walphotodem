package com.walphotodem.pmd.walphotoedem.Util

import android.arch.lifecycle.MutableLiveData

/**
 */
class PhotoSizeSelectionHelper {

    private val postLiveData: MutableLiveData<String> = MutableLiveData()

    companion object {
        const val DEFAULT_SIZE = "medium"
    }

    fun setPhotoSizeSelection(size: String) {
        postLiveData.postValue(size)
    }

    fun getPhotoSizeSelection(): String? {
        return if (postLiveData.value != null) postLiveData.value else DEFAULT_SIZE
    }

}