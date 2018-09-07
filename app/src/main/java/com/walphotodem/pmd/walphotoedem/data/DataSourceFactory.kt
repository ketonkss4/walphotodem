package com.walphotodem.pmd.walphotoedem.data

import AlbumQuery
import android.arch.lifecycle.MutableLiveData
import android.arch.paging.DataSource
import com.apollographql.apollo.ApolloClient

/**
 */
class DataSourceFactory(private val apolloClient: ApolloClient) : DataSource.Factory<Int, AlbumQuery.Record>(){
    val photoLiveData : MutableLiveData<PhotoPositionalDataSource> = MutableLiveData()

    override fun create(): DataSource<Int, AlbumQuery.Record> {
        val photoPositionalDataSource = PhotoPositionalDataSource(apolloClient)
        photoLiveData.postValue(photoPositionalDataSource)
        return photoPositionalDataSource
    }

}