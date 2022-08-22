package com.bchmsl.task6.util

import retrofit2.Response

sealed class ResponseHandler(val isLoading: Boolean = false){
    class Success<T>(val data: T): ResponseHandler()
    class Error(val throwable: Throwable): ResponseHandler()
    class Loading(): ResponseHandler(true)
}
