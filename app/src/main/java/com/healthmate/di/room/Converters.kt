package com.artcak.starter.di.room

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {
    companion object {
        @TypeConverter
        @JvmStatic
        fun fromString(value: String): List<String> {
            val typeToken = object : TypeToken<List<String>>() {}.type
            val freshData = Gson().fromJson<List<String>>(value, typeToken)
            return freshData
        }

        @TypeConverter
        @JvmStatic
        fun fromListString(list: List<String>): String {
            val gson = Gson()
            return gson.toJson(list)
        }
    }
}