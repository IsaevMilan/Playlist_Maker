package com.example.myplaylistmaker.data.search.request_and_response


interface NetworkClient {
    suspend fun doRequest (dto: Any): Response


}
