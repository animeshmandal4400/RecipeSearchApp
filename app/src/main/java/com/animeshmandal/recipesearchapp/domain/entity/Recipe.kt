package com.animeshmandal.recipesearchapp.domain.entity

data class Recipe(
    val id: Int,
    val title: String,
    val image: String?,
    val readyInMinutes: Int,
    val servings: Int,
    val pricePerServing: Double,
    val summary: String,
    val instructions: String?,
    val extendedIngredients: List<Ingredient>?,
    val equipment: List<Equipment>?,
    val nutrition: Nutrition?,
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
    val isFavorite: Boolean = false
)

data class Ingredient(
    val id: Int,
    val name: String,
    val original: String,
    val originalName: String,
    val amount: Double,
    val unit: String,
    val image: String?,
    val consistency: String?,
    val aisle: String?
)

data class Equipment(
    val id: Int,
    val name: String,
    val image: String?,
    val temperature: Double?,
    val temperatureUnit: String?
)

data class Nutrition(
    val nutrients: List<Nutrient>?,
    val properties: List<Property>?,
    val flavonoids: List<Flavonoid>?,
    val caloricBreakdown: CaloricBreakdown?,
    val weightPerServing: WeightPerServing?
)

data class Nutrient(
    val name: String,
    val amount: Double,
    val unit: String,
    val percentOfDailyNeeds: Double?
)

data class Property(
    val name: String,
    val amount: Double,
    val unit: String
)

data class Flavonoid(
    val name: String,
    val amount: Double,
    val unit: String
)

data class CaloricBreakdown(
    val percentProtein: Double,
    val percentFat: Double,
    val percentCarbs: Double
)

data class WeightPerServing(
    val amount: Int,
    val unit: String
)

