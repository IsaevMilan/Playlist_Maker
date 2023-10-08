package com.example.myplaylistmaker.data.search.request_and_response

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.example.myplaylistmaker.domain.search.models.SearchErrorType
import com.example.myplaylistmaker.domain.search.models.SearchResult
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val CODE_OK = 200
class RetrofitNetworkClient(private val context: Context) : NetworkClient {
    private val iTunesBaseURL = "https://itunes.apple.com"

    private val retrofit = Retrofit.Builder()
        .baseUrl(iTunesBaseURL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    private val iTunesService = retrofit.create(iTunesSearchAPI::class.java)
    private val mapper = SearchMapper()


    override fun doRequest(request: TrackSearchRequest): SearchResult {
        return try {
            val response = iTunesService.search(request.expression).execute()
            val list = if (response.code() != CODE_OK) {
                emptyList()
            } else {
                response.body()?.results.orEmpty()
            }
            SearchResult.Success(mapper.mapItems(list))
        } catch (exception: Exception) {
            when (isConnected()) {
                true -> SearchResult.Failure(SearchErrorType.unknownException())
                false -> SearchResult.Failure(SearchErrorType.noConnection())
            }
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)
        if (capabilities != null) {
            when {
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> return true
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> return true
            }

        }
        return false
    }
}



