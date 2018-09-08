package com.walphotodem.pmd.walphotoedem.Util

/**
 * This class abstracts the spanning logic from the activity class
 */
class GridSpanUtil {
    fun configureGridSpan(isLandscape: Boolean, photoSize: String?) : Int {
        return when (photoSize) {
            "small",
            "small2x" -> if (isLandscape) 1 else 2
            "medium" -> if (isLandscape) 2 else 4
            "medium2x" -> if (isLandscape) 3 else 6
            "large",
            "large1x",
            "large2x" -> if (isLandscape) 6 else 12
            else -> if (isLandscape) 2 else 4
        }
    }
}