package com.healthmate.api

data class Result<out T>(val status: Status, val data: T?, val message: String?, val response_code: Int=0, val cursor: String=""){
    enum class Status {
        SUCCESS,
        ERROR,
        LOADING
    }

    companion object{
        fun <T> success(data: T?,message: String = "",response_code: Int, cursor: String): Result<T>{
            return Result(Status.SUCCESS,data,message,response_code, cursor)
        }

        fun <T> error(message: String, data: T? = null,response_code: Int = 420): Result<T>{
            return Result(Status.ERROR,data,message,response_code)
        }

        fun <T> loading(data: T? = null): Result<T>{
            return Result(Status.LOADING,data,null)
        }
    }
}