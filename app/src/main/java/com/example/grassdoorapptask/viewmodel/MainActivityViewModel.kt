package com.example.grassdoorapptask.viewmodel

import android.app.Application
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.example.grassdoorapptask.R
import com.example.grassdoorapptask.api.IApiService
import com.example.grassdoorapptask.api.RetrofitApiClient
import com.example.grassdoorapptask.core.BaseViewModel
import com.example.grassdoorapptask.repository.ImplAppRepository
import com.example.grassdoorapptask.room.AppDataBase
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainActivityViewModel(application: Application) : BaseViewModel(application) {

    private var mContext: Context = application
    private var iApiService: IApiService
    var mdAppDatabase: AppDataBase? = null
    var moviewListLiveData = MutableLiveData<List<com.example.grassdoorapptask.model.Result>>()
    private lateinit var list : List<com.example.grassdoorapptask.model.Result>

    init {
        var apiClient = RetrofitApiClient.getAPIClient()
        iApiService = apiClient.create(IApiService::class.java)
        mdAppDatabase = AppDataBase.getDbClient(mContext)
    }

    fun getMovieData(page:String) {
        loadingLiveData.value = true
        CoroutineScope(Dispatchers.IO).launch {
            list = mdAppDatabase?.getDaoMovieAccess()?.getAllMovie()!!
            if (list.isNotEmpty()) {
                loadingLiveData.postValue(false)
                moviewListLiveData.postValue(list)
            } else {
                fetchFromNetwork(page)
            }
        }

    }

    private fun fetchFromNetwork(page:String) {
        val disposable = CompositeDisposable()
        val observable = ImplAppRepository(iApiService,mContext).getMovieData(page)
        val dispose = observable
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe { response ->
                loadingLiveData.value = false
                if (response != null) {
                    Log.d("Response", "Response Movie data success:$response")
                    moviewListLiveData.value = response.results
                        errorLiveData.value = mContext.resources.getString(R.string.server_error)
                } else {
                    errorLiveData.value = mContext.resources.getString(R.string.server_error)
                }
            }

        disposable.add(dispose)
    }


}