package com.animeshmandal.recipesearchapp.data.database

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface RecipeDao {
    
    @Query("SELECT * FROM recipes WHERE isFavorite = 1 ORDER BY cachedAt DESC")
    fun getFavoriteRecipes(): Flow<List<RecipeEntity>>
    
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    suspend fun getRecipeById(recipeId: Int): RecipeEntity?
    
    @Query("SELECT * FROM recipes WHERE id = :recipeId")
    fun getRecipeByIdFlow(recipeId: Int): Flow<RecipeEntity?>
    
    @Query("SELECT * FROM recipes ORDER BY cachedAt DESC LIMIT :limit")
    fun getRecentRecipes(limit: Int = 20): Flow<List<RecipeEntity>>
    
    @Query("SELECT * FROM recipes WHERE title LIKE '%' || :query || '%' ORDER BY cachedAt DESC")
    fun searchRecipes(query: String): Flow<List<RecipeEntity>>
    
    @Query("SELECT * FROM recipes WHERE veryPopular = 1 ORDER BY aggregateLikes DESC LIMIT :limit")
    fun getPopularRecipes(limit: Int = 10): Flow<List<RecipeEntity>>
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipe(recipe: RecipeEntity)
    
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecipes(recipes: List<RecipeEntity>)
    
    @Update
    suspend fun updateRecipe(recipe: RecipeEntity)
    
    @Query("UPDATE recipes SET isFavorite = :isFavorite WHERE id = :recipeId")
    suspend fun updateFavoriteStatus(recipeId: Int, isFavorite: Boolean)
    
    @Delete
    suspend fun deleteRecipe(recipe: RecipeEntity)
    
    @Query("DELETE FROM recipes WHERE id = :recipeId")
    suspend fun deleteRecipeById(recipeId: Int)
    
    @Query("DELETE FROM recipes WHERE isFavorite = 0 AND cachedAt < :timestamp")
    suspend fun deleteOldNonFavoriteRecipes(timestamp: Long)
    
    @Query("SELECT COUNT(*) FROM recipes WHERE isFavorite = 1")
    suspend fun getFavoriteCount(): Int
}


