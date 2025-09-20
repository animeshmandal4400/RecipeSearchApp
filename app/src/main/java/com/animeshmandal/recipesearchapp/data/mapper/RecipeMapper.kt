package com.animeshmandal.recipesearchapp.data.mapper

import com.animeshmandal.recipesearchapp.data.database.*
import com.animeshmandal.recipesearchapp.data.model.*
import com.animeshmandal.recipesearchapp.domain.entity.*
import javax.inject.Inject

class RecipeMapper @Inject constructor() {
    
    fun mapDtoToDomain(dto: RecipeDto): Recipe {
        return Recipe(
            id = dto.id,
            title = dto.title,
            image = dto.image,
            readyInMinutes = dto.readyInMinutes,
            servings = dto.servings,
            pricePerServing = dto.pricePerServing,
            summary = dto.summary,
            instructions = dto.instructions,
            extendedIngredients = dto.extendedIngredients?.map { mapIngredientDtoToDomain(it) },
            equipment = dto.equipment?.map { mapEquipmentDtoToDomain(it) },
            nutrition = dto.nutrition?.let { mapNutritionDtoToDomain(it) },
            cuisines = dto.cuisines,
            dishTypes = dto.dishTypes,
            diets = dto.diets,
            veryHealthy = dto.veryHealthy,
            cheap = dto.cheap,
            veryPopular = dto.veryPopular,
            sustainable = dto.sustainable,
            weightWatcherSmartPoints = dto.weightWatcherSmartPoints,
            gaps = dto.gaps,
            lowFodmap = dto.lowFodmap,
            aggregateLikes = dto.aggregateLikes,
            spoonacularScore = dto.spoonacularScore,
            healthScore = dto.healthScore,
            creditsText = dto.creditsText,
            license = dto.license,
            sourceName = dto.sourceName,
            sourceUrl = dto.sourceUrl,
            isFavorite = false
        )
    }
    
    fun mapToDomain(entity: RecipeEntity): Recipe {
        return Recipe(
            id = entity.id,
            title = entity.title,
            image = entity.image,
            readyInMinutes = entity.readyInMinutes,
            servings = entity.servings,
            pricePerServing = entity.pricePerServing,
            summary = entity.summary,
            instructions = entity.instructions,
            extendedIngredients = entity.extendedIngredients?.map { mapIngredientEntityToDomain(it) },
            equipment = entity.equipment?.map { mapEquipmentEntityToDomain(it) },
            nutrition = entity.nutrition?.let { mapNutritionEntityToDomain(it) },
            cuisines = entity.cuisines,
            dishTypes = entity.dishTypes,
            diets = entity.diets,
            veryHealthy = entity.veryHealthy,
            cheap = entity.cheap,
            veryPopular = entity.veryPopular,
            sustainable = entity.sustainable,
            weightWatcherSmartPoints = entity.weightWatcherSmartPoints,
            gaps = entity.gaps,
            lowFodmap = entity.lowFodmap,
            aggregateLikes = entity.aggregateLikes,
            spoonacularScore = entity.spoonacularScore,
            healthScore = entity.healthScore,
            creditsText = entity.creditsText,
            license = entity.license,
            sourceName = entity.sourceName,
            sourceUrl = entity.sourceUrl,
            isFavorite = entity.isFavorite
        )
    }
    
    fun mapToEntity(dto: RecipeDto): RecipeEntity {
        return RecipeEntity(
            id = dto.id,
            title = dto.title,
            image = dto.image,
            readyInMinutes = dto.readyInMinutes,
            servings = dto.servings,
            pricePerServing = dto.pricePerServing,
            summary = dto.summary,
            instructions = dto.instructions,
            cuisines = dto.cuisines,
            dishTypes = dto.dishTypes,
            diets = dto.diets,
            veryHealthy = dto.veryHealthy,
            cheap = dto.cheap,
            veryPopular = dto.veryPopular,
            sustainable = dto.sustainable,
            weightWatcherSmartPoints = dto.weightWatcherSmartPoints,
            gaps = dto.gaps,
            lowFodmap = dto.lowFodmap,
            aggregateLikes = dto.aggregateLikes,
            spoonacularScore = dto.spoonacularScore,
            healthScore = dto.healthScore,
            creditsText = dto.creditsText,
            license = dto.license,
            sourceName = dto.sourceName,
            sourceUrl = dto.sourceUrl,
            extendedIngredients = dto.extendedIngredients?.map { mapIngredientDtoToEntity(it, dto.id) },
            equipment = dto.equipment?.map { mapEquipmentDtoToEntity(it, dto.id) },
            nutrition = dto.nutrition?.let { mapNutritionDtoToEntity(it, dto.id) },
            isFavorite = false
        )
    }
    
    private fun mapIngredientDtoToDomain(dto: ExtendedIngredientDto): Ingredient {
        return Ingredient(
            id = dto.id,
            name = dto.name,
            original = dto.original,
            originalName = dto.originalName,
            amount = dto.amount,
            unit = dto.unit,
            image = dto.image,
            consistency = dto.consistency,
            aisle = dto.aisle
        )
    }
    
    private fun mapIngredientEntityToDomain(entity: IngredientEntity): Ingredient {
        return Ingredient(
            id = entity.id,
            name = entity.name,
            original = entity.original,
            originalName = entity.originalName,
            amount = entity.amount,
            unit = entity.unit,
            image = entity.image,
            consistency = entity.consistency,
            aisle = entity.aisle
        )
    }
    
    private fun mapIngredientDtoToEntity(dto: ExtendedIngredientDto, recipeId: Int): IngredientEntity {
        return IngredientEntity(
            id = dto.id,
            recipeId = recipeId,
            name = dto.name,
            original = dto.original,
            originalName = dto.originalName,
            amount = dto.amount,
            unit = dto.unit,
            image = dto.image,
            consistency = dto.consistency,
            aisle = dto.aisle
        )
    }
    
    private fun mapEquipmentDtoToDomain(dto: EquipmentDto): Equipment {
        return Equipment(
            id = dto.id,
            name = dto.name,
            image = dto.image,
            temperature = dto.temperature?.number,
            temperatureUnit = dto.temperature?.unit
        )
    }
    
    private fun mapEquipmentEntityToDomain(entity: EquipmentEntity): Equipment {
        return Equipment(
            id = entity.id,
            name = entity.name,
            image = entity.image,
            temperature = entity.temperature,
            temperatureUnit = entity.temperatureUnit
        )
    }
    
    private fun mapEquipmentDtoToEntity(dto: EquipmentDto, recipeId: Int): EquipmentEntity {
        return EquipmentEntity(
            id = dto.id,
            recipeId = recipeId,
            name = dto.name,
            image = dto.image,
            temperature = dto.temperature?.number,
            temperatureUnit = dto.temperature?.unit
        )
    }
    
    private fun mapNutritionDtoToDomain(dto: NutritionDto): Nutrition {
        return Nutrition(
            nutrients = dto.nutrients?.map { mapNutrientDtoToDomain(it) },
            properties = dto.properties?.map { mapPropertyDtoToDomain(it) },
            flavonoids = dto.flavonoids?.map { mapFlavonoidDtoToDomain(it) },
            caloricBreakdown = dto.caloricBreakdown?.let { mapCaloricBreakdownDtoToDomain(it) },
            weightPerServing = dto.weightPerServing?.let { mapWeightPerServingDtoToDomain(it) }
        )
    }
    
    private fun mapNutritionEntityToDomain(entity: NutritionEntity): Nutrition {
        return Nutrition(
            nutrients = entity.nutrients?.map { mapNutrientEntityToDomain(it) },
            properties = entity.properties?.map { mapPropertyEntityToDomain(it) },
            flavonoids = entity.flavonoids?.map { mapFlavonoidEntityToDomain(it) },
            caloricBreakdown = entity.caloricBreakdown?.let { mapCaloricBreakdownEntityToDomain(it) },
            weightPerServing = entity.weightPerServing?.let { mapWeightPerServingEntityToDomain(it) }
        )
    }
    
    private fun mapNutritionDtoToEntity(dto: NutritionDto, recipeId: Int): NutritionEntity {
        return NutritionEntity(
            recipeId = recipeId,
            nutrients = dto.nutrients?.map { mapNutrientDtoToEntity(it, recipeId) },
            properties = dto.properties?.map { mapPropertyDtoToEntity(it, recipeId) },
            flavonoids = dto.flavonoids?.map { mapFlavonoidDtoToEntity(it, recipeId) },
            caloricBreakdown = dto.caloricBreakdown?.let { mapCaloricBreakdownDtoToEntity(it, recipeId) },
            weightPerServing = dto.weightPerServing?.let { mapWeightPerServingDtoToEntity(it, recipeId) }
        )
    }
    
    private fun mapNutrientDtoToDomain(dto: NutrientDto): Nutrient {
        return Nutrient(
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit,
            percentOfDailyNeeds = dto.percentOfDailyNeeds
        )
    }
    
    private fun mapNutrientEntityToDomain(entity: NutrientEntity): Nutrient {
        return Nutrient(
            name = entity.name,
            amount = entity.amount,
            unit = entity.unit,
            percentOfDailyNeeds = entity.percentOfDailyNeeds
        )
    }
    
    private fun mapNutrientDtoToEntity(dto: NutrientDto, recipeId: Int): NutrientEntity {
        return NutrientEntity(
            id = "${dto.name}_$recipeId",
            recipeId = recipeId,
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit,
            percentOfDailyNeeds = dto.percentOfDailyNeeds
        )
    }
    
    private fun mapPropertyDtoToDomain(dto: PropertyDto): Property {
        return Property(
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit
        )
    }
    
    private fun mapPropertyEntityToDomain(entity: PropertyEntity): Property {
        return Property(
            name = entity.name,
            amount = entity.amount,
            unit = entity.unit
        )
    }
    
    private fun mapPropertyDtoToEntity(dto: PropertyDto, recipeId: Int): PropertyEntity {
        return PropertyEntity(
            id = "${dto.name}_$recipeId",
            recipeId = recipeId,
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit
        )
    }
    
    private fun mapFlavonoidDtoToDomain(dto: FlavonoidDto): Flavonoid {
        return Flavonoid(
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit
        )
    }
    
    private fun mapFlavonoidEntityToDomain(entity: FlavonoidEntity): Flavonoid {
        return Flavonoid(
            name = entity.name,
            amount = entity.amount,
            unit = entity.unit
        )
    }
    
    private fun mapFlavonoidDtoToEntity(dto: FlavonoidDto, recipeId: Int): FlavonoidEntity {
        return FlavonoidEntity(
            id = "${dto.name}_$recipeId",
            recipeId = recipeId,
            name = dto.name,
            amount = dto.amount,
            unit = dto.unit
        )
    }
    
    private fun mapCaloricBreakdownDtoToDomain(dto: CaloricBreakdownDto): CaloricBreakdown {
        return CaloricBreakdown(
            percentProtein = dto.percentProtein,
            percentFat = dto.percentFat,
            percentCarbs = dto.percentCarbs
        )
    }
    
    private fun mapCaloricBreakdownEntityToDomain(entity: CaloricBreakdownEntity): CaloricBreakdown {
        return CaloricBreakdown(
            percentProtein = entity.percentProtein,
            percentFat = entity.percentFat,
            percentCarbs = entity.percentCarbs
        )
    }
    
    private fun mapCaloricBreakdownDtoToEntity(dto: CaloricBreakdownDto, recipeId: Int): CaloricBreakdownEntity {
        return CaloricBreakdownEntity(
            recipeId = recipeId,
            percentProtein = dto.percentProtein,
            percentFat = dto.percentFat,
            percentCarbs = dto.percentCarbs
        )
    }
    
    private fun mapWeightPerServingDtoToDomain(dto: WeightPerServingDto): WeightPerServing {
        return WeightPerServing(
            amount = dto.amount,
            unit = dto.unit
        )
    }
    
    private fun mapWeightPerServingEntityToDomain(entity: WeightPerServingEntity): WeightPerServing {
        return WeightPerServing(
            amount = entity.amount,
            unit = entity.unit
        )
    }
    
    private fun mapWeightPerServingDtoToEntity(dto: WeightPerServingDto, recipeId: Int): WeightPerServingEntity {
        return WeightPerServingEntity(
            recipeId = recipeId,
            amount = dto.amount,
            unit = dto.unit
        )
    }
}


