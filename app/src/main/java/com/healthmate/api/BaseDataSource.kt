package com.healthmate.api

import com.google.gson.Gson
import com.healthmate.common.functions.replaceEmpty
import retrofit2.Response
import timber.log.Timber

abstract class BaseDataSource {
    protected suspend fun <T> getResult(call: suspend () -> Response<DataResponse<T>>) : Result<T> {
        try{
            val response = call()
            val body = response.body()
            if (body!=null){
                if (body.responseCode in 200..299){
                    return Result.success(body.data,body.message.replaceEmpty("-"),200,body.cursor) as Result<T>
                }else{
                    return error(body.message,body.responseCode)
                }
            }else{
                return error("Failed. Response not found.",response.code())
            }
        }catch (e: Exception){
            return error("Network call has failed for a following reason: ${e.message ?: e.toString()}")
        }
    }

    private fun <T> error(message: String,responseCode: Int = 400): Result<T>{
        Timber.e(message)
        return Result.error(" $message",null,responseCode)
    }
}