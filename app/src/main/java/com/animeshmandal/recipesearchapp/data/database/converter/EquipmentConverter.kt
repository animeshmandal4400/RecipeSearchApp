package com.animeshmandal.recipesearchapp.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.animeshmandal.recipesearchapp.data.database.EquipmentEntity

class EquipmentConverter {
    
    @TypeConverter
    fun fromEquipmentEntityList(value: List<EquipmentEntity>?): String? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<EquipmentEntity>>() {}.type
            Gson().toJson(value, listType)
        }
    }
    
    @TypeConverter
    fun toEquipmentEntityList(value: String?): List<EquipmentEntity>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<EquipmentEntity>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
}