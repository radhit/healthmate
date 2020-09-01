package com.healthmate.api

data class Result<out T>(val status: Status, val data: T?, val message: String?, val response_code: Int=0){
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object{
        fun <T> success(data: T?,message: String = "",response_code: Int): Result<T>{
            return Result(Status.SUCCESS,data,message,response_code)
        }

        fun <T> error(message: String, data: T? = null,response_code: Int = 420): Result<T>{
            return Result(Status.ERROR,data,message,response_code)
        }

        fun <T> loading(data: T? = null): Result<T>{
            return Result(Status.LOADING,data,null)
        }
    }
}