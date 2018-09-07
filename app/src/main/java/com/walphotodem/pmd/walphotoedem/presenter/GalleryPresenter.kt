package com.walphotodem.pmd.walphotoedem.presenter

import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.paging.LivePagedListBuilder
import android.arch.paging.PagedList
import com.apollographql.apollo.ApolloClient
import com.walphotodem.pmd.walphotoedem.adapter.PhotoGridAdapter
import com.walphotodem.pmd.walphotoedem.data.DataSourceFactory
import com.walphotodem.pmd.walphotoedem.data.RequestFailure

/**
 * This class acts as the presenter for any PhotoGallery View Controller containing
 * a PhotoGridAdapter. Sets up the data source for the adapter
 */
class GalleryPresenter(
        private val apolloClient: ApolloClient,
        private val failedRequestListener: OnFailedRequestListener
) {


    interface OnFailedRequestListener {
        fun onFailedRequest(requestFailure: RequestFailure)
    }

    companion object {
        const val PAGE_LIMIT = 12
        const val INITIAL_LOAD_SIZE_HINT = 15
        const val PREFETCH_DISTANCE = 3
    }

    fun setUpGalleryDataSource(photoGridAdapter: PhotoGridAdapter, owner: LifecycleOwner) {
        val pagedListConfig = createPagedListConfig()


        val dataSourceFactory = DataSourceFactory(apolloClient)
        val liveData = LivePagedListBuilder(
                dataSourceFactory,
                pagedListConfig
        ).build()

        liveData.observe(owner, Observer {
            photoGridAdapter.submitList(it)
        })

        dataSourceFactory.photoLiveData.observe(owner, Observer {
            it?.requestFailureLiveData?.observe(
                    owner,
                    Observer<RequestFailure> { requestFailure ->
                        if (requestFailure != null) {
                            failedRequestListener.onFailedRequest(requestFailure)
                        }
                    }
            )
        })
    }

    private fun createPagedListConfig(): PagedList.Config {
        return PagedList.Config.Builder().setEnablePlaceholders(true)
                .setPageSize(PAGE_LIMIT)
                .setInitialLoadSizeHint(INITIAL_LOAD_SIZE_HINT)
                .setPrefetchDistance(PREFETCH_DISTANCE)
                .setEnablePlaceholders(false)
                .build()
    }

}