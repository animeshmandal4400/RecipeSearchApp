package com.animeshmandal.recipesearchapp.data.database.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.animeshmandal.recipesearchapp.data.database.*

class NutritionConverter {
    
    @TypeConverter
    fun fromNutritionEntity(value: NutritionEntity?): String? {
        return if (value == null) null else Gson().toJson(value)
    }
    
    @TypeConverter
    fun toNutritionEntity(value: String?): NutritionEntity? {
        return if (value == null) null else Gson().fromJson(value, NutritionEntity::class.java)
    }
    
    @TypeConverter
    fun fromNutrientEntityList(value: List<NutrientEntity>?): String? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<NutrientEntity>>() {}.type
            Gson().toJson(value, listType)
        }
    }
    
    @TypeConverter
    fun toNutrientEntityList(value: String?): List<NutrientEntity>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<NutrientEntity>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
    
    @TypeConverter
    fun fromPropertyEntityList(value: List<PropertyEntity>?): String? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<PropertyEntity>>() {}.type
            Gson().toJson(value, listType)
        }
    }
    
    @TypeConverter
    fun toPropertyEntityList(value: String?): List<PropertyEntity>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<PropertyEntity>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
    
    @TypeConverter
    fun fromFlavonoidEntityList(value: List<FlavonoidEntity>?): String? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<FlavonoidEntity>>() {}.type
            Gson().toJson(value, listType)
        }
    }
    
    @TypeConverter
    fun toFlavonoidEntityList(value: String?): List<FlavonoidEntity>? {
        return if (value == null) null else {
            val listType = object : TypeToken<List<FlavonoidEntity>>() {}.type
            Gson().fromJson(value, listType)
        }
    }
    
    @TypeConverter
    fun fromCaloricBreakdownEntity(value: CaloricBreakdownEntity?): String? {
        return if (value == null) null else Gson().toJson(value)
    }
    
    @TypeConverter
    fun toCaloricBreakdownEntity(value: String?): CaloricBreakdownEntity? {
        return if (value == null) null else Gson().fromJson(value, CaloricBreakdownEntity::class.java)
    }
    
    @TypeConverter
    fun fromWeightPerServingEntity(value: WeightPerServingEntity?): String? {
        return if (value == null) null else Gson().toJson(value)
    }
    
    @TypeConverter
    fun toWeightPerServingEntity(value: String?): WeightPerServingEntity? {
        return if (value == null) null else Gson().fromJson(value, WeightPerServingEntity::class.java)
    }
}