package com.walphotodem.pmd.walphotoedem

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v4.widget.SwipeRefreshLayout
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import app.PhotoApp
import com.walphotodem.pmd.walphotoedem.Util.GridSpanUtil
import com.walphotodem.pmd.walphotoedem.Util.PhotoSizeSelectionHelper
import com.walphotodem.pmd.walphotoedem.Util.PhotoSizeSelectionHelper.Companion.SIZE_STATE_KEY
import com.walphotodem.pmd.walphotoedem.adapter.PhotoGridAdapter
import com.walphotodem.pmd.walphotoedem.data.RequestFailure
import com.walphotodem.pmd.walphotoedem.presenter.GalleryPresenter

/**
 * MainActivity serves as the View Controller of the application. It is intentionally
 * kept free of any logic that is not pertained to view manipulation. For everything else
 * dependencies are used
 */
class MainActivity : AppCompatActivity(), GalleryPresenter.OnFailedRequestListener {
    private lateinit var photoListView: RecyclerView
    private lateinit var galleryPresenter: GalleryPresenter
    private lateinit var progressBar: ProgressBar
    private lateinit var swipeContainer: SwipeRefreshLayout
    private val photoSizeSelectionHelper: PhotoSizeSelectionHelper = PhotoSizeSelectionHelper()
    private val photoGridAdapter = PhotoGridAdapter(photoSizeSelectionHelper)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_grid)
        restorePhotoSizeSelection(savedInstanceState)
        photoListView = findViewById(R.id.photo_grid)
        progressBar = findViewById(R.id.progress_bar)
        swipeContainer = findViewById(R.id.swipeContainer)
        setRefreshColorScheme(swipeContainer)
        showProgressIndicator()
        val layoutManager = setSizeAndOrientationBasedLayoutManager()

        photoListView.layoutManager = layoutManager
        photoListView.adapter = photoGridAdapter
        val photoApp = application as PhotoApp

        galleryPresenter = GalleryPresenter(photoApp.apolloClient, this)
        galleryPresenter.setUpGalleryDataSource(
                photoGridAdapter,
                this
        )
        galleryPresenter.setupPullToRefreshListener(swipeContainer, photoGridAdapter)

        photoGridAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                dismissPullToRefreshIndicator()
                if (itemCount != 0) dismissProgressIndicator()
            }


        })
    }

    /**
     * This method returns a layout manager based on photo size and
     * orientation
     */
    private fun setSizeAndOrientationBasedLayoutManager(): GridLayoutManager {
        val layoutManager = GridLayoutManager(this, 12)
        layoutManager.spanSizeLookup = object : GridLayoutManager.SpanSizeLookup() {
            override fun getSpanSize(position: Int): Int {
                return GridSpanUtil().configureGridSpan(
                        isLandscape(),
                        photoSizeSelectionHelper.getPhotoSizeSelection()
                )
            }
        }
        return layoutManager
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.size_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        photoSizeSelectionHelper.setPhotoSizeSelection(item.title.toString())
        galleryPresenter.refresh()
        return super.onOptionsItemSelected(item)
    }

    private fun isLandscape() =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    /**
     * This method is invoked on failed photo album request and pops a
     * snackBar display to the user with option to retry
     */
    override fun onFailedRequest(requestFailure: RequestFailure) {
        dismissProgressIndicator()
        dismissPullToRefreshIndicator()
        Snackbar.make(
                findViewById<View>(android.R.id.content),
                requestFailure.errorMsg.toString(),
                Snackbar.LENGTH_INDEFINITE
        )
                .setAction("RETRY") {
                    // Retry the failed request
                    showProgressIndicator()
                    requestFailure.retryable.retry()
                }.show()
    }

    private fun dismissPullToRefreshIndicator() {
        if (swipeContainer.isRefreshing) swipeContainer.isRefreshing = false
    }

    private fun showProgressIndicator() {
        progressBar.visibility = VISIBLE
    }

    private fun dismissProgressIndicator() {
        progressBar.visibility = GONE
    }

    private fun setRefreshColorScheme(swipeContainer: SwipeRefreshLayout) {
        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,
                android.R.color.holo_green_light,
                android.R.color.holo_orange_light,
                android.R.color.holo_red_light);
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SIZE_STATE_KEY, photoSizeSelectionHelper.getPhotoSizeSelection())
        super.onSaveInstanceState(outState)
    }

    private fun restorePhotoSizeSelection(savedInstanceState: Bundle?) {
        photoSizeSelectionHelper.setPhotoSizeSelection(savedInstanceState?.getString(SIZE_STATE_KEY))
    }
}
