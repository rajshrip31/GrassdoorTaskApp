package com.example.grassdoorapptask.viewmodel

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.example.grassdoorapptask.core.BaseViewModel
import com.example.grassdoorapptask.model.Result
import com.example.grassdoorapptask.room.AppDataBase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MovieDetailsViewModel (application: Application) : BaseViewModel(application) {

    private var mContext: Context = application
    var mdAppDatabase: AppDataBase? = null
    var movieLiveData = MutableLiveData<Result>()

    init {
        mdAppDatabase = AppDataBase.getDbClient(mContext)
    }

    fun getMovieDetails(id:String){
        CoroutineScope(Dispatchers.IO).launch {
            val id = mdAppDatabase?.getDaoMovieAccess()?.getMovieByID(Integer.parseInt(id))!!
            movieLiveData.postValue(id)

        }
    }
}