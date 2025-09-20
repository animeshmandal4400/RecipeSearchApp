package com.animeshmandal.recipesearchapp.data.repository

import com.animeshmandal.recipesearchapp.core.util.Result
import com.animeshmandal.recipesearchapp.data.api.SpoonacularApi
import com.animeshmandal.recipesearchapp.data.database.dao.RecipeDao
import com.animeshmandal.recipesearchapp.data.database.RecipeEntity
import com.animeshmandal.recipesearchapp.data.mapper.RecipeMapper
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.domain.repository.RecipeRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject

class RecipeRepositoryImpl @Inject constructor(
    private val api: SpoonacularApi,
    private val recipeDao: RecipeDao,
    private val mapper: RecipeMapper
) : RecipeRepository {
    
    override suspend fun getPopularRecipes(): Result<List<Recipe>> {
        return try {
            println("📱 Repository: Getting popular recipes...")
            
            // First try to get from cache
            val cachedRecipes = recipeDao.getPopularRecipes(10)
            val cachedList = mutableListOf<RecipeEntity>()
            cachedRecipes.collect { cachedList.addAll(it) }
            
            if (cachedList.isNotEmpty()) {
                println("📱 Repository: Found ${cachedList.size} cached recipes")
                return Result.Success(cachedList.map { mapper.mapToDomain(it) })
            }
            
            println("📱 Repository: No cache found, fetching from API...")
            // If no cache, fetch from API
            val response = api.getRandomRecipes(10)
            println("📱 Repository: API response code: ${response.code()}")
            println("📱 Repository: API response body: ${response.body()}")
            
            if (response.isSuccessful && response.body() != null) {
                val recipes = response.body()!!.recipes
                println("📱 Repository: Received ${recipes.size} recipes from API")
                val recipeEntities = recipes.map { mapper.mapToEntity(it) }
                recipeDao.insertRecipes(recipeEntities)
                println("📱 Repository: Inserted ${recipeEntities.size} recipes to database")
                Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
            } else {
                println("📱 Repository: API call failed - ${response.code()}: ${response.message()}")
                Result.Error(Exception("Failed to fetch recipes from API: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            println("📱 Repository: Exception occurred: ${e.message}")
            e.printStackTrace()
            Result.Error(e)
        }
    }
    
    override fun getPopularRecipesFlow(): Flow<List<Recipe>> {
        return recipeDao.getPopularRecipes(10).map { recipeEntities ->
            recipeEntities.map { mapper.mapToDomain(it) }
        }
    }
    
    override suspend fun getAllRecipes(): Result<List<Recipe>> {
        return try {
            val cachedRecipes = recipeDao.getRecentRecipes(20)
            val cachedList = mutableListOf<RecipeEntity>()
            cachedRecipes.collect { cachedList.addAll(it) }
            
            if (cachedList.isNotEmpty()) {
                return Result.Success(cachedList.map { mapper.mapToDomain(it) })
            }
            
            // Fetch from API if no cache
            val response = api.getRandomRecipes(20)
            if (response.isSuccessful && response.body() != null) {
                val recipes = response.body()!!.recipes
                val recipeEntities = recipes.map { mapper.mapToEntity(it) }
                recipeDao.insertRecipes(recipeEntities)
                Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
            } else {
                Result.Error(Exception("Failed to fetch recipes from API"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getAllRecipesFlow(): Flow<List<Recipe>> {
        return recipeDao.getRecentRecipes(20).map { recipeEntities ->
            recipeEntities.map { mapper.mapToDomain(it) }
        }
    }
    
    override suspend fun searchRecipes(query: String): Result<List<Recipe>> {
        return try {
            println("🔍 Repository: Searching recipes for query: '$query'")
            
            // First check cache
            val cachedRecipes = recipeDao.searchRecipes(query)
            val cachedList = mutableListOf<RecipeEntity>()
            cachedRecipes.collect { cachedList.addAll(it) }
            
            if (cachedList.isNotEmpty()) {
                println("🔍 Repository: Found ${cachedList.size} cached recipes for query: '$query'")
                return Result.Success(cachedList.map { mapper.mapToDomain(it) })
            }
            
            println("🔍 Repository: No cache found, searching API for: '$query'")
            // Search from API
            val response = api.searchRecipes(query, 20)
            println("🔍 Repository: Search API response code: ${response.code()}")
            println("🔍 Repository: Search API response body: ${response.body()}")
            
            if (response.isSuccessful && response.body() != null) {
                val recipes = response.body()!!.recipes
                println("🔍 Repository: Found ${recipes.size} recipes for query: '$query'")
                val recipeEntities = recipes.map { mapper.mapToEntity(it) }
                recipeDao.insertRecipes(recipeEntities)
                println("🔍 Repository: Inserted ${recipeEntities.size} search results to database")
                Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
            } else {
                println("🔍 Repository: Search API call failed - ${response.code()}: ${response.message()}")
                Result.Error(Exception("Failed to search recipes from API: ${response.code()} ${response.message()}"))
            }
        } catch (e: Exception) {
            println("🔍 Repository: Search exception occurred: ${e.message}")
            e.printStackTrace()
            Result.Error(e)
        }
    }
    
    override fun searchRecipesFlow(query: String): Flow<List<Recipe>> {
        return recipeDao.searchRecipes(query).map { recipeEntities ->
            recipeEntities.map { mapper.mapToDomain(it) }
        }
    }
    
    override suspend fun getRecipeById(id: Int): Result<Recipe> {
        return try {
            val recipeEntity = recipeDao.getRecipeById(id)
            if (recipeEntity != null) {
                Result.Success(mapper.mapToDomain(recipeEntity))
            } else {
                // Fetch from API if not in cache
                val response = api.getRecipeInformation(id)
                if (response.isSuccessful && response.body() != null) {
                    val recipe = response.body()!!
                    val recipeEntity = mapper.mapToEntity(recipe)
                    recipeDao.insertRecipe(recipeEntity)
                    Result.Success(mapper.mapDtoToDomain(recipe))
                } else {
                    Result.Error(Exception("Failed to fetch recipe from API"))
                }
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getRecipeByIdFlow(id: Int): Flow<Recipe?> {
        return recipeDao.getRecipeByIdFlow(id).map { recipeEntity ->
            recipeEntity?.let { mapper.mapToDomain(it) }
        }
    }
    
    override suspend fun getSimilarRecipes(id: Int): Result<List<Recipe>> {
        return try {
            val response = api.getSimilarRecipes(id)
            if (response.isSuccessful && response.body() != null) {
                val recipes = response.body()!!
                Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
            } else {
                Result.Error(Exception("Failed to fetch similar recipes from API"))
            }
        } catch (e: Exception) {
            Result.Error(e)
        }
    }
    
    override fun getFavoriteRecipes(): Flow<List<Recipe>> {
        return recipeDao.getFavoriteRecipes().map { recipeEntities ->
            recipeEntities.map { mapper.mapToDomain(it) }
        }
    }
    
    override suspend fun addToFavorites(recipeId: Int) {
        recipeDao.updateFavoriteStatus(recipeId, true)
    }
    
    override suspend fun removeFromFavorites(recipeId: Int) {
        recipeDao.updateFavoriteStatus(recipeId, false)
    }
    
    override suspend fun isFavorite(recipeId: Int): Boolean {
        val recipe = recipeDao.getRecipeById(recipeId)
        return recipe?.isFavorite ?: false
    }
    
    override suspend fun refreshRecipes() {
        // This would be called by the data source to refresh from API
        // Implementation would depend on the API data source
    }
    
    override suspend fun clearCache() {
        recipeDao.deleteOldNonFavoriteRecipes(System.currentTimeMillis() - (24 * 60 * 60 * 1000))
    }
}