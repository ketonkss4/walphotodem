package com.walphotodem.pmd.walphotoedem.data

import AlbumQuery
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.PositionalDataSource
import android.util.Log
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

/**
 */
class PhotoPositionalDataSource(private val apolloClient: ApolloClient) :
        PositionalDataSource<AlbumQuery.Record>() {

    val requestFailureLiveData: MutableLiveData<RequestFailure> = MutableLiveData()

    override fun loadInitial(
            params: LoadInitialParams,
            callback: LoadInitialCallback<AlbumQuery.Record>
    ) {
        Log.e("Initial OffSet", params.requestedStartPosition.toString())

        val startPosition = if (params.requestedStartPosition <= 0) {
            params.requestedStartPosition
        } else {
            0
        }

        val albumQuery = setAlbumQueryOffset(startPosition)
        //load initial data
        apolloClient.query(albumQuery).enqueue(object : ApolloCall.Callback<AlbumQuery.Data>() {
            override fun onResponse(response: Response<AlbumQuery.Data>) {
                val records = response.data()?.album()?.photos()?.records()
                records?.let { callback.onResult(it, params.requestedStartPosition) }
            }

            override fun onFailure(e: ApolloException) {
                val retryable = object : Retryable {
                    override fun retry() {
                        loadInitial(params, callback)
                    }
                }
                handleError(retryable, e.message)
            }
        })

    }

    override fun loadRange(
            params: LoadRangeParams,
            callback: LoadRangeCallback<AlbumQuery.Record>
    ) {
        Log.e("OffSet", params.startPosition.toString())
        val albumQuery = setAlbumQueryOffset(params.startPosition)
        //load next page of results and update data
        apolloClient.query(albumQuery).enqueue(object : ApolloCall.Callback<AlbumQuery.Data>() {
            override fun onResponse(response: Response<AlbumQuery.Data>) {
                val records = response.data()?.album()?.photos()?.records()
                records?.let { callback.onResult(records) }
            }

            override fun onFailure(e: ApolloException) {

                val retryable = object : Retryable {
                    override fun retry() {
                        loadRange(params, callback)
                    }
                }
                handleError(retryable, e.message)
            }
        })

    }

    private fun setAlbumQueryOffset(startPosition: Int) =
            AlbumQuery.builder().offset(startPosition).build()

    private fun handleError(retryable: Retryable, errorMsg: String?) {
        requestFailureLiveData.postValue(RequestFailure(retryable, errorMsg))
    }

}