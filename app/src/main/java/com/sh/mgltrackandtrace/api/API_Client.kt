package com.sh.mgltrackandtrace.api

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


class API_Client{
    companion object {
        var loggingInterceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        var okHttpClient = OkHttpClient.Builder()
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .addInterceptor(loggingInterceptor).build()

        fun getRetrofit(): Retrofit{
            return Retrofit.Builder()
                    .baseUrl("https://mglexpress.com.mm/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .client(okHttpClient)
                    .build()
        }

        fun getNewRetrofit():Retrofit{
            return Retrofit.Builder()
                .baseUrl("http://oms.mglexpress.com.mm/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
        fun getApiRetrofit() : Retrofit{
            return Retrofit.Builder()
                .baseUrl("https://verify.smspoh.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build()
        }
    }
}