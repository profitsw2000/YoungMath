package ru.profitsw2000.data.room.mappers

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.lang.reflect.Type
import java.util.Date

class Converter {

    @TypeConverter
    fun fromIntList(intList: List<Int>): String = Gson().toJson(intList)

    @TypeConverter
    fun toIntList(string: String): List<Int> {
        val intList: Type = object : TypeToken<List<Int>>() {}.type
        return Gson().fromJson(string, intList)
    }

    @TypeConverter
    fun fromFloatList(floatList: List<Float>): String = Gson().toJson(floatList)

    @TypeConverter
    fun toFloatList(string: String): List<Float> {
        val floatList: Type = object : TypeToken<List<Float>>() {}.type
        return Gson().fromJson(string, floatList)
    }

    @TypeConverter
    fun toDate(dateLong: Long): Date {
        return Date(dateLong)
    }

    @TypeConverter
    fun fromDate(date: Date): Long {
        return date.time
    }
}