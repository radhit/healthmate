package com.healthmate.api

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.lifecycle.map
import com.healthmate.common.functions.replaceEmpty
import kotlinx.coroutines.Dispatchers
import okhttp3.Dispatcher

fun <T,A> resultLiveData(
    databaseQuery: ()->LiveData<T>,
    networkCall: suspend () -> Result<A>,
    saveCallResult: suspend (A) -> Unit
): LiveData<Result<T>> =
    liveData(Dispatchers.IO){
        emit(Result.loading())
        val source = databaseQuery.invoke().map {
            Result.success(it,"Berhasil.",200)
        }
        emitSource(source)

        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Result.Status.SUCCESS){
            saveCallResult(responseStatus.data!!)
        }else if(responseStatus.status == Result.Status.ERROR){
            emit(Result.error(responseStatus.message!!))
            emitSource(source)
        }
    }

fun <A> resultLiveDataNoDao(
    networkCall: suspend () -> Result<A>
): LiveData<Result<A>> =
    liveData(Dispatchers.IO){
        emit(Result.loading())
        val responseStatus = networkCall.invoke()
        if (responseStatus.status == Result.Status.SUCCESS){
            println("resultLiveDataNoDao SUCCESS")
            emit(Result.success(responseStatus.data,responseStatus.message.replaceEmpty(""),responseStatus.response_code))
        }else if(responseStatus.status == Result.Status.ERROR){
            println("resultLiveDataNoDao ERROR")
            emit(Result.error(responseStatus.message!!))
        }
    }