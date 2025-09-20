package com.animeshmandal.recipesearchapp.data.model

import com.google.gson.annotations.SerializedName

data class RecipeDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("title")
    val title: String,
    
    @SerializedName("image")
    val image: String?,
    
    @SerializedName("readyInMinutes")
    val readyInMinutes: Int,
    
    @SerializedName("servings")
    val servings: Int,
    
    @SerializedName("pricePerServing")
    val pricePerServing: Double,
    
    @SerializedName("summary")
    val summary: String,
    
    @SerializedName("instructions")
    val instructions: String?,
    
    @SerializedName("extendedIngredients")
    val extendedIngredients: List<ExtendedIngredientDto>?,
    
    @SerializedName("equipment")
    val equipment: List<EquipmentDto>?,
    
    @SerializedName("nutrition")
    val nutrition: NutritionDto?,
    
    @SerializedName("cuisines")
    val cuisines: List<String>?,
    
    @SerializedName("dishTypes")
    val dishTypes: List<String>?,
    
    @SerializedName("diets")
    val diets: List<String>?,
    
    @SerializedName("veryHealthy")
    val veryHealthy: Boolean?,
    
    @SerializedName("cheap")
    val cheap: Boolean?,
    
    @SerializedName("veryPopular")
    val veryPopular: Boolean?,
    
    @SerializedName("sustainable")
    val sustainable: Boolean?,
    
    @SerializedName("weightWatcherSmartPoints")
    val weightWatcherSmartPoints: Int?,
    
    @SerializedName("gaps")
    val gaps: String?,
    
    @SerializedName("lowFodmap")
    val lowFodmap: Boolean?,
    
    @SerializedName("aggregateLikes")
    val aggregateLikes: Int?,
    
    @SerializedName("spoonacularScore")
    val spoonacularScore: Double?,
    
    @SerializedName("healthScore")
    val healthScore: Double?,
    
    @SerializedName("creditsText")
    val creditsText: String?,
    
    @SerializedName("license")
    val license: String?,
    
    @SerializedName("sourceName")
    val sourceName: String?,
    
    @SerializedName("sourceUrl")
    val sourceUrl: String?
)

data class ExtendedIngredientDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("original")
    val original: String,
    
    @SerializedName("originalName")
    val originalName: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("unit")
    val unit: String,
    
    @SerializedName("image")
    val image: String?,
    
    @SerializedName("consistency")
    val consistency: String?,
    
    @SerializedName("aisle")
    val aisle: String?
)

data class EquipmentDto(
    @SerializedName("id")
    val id: Int,
    
    @SerializedName("name")
    val name: String,
    
    @SerializedName("image")
    val image: String?,
    
    @SerializedName("temperature")
    val temperature: TemperatureDto?
)

data class TemperatureDto(
    @SerializedName("number")
    val number: Double,
    
    @SerializedName("unit")
    val unit: String
)

data class NutritionDto(
    @SerializedName("nutrients")
    val nutrients: List<NutrientDto>?,
    
    @SerializedName("properties")
    val properties: List<PropertyDto>?,
    
    @SerializedName("flavonoids")
    val flavonoids: List<FlavonoidDto>?,
    
    @SerializedName("caloricBreakdown")
    val caloricBreakdown: CaloricBreakdownDto?,
    
    @SerializedName("weightPerServing")
    val weightPerServing: WeightPerServingDto?
)

data class NutrientDto(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("unit")
    val unit: String,
    
    @SerializedName("percentOfDailyNeeds")
    val percentOfDailyNeeds: Double?
)

data class PropertyDto(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("unit")
    val unit: String
)

data class FlavonoidDto(
    @SerializedName("name")
    val name: String,
    
    @SerializedName("amount")
    val amount: Double,
    
    @SerializedName("unit")
    val unit: String
)

data class CaloricBreakdownDto(
    @SerializedName("percentProtein")
    val percentProtein: Double,
    
    @SerializedName("percentFat")
    val percentFat: Double,
    
    @SerializedName("percentCarbs")
    val percentCarbs: Double
)

data class WeightPerServingDto(
    @SerializedName("amount")
    val amount: Int,
    
    @SerializedName("unit")
    val unit: String
)

data class RecipeSearchResponseDto(
    @SerializedName("recipes")
    val recipes: List<RecipeDto>,
    
    @SerializedName("totalResults")
    val totalResults: Int,
    
    @SerializedName("offset")
    val offset: Int,
    
    @SerializedName("number")
    val number: Int
)