package com.walphotodem.pmd.walphotoedem.Util

import org.junit.Test

/**
 */
class GridSpanUtilTest {

    @Test
    fun setAppropriateSpanSizeBasedOnOrientationAndPhotoSize() {
        val sizeList = arrayListOf("small",
                "small2x",
                "medium",
                "medium2x",
                "large",
                "large1x",
                "large2x",
                null)

        sizeList.forEach {
            when (it) {
                "small",
                "small2x" -> {
                    verifySpanSize(true, it, 1, 2)
                    verifySpanSize(false, it, 1, 2)
                }
                "medium" -> {
                    verifySpanSize(true, it, 2, 4)
                    verifySpanSize(false, it, 2, 4)
                }
                "medium2x" -> {
                    verifySpanSize(true, it, 3, 6)
                    verifySpanSize(false, it, 3, 6)
                }
                "large",
                "large1x",
                "large2x" -> {
                    verifySpanSize(true, it, 6, 12)
                    verifySpanSize(false, it, 6, 12)
                }
                else -> {
                    verifySpanSize(true, it, 2, 4)
                    verifySpanSize(false, it, 2, 4)
                }
            }
        }
    }

    fun verifySpanSize(
            isLandscape: Boolean,
            photoSize: String?,
            landscapeSpan: Int,
            portraitSpan: Int
    ) {
        val gridSpan = GridSpanUtil().configureGridSpan(isLandscape, photoSize)
        if (isLandscape) assert(gridSpan == landscapeSpan) else (gridSpan == portraitSpan)
    }
}