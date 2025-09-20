package com.animeshmandal.recipesearchapp.data.database

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.animeshmandal.recipesearchapp.data.database.converter.ListStringConverter
import com.animeshmandal.recipesearchapp.data.database.converter.NutritionConverter
import com.animeshmandal.recipesearchapp.data.database.converter.IngredientConverter
import com.animeshmandal.recipesearchapp.data.database.converter.EquipmentConverter

@Entity(tableName = "recipes")
@TypeConverters(
    ListStringConverter::class,
    NutritionConverter::class,
    IngredientConverter::class,
    EquipmentConverter::class
)
data class RecipeEntity(
    @PrimaryKey
    val id: Int,
    
    val title: String,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val pricePerServing: Double,
    val summary: String,
    val instructions: String?,
    val cuisines: List<String>?,
    val dishTypes: List<String>?,
    val diets: List<String>?,
    val veryHealthy: Boolean?,
    val cheap: Boolean?,
    val veryPopular: Boolean?,
    val sustainable: Boolean?,
    val weightWatcherSmartPoints: Int?,
    val gaps: String?,
    val lowFodmap: Boolean?,
    val aggregateLikes: Int?,
    val spoonacularScore: Double?,
    val healthScore: Double?,
    val creditsText: String?,
    val license: String?,
    val sourceName: String?,
    val sourceUrl: String?,
    val extendedIngredients: List<IngredientEntity>?,
    val equipment: List<EquipmentEntity>?,
    val nutrition: NutritionEntity?,
    val isFavorite: Boolean = false,
    val cachedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "ingredients")
data class IngredientEntity(
    @PrimaryKey
    val id: Int,
    
    val recipeId: Int,
    val name: String,
    val original: String,
    val originalName: String,
    val amount: Double,
    val unit: String,
    val image: String?,
    val consistency: String?,
    val aisle: String?
)

@Entity(tableName = "equipment")
data class EquipmentEntity(
    @PrimaryKey
    val id: Int,
    
    val recipeId: Int,
    val name: String,
    val image: String?,
    val temperature: Double?,
    val temperatureUnit: String?
)

@Entity(tableName = "nutrition")
data class NutritionEntity(
    @PrimaryKey
    val recipeId: Int,
    
    val nutrients: List<NutrientEntity>?,
    val properties: List<PropertyEntity>?,
    val flavonoids: List<FlavonoidEntity>?,
    val caloricBreakdown: CaloricBreakdownEntity?,
    val weightPerServing: WeightPerServingEntity?
)

@Entity(tableName = "nutrients")
data class NutrientEntity(
    @PrimaryKey
    val id: String, // name + recipeId
    
    val recipeId: Int,
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double?
)

@Entity(tableName = "properties")
data class PropertyEntity(
    @PrimaryKey
    val id: String, // name + recipeId
    
    val recipeId: Int,
    val name: String,
    val amount: Double,
    val unit: String
)

@Entity(tableName = "flavonoids")
data class FlavonoidEntity(
    @PrimaryKey
    val id: String, // name + recipeId
    
    val recipeId: Int,
    val name: String,
    val amount: Double,
    val unit: String
)

@Entity(tableName = "caloric_breakdown")
data class CaloricBreakdownEntity(
    @PrimaryKey
    val recipeId: Int,
    
    val percentProtein: Double,
    val percentFat: Double,
    val percentCarbs: Double
)

@Entity(tableName = "weight_per_serving")
data class WeightPerServingEntity(
    @PrimaryKey
    val recipeId: Int,
    
    val amount: Int,
    val unit: String
)