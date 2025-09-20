package com.animeshmandal.recipesearchapp.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.animeshmandal.recipesearchapp.data.database.IngredientEntity

class IngredientConverter {
    
    @TypeConverter
    fun fromIngredientEntityList(value: List<IngredientEntity>?): String? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<IngredientEntity>>() {}.type
            Gson().toJson(value, listType)
        }
    }
    
    @TypeConverter
    fun toIngredientEntityList(value: String?): List<IngredientEntity>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<IngredientEntity>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
}