package com.example.project.converter

import androidx.room.TypeConverter
import com.example.project.data.entity.Keranjang
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class KeranjangListConverter {
    @TypeConverter
    fun fromString(value: String): List<Keranjang> {
        val listType = object : TypeToken<List<Keranjang>>() {}.type
        return Gson().fromJson(value, listType)
    }

    @TypeConverter
    fun fromList(list: List<Keranjang>): String {
        return Gson().toJson(list)
    }
}