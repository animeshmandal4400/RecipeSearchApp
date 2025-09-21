package com.animeshmandal.recipesearchapp.presentation.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.domain.usecase.GetAllRecipesUseCase
import com.animeshmandal.recipesearchapp.domain.usecase.GetPopularRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject
import com.animeshmandal.recipesearchapp.core.util.Result
import com.animeshmandal.recipesearchapp.domain.repository.RecipeRepository

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularRecipesUseCase: GetPopularRecipesUseCase,
    private val getAllRecipesUseCase: GetAllRecipesUseCase,
    private val recipeRepository: RecipeRepository

) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            loadCachedRecipes()
            observeRecipes()
            refreshRecipes()
        }
    }


    private fun observeRecipes() {
        viewModelScope.launch {
            println("🏠 HomeViewModel: Loading popular recipes...")
            _uiState.value = _uiState.value.copy(isLoadingPopular = true)
            when (val result = getPopularRecipesUseCase()) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    println("🏠 HomeViewModel: Popular recipes loaded successfully: ${result.data.size} recipes")
                    println("🏠 HomeViewModel: Recipe titles: ${result.data.map { it.title }}")
                    _uiState.value = _uiState.value.copy(
                        popularRecipes = result.data,
                        isLoadingPopular = false
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Error -> {
                    println("🏠 HomeViewModel: Error loading popular recipes: ${result.exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoadingPopular = false,
                        error = result.exception.message
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Loading -> {
                    println("🏠 HomeViewModel: Loading popular recipes...")
                    _uiState.value = _uiState.value.copy(isLoadingPopular = true)
                }
            }
        }
        
        viewModelScope.launch {
            println("🏠 HomeViewModel: Loading all recipes...")
            _uiState.value = _uiState.value.copy(isLoadingAll = true)
            when (val result = getAllRecipesUseCase()) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    println("🏠 HomeViewModel: All recipes loaded successfully: ${result.data.size} recipes")
                    println("🏠 HomeViewModel: All recipe titles: ${result.data.map { it.title }}")
                    _uiState.value = _uiState.value.copy(
                        allRecipes = result.data,
                        isLoadingAll = false
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Error -> {
                    println("🏠 HomeViewModel: Error loading all recipes: ${result.exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoadingAll = false,
                        error = result.exception.message
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Loading -> {
                    println("🏠 HomeViewModel: Loading all recipes...")
                    _uiState.value = _uiState.value.copy(isLoadingAll = true)
                }
            }
        }
    }

    private suspend fun loadCachedRecipes() {
        try {
            val cachedPopular = recipeRepository.getPopularRecipesFlow().firstOrNull()
            if (!cachedPopular.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(popularRecipes = cachedPopular)
            }

            val cachedAll = recipeRepository.getAllRecipesFlow().firstOrNull()
            if (!cachedAll.isNullOrEmpty()) {
                _uiState.value = _uiState.value.copy(allRecipes = cachedAll)
            }
        } catch (e: Exception) {
            println("🏠 HomeViewModel: Error loading cached recipes: ${e.message}")
        }
    }


    private suspend fun refreshRecipes() {
        try {
            _uiState.value = _uiState.value.copy(isLoadingPopular = true, isLoadingAll = true)

            // Popular Recipes API fetch
            when (val popularResponse = recipeRepository.getPopularRecipes()) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    popularRecipes = popularResponse.data,
                    isLoadingPopular = false
                )
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoadingPopular = false,
                    error = popularResponse.exception.message
                )
                is Result.Loading -> _uiState.value = _uiState.value.copy(isLoadingPopular = true)
            }

            // All Recipes API fetch
            when (val allResponse = recipeRepository.getAllRecipes()) {
                is Result.Success -> _uiState.value = _uiState.value.copy(
                    allRecipes = allResponse.data,
                    isLoadingAll = false
                )
                is Result.Error -> _uiState.value = _uiState.value.copy(
                    isLoadingAll = false,
                    error = allResponse.exception.message
                )
                is Result.Loading -> _uiState.value = _uiState.value.copy(isLoadingAll = true)
            }

        } catch (e: Exception) {
            println("🏠 HomeViewModel: Failed to refresh recipes: ${e.message}")
        }
    }

    fun loadRecipes() {
        viewModelScope.launch {
            println("🏠 HomeViewModel: loadRecipes() called")
            getPopularRecipesUseCase()
            getAllRecipesUseCase()
        }
    }
    
    fun loadMoreRecipes() {
        // Implement pagination logic here
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class HomeUiState(
    val userName: String = "User",
    val popularRecipes: List<Recipe> = emptyList(),
    val allRecipes: List<Recipe> = emptyList(),
    val isLoadingPopular: Boolean = false,
    val isLoadingAll: Boolean = false,
    val error: String? = null
)
