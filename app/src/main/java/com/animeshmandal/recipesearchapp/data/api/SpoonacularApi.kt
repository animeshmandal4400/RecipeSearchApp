package com.animeshmandal.recipesearchapp.data.api

import com.animeshmandal.recipesearchapp.data.model.RecipeDto
import com.animeshmandal.recipesearchapp.data.model.RecipeSearchResponseDto
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SpoonacularApi {
    
    @GET("recipes/random")
    suspend fun getRandomRecipes(
        @Query("number") number: Int = 10,
        @Query("tags") tags: String? = null
    ): Response<RecipeSearchResponseDto>
    
    @GET("recipes/complexSearch")
    suspend fun searchRecipes(
        @Query("query") query: String,
        @Query("number") number: Int = 20,
        @Query("offset") offset: Int = 0,
        @Query("diet") diet: String? = null,
        @Query("cuisine") cuisine: String? = null,
        @Query("type") type: String? = null
    ): Response<RecipeSearchResponseDto>
    
    @GET("recipes/{id}/information")
    suspend fun getRecipeInformation(
        @Path("id") id: Int,
        @Query("includeNutrition") includeNutrition: Boolean = true
    ): Response<RecipeDto>
    
    @GET("recipes/{id}/similar")
    suspend fun getSimilarRecipes(
        @Path("id") id: Int,
        @Query("number") number: Int = 5
    ): Response<List<RecipeDto>>
}


