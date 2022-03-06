package com.example.grassdoorapptask.repository

import com.example.grassdoorapptask.model.ResponsePopularMovie
import io.reactivex.Observable

interface AppRepository {

    fun getMovieData(page:String): Observable<ResponsePopularMovie>
}