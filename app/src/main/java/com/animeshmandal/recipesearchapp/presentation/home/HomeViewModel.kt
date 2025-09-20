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

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val getPopularRecipesUseCase: GetPopularRecipesUseCase,
    private val getAllRecipesUseCase: GetAllRecipesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()
    
    init {
        observeRecipes()
    }
    
    private fun observeRecipes() {
        viewModelScope.launch {
            println("🏠 HomeViewModel: Loading popular recipes...")
            _uiState.value = _uiState.value.copy(isLoadingPopular = true)
            when (val result = getPopularRecipesUseCase()) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    println("🏠 HomeViewModel: Popular recipes loaded successfully: ${result.data.size} recipes")
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
    
    fun loadRecipes() {
        viewModelScope.launch {
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
