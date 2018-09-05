package com.walphotodem.pmd.walphotoedem.presenter

import AlbumQuery
import com.apollographql.apollo.ApolloClient
import com.walphotodem.pmd.walphotoedem.models.AlbumQueryCallback

/**
 */
class GallaryPresenter {

    fun requestPhotos(apolloClient: ApolloClient, queryCallback: AlbumQueryCallback) {
        apolloClient.query(AlbumQuery.builder().build()).enqueue(queryCallback)
    }
}