package com.walphotodem.pmd.walphotoedem.networking

import android.content.Context
import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.cache.http.HttpCachePolicy
import com.apollographql.apollo.cache.http.ApolloHttpCache
import com.apollographql.apollo.cache.http.DiskLruHttpCacheStore
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import type.CustomType


/**
 */

class GraphClient {

    companion object {
        private const val COOKIE_HEADER = "Cookie"
    }

    fun buildClient(applicationContext: Context): ApolloClient {
        val size = 1024L * 1024L
        val cacheStore = DiskLruHttpCacheStore(applicationContext.cacheDir, size)


        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
        val authInterceptor = { chain: Interceptor.Chain ->
            val original = chain.request()
            val request = original.newBuilder()
                    .header(COOKIE_HEADER, AUTH_COOKIE)
                    .method(original.method(), original.body())
                    .build()
            chain.proceed(request)
        }
        val okHttpClient = OkHttpClient.Builder()
// Apollo Caching will now work when Http Logging is set as normal interceptor team will fix
// in future hopefully
// https://github.com/apollographql/apollo-android/issues/638
                .addNetworkInterceptor(httpLoggingInterceptor)
                .addInterceptor(authInterceptor)
                .build()

        return ApolloClient.builder()
                .serverUrl(BASE_URL)
                .httpCache(ApolloHttpCache(cacheStore))
                .defaultHttpCachePolicy(HttpCachePolicy.CACHE_FIRST)
                .okHttpClient(okHttpClient)
                .addCustomTypeAdapter(CustomType.RECORDID, RecordIDCustomTypeAdapter())
                .build()
    }
}