package com.inces.incesclient.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
        private const val BASE_URL = "https://sambalpurihaat.com/public/"

    private val instance by lazy {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC)
        val client = OkHttpClient.Builder()
            .addNetworkInterceptor {
                val original = it.request()
                val requestBuilder = original.newBuilder()
                    .method(original.method, original.body)
                it.proceed(requestBuilder.build())
            }
            .addInterceptor(loggingInterceptor)
            .build()
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(client)
            .build()
    }

    val api: Api by lazy {
        instance.create(Api::class.java)
    }
}