package com.example.myplaylistmaker.data.search.request_and_response

import com.example.myplaylistmaker.data.search.request_and_response.Response


interface NetworkClient {
    fun doRequest (dto:Any) : Response
}