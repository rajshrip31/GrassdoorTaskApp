package com.example.grassdoorapptask.api

import com.example.grassdoorapptask.model.ResponsePopularMovie
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query


interface IApiService {

    //@GET("3/movie/popular?api_key=8ca24adbf94e609968cf32f1383af485&language=en-US&page={page}")
    @GET("3/movie/popular?")
    fun getMovieList(@Query("api_key") apiKey: String): Call<ResponsePopularMovie>
}