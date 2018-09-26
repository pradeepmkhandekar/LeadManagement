package com.pb.leadmanagement.core

import com.google.gson.GsonBuilder
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * Created by Nilesh Birhade on 17-08-2018.
 */
open class RetroRequestBuilder {
    internal var restAdapter: Retrofit? = null
    var URL = "http://202.131.96.100:7541/"

    protected fun build(): Retrofit {
        if (restAdapter == null) {

            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
            val gson = GsonBuilder()
                    .serializeNulls()
                    // .setLenient()
                    .create()

            val okHttpClient = okhttp3.OkHttpClient.Builder()
                    .connectTimeout(1, TimeUnit.MINUTES)
                    .writeTimeout(1, TimeUnit.MINUTES)
                    .readTimeout(1, TimeUnit.MINUTES)
                    .addInterceptor(interceptor)
                    .build()

            restAdapter = Retrofit.Builder()
                    .baseUrl(URL)
                    .client(okHttpClient)
                    .addConverterFactory(GsonConverterFactory.create(gson))
                    .build()

        }
        return restAdapter as Retrofit
    }
}