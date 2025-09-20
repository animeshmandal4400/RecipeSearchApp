package com.animeshmandal.recipesearchapp.domain.repository

import com.animeshmandal.recipesearchapp.core.util.Result
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import kotlinx.coroutines.flow.Flow

interface RecipeRepository {
    
    // Popular recipes
    suspend fun getPopularRecipes(): Result<List<Recipe>>
    fun getPopularRecipesFlow(): Flow<List<Recipe>>
    
    // All recipes
    suspend fun getAllRecipes(): Result<List<Recipe>>
    fun getAllRecipesFlow(): Flow<List<Recipe>>
    
    // Search recipes
    suspend fun searchRecipes(query: String): Result<List<Recipe>>
    fun searchRecipesFlow(query: String): Flow<List<Recipe>>
    
    // Recipe details
    suspend fun getRecipeById(id: Int): Result<Recipe>
    fun getRecipeByIdFlow(id: Int): Flow<Recipe?>
    
    // Similar recipes
    suspend fun getSimilarRecipes(id: Int): Result<List<Recipe>>
    
    // Favorites
    fun getFavoriteRecipes(): Flow<List<Recipe>>
    suspend fun addToFavorites(recipeId: Int)
    suspend fun removeFromFavorites(recipeId: Int)
    suspend fun isFavorite(recipeId: Int): Boolean
    
    // Cache management
    suspend fun refreshRecipes()
    suspend fun clearCache()
}

