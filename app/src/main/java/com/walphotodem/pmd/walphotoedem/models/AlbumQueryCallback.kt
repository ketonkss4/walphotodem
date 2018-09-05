package com.walphotodem.pmd.walphotoedem.models

import AlbumQuery
import com.apollographql.apollo.ApolloCall
import com.apollographql.apollo.api.Response
import com.apollographql.apollo.exception.ApolloException

/**
 */
class AlbumQueryCallback(private val requestListener: RequestListener) : ApolloCall.Callback<AlbumQuery.Data>() {

    interface RequestListener {
        fun onSuccess(photos: List<AlbumQuery.Record>)
        fun onFailure(exceptionMessage: String?)
    }

    override fun onResponse(response: Response<AlbumQuery.Data>) {
        val records = parseRecords(response)
        if (records != null) {
            requestListener.onSuccess(records)
        } else {
            onFailure(ApolloException("No Photos Available"))
        }
    }

    private fun parseRecords(response: Response<AlbumQuery.Data>): MutableList<AlbumQuery.Record>? =
            response.data()?.album()?.photos()?.records()


    override fun onFailure(e: ApolloException) {
        requestListener.onFailure(e.message)
    }

}