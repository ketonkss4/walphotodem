package com.walphotodem.pmd.walphotoedem

import AlbumQuery
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.walphotodem.pmd.walphotoedem.models.AlbumQueryCallback
import com.walphotodem.pmd.walphotoedem.presenter.GallaryPresenter

/**
 */
class MainActivity : AppCompatActivity(), AlbumQueryCallback.RequestListener{
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.photo_grid)
        val photoApp = application as PhotoApp
        val queryCallback = AlbumQueryCallback(this)
        GallaryPresenter().requestPhotos(photoApp.apolloClient, queryCallback)
    }

    override fun onSuccess(photos: List<AlbumQuery.Record>) {
    }

    override fun onFailure(exceptionMessage: String?) {
    }
}
