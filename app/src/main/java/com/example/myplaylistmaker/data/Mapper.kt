package com.example.myplaylistmaker.data

interface Mapper<In, Out> {

    fun mapItem(item: In): Out

    fun mapItems(items: List<In>): List<Out> {
        return items.map { item -> mapItem(item) }
    }
}