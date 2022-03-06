package com.example.grassdoorapptask.api

import io.reactivex.android.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import java.util.logging.Logger

object RetrofitApiClient {
    private const val BASE_URL = "https://api.themoviedb.org/"

    private val logger: Logger = Logger.getLogger("Grassdoor Retrofit") //Logger.getLogger(RetrofitApiClient::class.java.simpleName)
    private var apiClient: Retrofit? = null

    private fun createClient(): Retrofit {
        apiClient = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create())
            .client(getOkHttpClient())
            .build()

        return apiClient!!
    }


    private fun getOkHttpClient(): OkHttpClient {

        val okHttpClient = OkHttpClient.Builder()
        okHttpClient.addInterceptor { chain ->
            var originalRequest = chain.request()
            var request = originalRequest.newBuilder()
                .addHeader("Cache-Control", "no-cache")
                .build()

            chain.proceed(request)
        }

        okHttpClient.connectTimeout(90, TimeUnit.SECONDS)
        okHttpClient.readTimeout(90, TimeUnit.SECONDS)

        if (BuildConfig.DEBUG) { //BuildConfig.DEBUG
            var loggingInterceptor =
                HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message ->
                    logger.info(
                        message
                    )
                })

            loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY
            okHttpClient.addInterceptor(loggingInterceptor)
        }
        return okHttpClient.build()
    }

    @JvmStatic
    fun getAPIClient(): Retrofit {
        return apiClient
            ?: createClient()
    }


}