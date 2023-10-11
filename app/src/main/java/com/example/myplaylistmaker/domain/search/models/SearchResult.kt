package com.example.myplaylistmaker.domain.search.models

const val ERROR_NO_CONNECTION = 100
const val ERROR_UNKNOWN = 300
@JvmInline
value class SearchErrorType private constructor(val error: Int) {
    companion object {
        fun noConnection() = SearchErrorType(ERROR_NO_CONNECTION)

        fun unknownException() = SearchErrorType(ERROR_UNKNOWN)
    }
}

sealed interface SearchResult {
    data class Success(val list: List<Track>) : SearchResult

    data class Failure(val errorType: SearchErrorType) : SearchResult
}