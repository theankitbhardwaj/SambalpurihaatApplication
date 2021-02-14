package com.inces.incesclient.db

import androidx.room.TypeConverter
import com.google.gson.Gson

import com.google.gson.reflect.TypeToken
import com.inces.incesclient.models.Product
import com.inces.incesclient.models.Variant
import java.lang.reflect.Type


class Converter {
    @TypeConverter
    fun variantsListToString(value: List<Variant>?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun variantsStringToList(value: String?): List<Variant> {
        val type: Type = object : TypeToken<List<Variant>?>() {}.type
        return Gson().fromJson<List<Variant>>(value, type)
    }

    @TypeConverter
    fun productToString(value: Product?): String {
        return Gson().toJson(value)
    }

    @TypeConverter
    fun stringToProduct(value: String?): Product {
        val type: Type = object : TypeToken<Product>() {}.type
        return Gson().fromJson<Product>(value, type)
    }




}