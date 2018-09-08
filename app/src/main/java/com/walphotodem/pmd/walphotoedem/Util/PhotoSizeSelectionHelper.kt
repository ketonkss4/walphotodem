package com.walphotodem.pmd.walphotoedem.Util

/**
 */
class PhotoSizeSelectionHelper {

    private var photoSizeSelection: String? = ""

    companion object {
        const val DEFAULT_SIZE = "medium"
        const val SIZE_STATE_KEY: String = "SIZE_STATE_KEY"
    }

    fun setPhotoSizeSelection(size: String?) {
        photoSizeSelection = size
    }

    fun getPhotoSizeSelection(): String? {
        return if (!photoSizeSelection.isNullOrEmpty()) photoSizeSelection else DEFAULT_SIZE
    }

}