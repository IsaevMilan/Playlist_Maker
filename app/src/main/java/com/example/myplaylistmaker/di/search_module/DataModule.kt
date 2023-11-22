package com.example.myplaylistmaker.di.search_module

import android.content.Context
import com.example.myplaylistmaker.app.App
import com.example.myplaylistmaker.data.search.history.SEARCH_SHARED_PREFS_KEY
import com.example.myplaylistmaker.data.search.request_and_response.ITunesSearchAPI
import com.example.myplaylistmaker.data.search.request_and_response.NetworkClient
import com.example.myplaylistmaker.data.search.request_and_response.RetrofitNetworkClient
import com.example.myplaylistmaker.data.search.request_and_response.SearchMapper
import com.google.gson.Gson
import org.koin.android.ext.koin.androidApplication
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://itunes.apple.com"

val dataModule = module {

    single<ITunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ITunesSearchAPI::class.java)
    }
    single {
        androidApplication() as App
    }

    single {
        androidContext()
            .getSharedPreferences(SEARCH_SHARED_PREFS_KEY, Context.MODE_PRIVATE)
    }
    single<NetworkClient> {
        RetrofitNetworkClient(get(), get(), get())
    }

    factory { Gson() }
    factory { SearchMapper() }



}

