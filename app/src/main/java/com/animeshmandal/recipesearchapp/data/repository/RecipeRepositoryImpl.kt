package com.animeshmandal.recipesearchapp.data.repository

import com.animeshmandal.recipesearchapp.core.util.Result
import com.animeshmandal.recipesearchapp.data.api.SpoonacularApi
import com.animeshmandal.recipesearchapp.data.database.RecipeDao
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
            // First try to get from cache
            val cachedRecipes = recipeDao.getPopularRecipes(10)
            val cachedList = mutableListOf<RecipeEntity>()
            cachedRecipes.collect { cachedList.addAll(it) }
            
            if (cachedList.isNotEmpty()) {
                return Result.Success(cachedList.map { mapper.mapToDomain(it) })
            }
            
            // If no cache, fetch from API
            val response = api.getRandomRecipes(10)
            val recipes = response.recipes
            val recipeEntities = recipes.map { mapper.mapToEntity(it) }
            recipeDao.insertRecipes(recipeEntities)
            Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
        } catch (e: Exception) {
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
            val recipes = response.recipes
            val recipeEntities = recipes.map { mapper.mapToEntity(it) }
            recipeDao.insertRecipes(recipeEntities)
            Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
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
            // First check cache
            val cachedRecipes = recipeDao.searchRecipes(query)
            val cachedList = mutableListOf<RecipeEntity>()
            cachedRecipes.collect { cachedList.addAll(it) }
            
            if (cachedList.isNotEmpty()) {
                return Result.Success(cachedList.map { mapper.mapToDomain(it) })
            }
            
            // Search from API
            val response = api.searchRecipes(query, 20)
            val recipes = response.recipes
            val recipeEntities = recipes.map { mapper.mapToEntity(it) }
            recipeDao.insertRecipes(recipeEntities)
            Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
        } catch (e: Exception) {
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
                val recipe = api.getRecipeInformation(id)
                val recipeEntity = mapper.mapToEntity(recipe)
                recipeDao.insertRecipe(recipeEntity)
                Result.Success(mapper.mapDtoToDomain(recipe))
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
            val recipes = api.getSimilarRecipes(id)
            Result.Success(recipes.map { mapper.mapDtoToDomain(it) })
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

/*

> Task :app:compileDebugKotlin FAILED
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:21:16 New inference error [NewConstraintError at Incorporate TypeVariable(K) == com/animeshmandal/recipesearchapp/core/util/Result<kotlin/Nothing> from Fix variable K from position Fix variable K: kotlinx/coroutines/flow/Flow<TypeVariable(R)> <!: kotlin/Nothing].
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:33:36 Unresolved reference 'recipes'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:34:42 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:34:46 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:35:37 Argument type mismatch: actual type is 'kotlinx.coroutines.flow.Flow<ERROR CLASS: Cannot infer argument for type parameter R>', but 'kotlin.collections.List<com.animeshmandal.recipesearchapp.data.database.RecipeEntity>' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:36:36 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:36:40 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:49:16 New inference error [NewConstraintError at Incorporate TypeVariable(K) == com/animeshmandal/recipesearchapp/core/util/Result<kotlin/Nothing> from Fix variable K from position Fix variable K: kotlinx/coroutines/flow/Flow<TypeVariable(R)> <!: kotlin/Nothing].
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:60:36 Unresolved reference 'recipes'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:61:42 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:61:46 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:62:37 Argument type mismatch: actual type is 'kotlinx.coroutines.flow.Flow<ERROR CLASS: Cannot infer argument for type parameter R>', but 'kotlin.collections.List<com.animeshmandal.recipesearchapp.data.database.RecipeEntity>' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:63:36 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:63:40 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:76:16 New inference error [NewConstraintError at Incorporate TypeVariable(K) == com/animeshmandal/recipesearchapp/core/util/Result<kotlin/Nothing> from Fix variable K from position Fix variable K: kotlinx/coroutines/flow/Flow<TypeVariable(R)> <!: kotlin/Nothing].
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:88:36 Unresolved reference 'recipes'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:89:42 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:89:46 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:90:37 Argument type mismatch: actual type is 'kotlinx.coroutines.flow.Flow<ERROR CLASS: Cannot infer argument for type parameter R>', but 'kotlin.collections.List<com.animeshmandal.recipesearchapp.data.database.RecipeEntity>' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:91:36 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:91:40 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:111:55 Argument type mismatch: actual type is 'retrofit2.Response<com.animeshmandal.recipesearchapp.data.model.RecipeDto>', but 'com.animeshmandal.recipesearchapp.data.model.RecipeDto' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:113:54 Argument type mismatch: actual type is 'retrofit2.Response<com.animeshmandal.recipesearchapp.data.model.RecipeDto>', but 'com.animeshmandal.recipesearchapp.data.model.RecipeDto' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:129:13 Argument type mismatch: actual type is 'com.animeshmandal.recipesearchapp.core.util.Result.Success<T>', but 'K' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:129:20 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:129:28 Argument type mismatch: actual type is 'kotlinx.coroutines.flow.Flow<R>', but 'T' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:129:36 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:129:36 Unresolved reference. None of the following candidates is applicable because of a receiver type mismatch:
fun <T, R> Flow<T>.map(crossinline transform: suspend (@ParameterName(...) T) -> R): Flow<R>
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:129:40 Cannot infer type for this parameter. Please specify it explicitly.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/data/repository/RecipeRepositoryImpl.kt:131:13 Argument type mismatch: actual type is 'com.animeshmandal.recipesearchapp.core.util.Result.Error', but 'K' was expected.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/di/DatabaseModule.kt:25:16 Return type mismatch: expected 'com.animeshmandal.recipesearchapp.data.database.dao.RecipeDao', actual 'com.animeshmandal.recipesearchapp.data.database.RecipeDao'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/favorites/FavoritesScreen.kt:14:17 Unresolved reference 'hilt'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/favorites/FavoritesScreen.kt:21:37 Unresolved reference 'hiltViewModel'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/favorites/FavoritesScreen.kt:129:22 Unresolved reference 'clip'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/home/HomeScreen.kt:13:17 Unresolved reference 'hilt'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/home/HomeScreen.kt:25:32 Unresolved reference 'hiltViewModel'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/home/components/SearchBar.kt:17:5 None of the following candidates is applicable:
fun OutlinedTextField(value: TextFieldValue, onValueChange: (TextFieldValue) -> Unit, modifier: Modifier = ..., enabled: Boolean = ..., readOnly: Boolean = ..., textStyle: TextStyle = ..., label: @Composable() ComposableFunction0<Unit>? = ..., placeholder: @Composable() ComposableFunction0<Unit>? = ..., leadingIcon: @Composable() ComposableFunction0<Unit>? = ..., trailingIcon: @Composable() ComposableFunction0<Unit>? = ..., prefix: @Composable() ComposableFunction0<Unit>? = ..., suffix: @Composable() ComposableFunction0<Unit>? = ..., supportingText: @Composable() ComposableFunction0<Unit>? = ..., isError: Boolean = ..., visualTransformation: VisualTransformation = ..., keyboardOptions: KeyboardOptions = ..., keyboardActions: KeyboardActions = ..., singleLine: Boolean = ..., maxLines: Int = ..., minLines: Int = ..., interactionSource: MutableInteractionSource? = ..., shape: Shape = ..., colors: TextFieldColors = ...): Unit
fun OutlinedTextField(value: String, onValueChange: (String) -> Unit, modifier: Modifier = ..., enabled: Boolean = ..., readOnly: Boolean = ..., textStyle: TextStyle = ..., label: @Composable() ComposableFunction0<Unit>? = ..., placeholder: @Composable() ComposableFunction0<Unit>? = ..., leadingIcon: @Composable() ComposableFunction0<Unit>? = ..., trailingIcon: @Composable() ComposableFunction0<Unit>? = ..., prefix: @Composable() ComposableFunction0<Unit>? = ..., suffix: @Composable() ComposableFunction0<Unit>? = ..., supportingText: @Composable() ComposableFunction0<Unit>? = ..., isError: Boolean = ..., visualTransformation: VisualTransformation = ..., keyboardOptions: KeyboardOptions = ..., keyboardActions: KeyboardActions = ..., singleLine: Boolean = ..., maxLines: Int = ..., minLines: Int = ..., interactionSource: MutableInteractionSource? = ..., shape: Shape = ..., colors: TextFieldColors = ...): Unit
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/home/components/SearchBar.kt:21:25 @Composable invocations can only happen from the context of a @Composable function
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/home/components/SearchBar.kt:23:13 @Composable invocations can only happen from the context of a @Composable function
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/recipedetail/RecipeDetailScreen.kt:17:17 Unresolved reference 'hilt'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/recipedetail/RecipeDetailScreen.kt:25:40 Unresolved reference 'hiltViewModel'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/search/SearchScreen.kt:15:17 Unresolved reference 'hilt'.
e: file:///Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/src/main/java/com/animeshmandal/recipesearchapp/presentation/search/SearchScreen.kt:24:34 Unresolved reference 'hiltViewModel'.

FAILURE: Build failed with an exception.

* What went wrong:
Execution failed for task ':app:compileDebugKotlin'.
> A failure occurred while executing org.jetbrains.kotlin.compilerRunner.GradleCompilerRunnerWithWorkers$GradleKotlinCompilerWorkAction
> Compilation error. See log for more details
*/
