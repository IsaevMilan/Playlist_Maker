package com.example.myplaylistmaker.di.search_module

import android.content.Context
import com.example.myplaylistmaker.data.local.SearchHistoryStorage
import com.example.myplaylistmaker.data.local.SharedPreferencesSearchHistoryStorage
import com.example.myplaylistmaker.data.search.request_and_response.SearchMapper
import com.example.myplaylistmaker.data.search.request_and_response.iTunesSearchAPI
import com.google.gson.Gson
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://itunes.apple.com"

val dataModule = module {

    single<iTunesSearchAPI> {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(iTunesSearchAPI::class.java)
    }

    single {
        androidContext()
            .getSharedPreferences("local_storage", Context.MODE_PRIVATE)
    }

    factory { Gson() }
    factory { SearchMapper() }

    single <SearchHistoryStorage> {
        SharedPreferencesSearchHistoryStorage(get(), get())
    }

}

