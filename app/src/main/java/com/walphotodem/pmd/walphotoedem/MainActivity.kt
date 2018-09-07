package com.walphotodem.pmd.walphotoedem

import android.content.res.Configuration
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.View.GONE
import android.view.View.VISIBLE
import android.widget.ProgressBar
import com.walphotodem.pmd.walphotoedem.Util.PhotoSizeSelectionHelper
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
    private val photoSizeSelectionHelper: PhotoSizeSelectionHelper = PhotoSizeSelectionHelper()
    private val photoGridAdapter = PhotoGridAdapter(photoSizeSelectionHelper)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_grid)
        photoListView = findViewById(R.id.photo_grid)
        progressBar = findViewById(R.id.progress_bar)


        val layoutManager = if (isLandScape()) {
            GridLayoutManager(this, 6)
        } else {
            GridLayoutManager(this, 3)
        }

        photoListView.layoutManager = layoutManager
        photoListView.adapter = photoGridAdapter
        val photoApp = application as PhotoApp

        galleryPresenter = GalleryPresenter(photoApp.apolloClient, this)
        galleryPresenter.setUpGalleryDataSource(
                photoGridAdapter,
                this
        )

        photoGridAdapter.registerAdapterDataObserver(object : RecyclerView.AdapterDataObserver() {
            override fun onItemRangeInserted(positionStart: Int, itemCount: Int) {
                dismissProgressIndicator()
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.size_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        photoSizeSelectionHelper.setPhotoSizeSelection(item.title.toString())

        return super.onOptionsItemSelected(item)
    }

    private fun isLandScape() =
            resources.configuration.orientation == Configuration.ORIENTATION_LANDSCAPE

    override fun onFailedRequest(requestFailure: RequestFailure) {
        dismissProgressIndicator()
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

    private fun showProgressIndicator() {
        progressBar.visibility = VISIBLE
    }

    private fun dismissProgressIndicator() {
        progressBar.visibility = GONE
    }
}
