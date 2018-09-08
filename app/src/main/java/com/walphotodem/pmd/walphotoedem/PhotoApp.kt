package com.walphotodem.pmd.walphotoedem

import android.app.Application
import com.apollographql.apollo.ApolloClient
import com.walphotodem.pmd.walphotoedem.networking.GraphClient

/**
 */
class PhotoApp : Application() {
    lateinit var apolloClient: ApolloClient

    override fun onCreate() {
        super.onCreate()
        apolloClient = GraphClient().buildClient(applicationContext)
    }

}