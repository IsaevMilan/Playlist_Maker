package com.example.myplaylistmaker.data.search.request_and_response

import com.example.myplaylistmaker.domain.search.ErrorClass

sealed class Resource<T>(val data: T? = null, val message: ErrorClass? = null) {
    class Success<T>(data: T): Resource<T>(data)
    class Error<T>(message: ErrorClass?, data: T? = null): Resource<T>(data, message)
}