package com.example.grassdoorapptask.repository

import android.content.Context
import com.example.grassdoorapptask.api.IApiService
import com.example.grassdoorapptask.model.ResponsePopularMovie
import com.example.grassdoorapptask.room.AppDataBase
import io.reactivex.Observable
import io.reactivex.ObservableEmitter
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ImplAppRepository(private val iApiService: IApiService, private val mContext: Context) :
    AppRepository {

    private fun responseError(observableEmitter: ObservableEmitter<ResponsePopularMovie>) {
        val response = ResponsePopularMovie(0, emptyList(),0,0)
        observableEmitter.onNext(response)
        observableEmitter.onComplete()
    }

    override fun getMovieData(page:String): Observable<ResponsePopularMovie> {
        return Observable.create {
            var key:String = "8ca24adbf94e609968cf32f1383af485"
            val apiCall = iApiService.getMovieList(key) //page

            apiCall.enqueue(object : Callback<ResponsePopularMovie> {
                override fun onFailure(call: Call<ResponsePopularMovie>, t: Throwable) {
                    val response = ResponsePopularMovie(0, emptyList(),0,0)
                    it.onNext(response)
                    it.onComplete()
                }

                override fun onResponse(call: Call<ResponsePopularMovie>, response: Response<ResponsePopularMovie>) {
                    when {
                        response.isSuccessful -> {
                            val responseData = response.body()
                            if (responseData != null) {
                                CoroutineScope(Dispatchers.IO).launch {
                                    for (item in responseData.results!!) {
                                        val mdAppDatabase = AppDataBase.getDbClient(mContext)
                                        mdAppDatabase.getDaoMovieAccess().insertMovie(item)
                                    }
                                }
                                it.onNext(responseData)
                                it.onComplete()
                            }else{
                                responseError(it)
                            }
                        }
                        else -> {
                            responseError(it)
                        }
                    }
                }
            })
        }
    }

}