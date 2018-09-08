package com.walphotodem.pmd.walphotoedem

import android.content.Context
import android.support.test.InstrumentationRegistry
import android.support.test.espresso.Espresso.onView
import android.support.test.espresso.Espresso.openActionBarOverflowOrOptionsMenu
import android.support.test.espresso.assertion.ViewAssertions.matches
import android.support.test.espresso.matcher.ViewMatchers.*
import android.support.test.rule.ActivityTestRule
import android.support.test.runner.AndroidJUnit4
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.File


/**
 */
@RunWith(AndroidJUnit4::class)
class MainActivityTest {

    @get:Rule
    var activityTestRule: ActivityTestRule<MainActivity> = ActivityTestRule<MainActivity>(MainActivity::class.java)

    @Before
    fun setUp() {
        trimCache(activityTestRule.activity.application)
    }

    @Test
    fun verifyProgressBarIsDisplayedUponStartup() {
        onView(withId(R.id.progress_bar)).check(matches(isDisplayed()))
    }

    @Test
    fun verifyFilterMenu(){
        openActionBarOverflowOrOptionsMenu(InstrumentationRegistry.getInstrumentation().targetContext)
        val activity = activityTestRule.activity
        onView(withText(activity.getString(R.string.small))).check(matches(isDisplayed()))
        onView(withText(activity.getString(R.string.small2x))).check(matches(isDisplayed()))
        onView(withText(activity.getString(R.string.medium))).check(matches(isDisplayed()))
        onView(withText(activity.getString(R.string.medium2x))).check(matches(isDisplayed()))
        onView(withText(activity.getString(R.string.large))).check(matches(isDisplayed()))
        onView(withText(activity.getString(R.string.large1x))).check(matches(isDisplayed()))
        onView(withText(activity.getString(R.string.large2x))).check(matches(isDisplayed()))
    }

    private fun trimCache(context: Context) {
        try {
            val dir = context.cacheDir
            if (dir != null && dir.isDirectory) {
                deleteDir(dir)

            }
        } catch (e: Exception) {
            // TODO: handle exception
        }

    }


    private fun deleteDir(dir: File?) {
        if (dir != null && dir.isDirectory) {
            val children = dir.list()
            for (i in children.indices) {
                (File(dir, children[i])).delete()
            }
        }
    }
}