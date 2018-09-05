package com.walphotodem.pmd.walphotoedem.networking

import com.apollographql.apollo.ApolloClient
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import type.CustomType


/**
 */
class GraphClient {

    private val COOKIE_HEADER = "Cookie"

    fun buildClient(): ApolloClient {
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val authInterceptor = { chain : Interceptor.Chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .header(COOKIE_HEADER, AUTH_COOKIE)
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpLoggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.RECORDID, RecordIDCustomTypeAdapter())
                .build()
    }
}