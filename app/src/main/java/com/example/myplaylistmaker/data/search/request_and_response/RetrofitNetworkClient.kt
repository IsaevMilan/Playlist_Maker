package com.example.myplaylistmaker.data.search.request_and_response

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitNetworkClient {
    class RetrofitNetworkClient : NetworkClient {
        private val iTunesBaseURL = "https://itunes.apple.com"

        private val retrofit = Retrofit.Builder()
            .baseUrl(iTunesBaseURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        private val iTunesService = retrofit.create(iTunesSearchAPI::class.java)


        override fun doRequest(dto: Any): Response {
            if (dto is TrackSearchRequest) {
                val resp = iTunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                return body.apply { resultCode = resp.code() }
            } else {
                return Response().apply { resultCode = 400 }
            }
        }
    }
}