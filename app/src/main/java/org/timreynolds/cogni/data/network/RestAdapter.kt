

package org.timreynolds.cogni.data.network

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * RestAdapter
 */
class RestAdapter {

    companion object {
        private val API_BASE = "https://cashback-explorer-api.herokuapp.com/"
    }

    val apiHelper: ApiHelper

    init {

        val interceptor : HttpLoggingInterceptor = HttpLoggingInterceptor().apply {
            this.level = HttpLoggingInterceptor.Level.BODY
            //this.level = HttpLoggingInterceptor.Level.BASIC
        }

        val client : OkHttpClient = OkHttpClient.Builder().apply {
            this.addInterceptor(interceptor)
        }.build()

        val retrofit = Retrofit.Builder()
                .baseUrl(API_BASE)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build()

        apiHelper = retrofit.create(ApiHelper::class.java)
    }
}
