package com.animeshmandal.recipesearchapp.presentation.recipedetail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.domain.usecase.GetRecipeByIdUseCase
import com.animeshmandal.recipesearchapp.domain.usecase.GetSimilarRecipesUseCase
import com.animeshmandal.recipesearchapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class RecipeDetailViewModel @Inject constructor(
    private val getRecipeByIdUseCase: GetRecipeByIdUseCase,
    private val getSimilarRecipesUseCase: GetSimilarRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(RecipeDetailUiState())
    val uiState: StateFlow<RecipeDetailUiState> = _uiState.asStateFlow()
    
    fun loadRecipe(recipeId: Int) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            when (val result = getRecipeByIdUseCase(recipeId)) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        recipe = result.data,
                        isLoading = false,
                        isFavorite = result.data.isFavorite
                    )
                    loadSimilarRecipes(recipeId)
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    private fun loadSimilarRecipes(recipeId: Int) {
        viewModelScope.launch {
            when (val result = getSimilarRecipesUseCase(recipeId)) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    _uiState.value = _uiState.value.copy(similarRecipes = result.data)
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Error -> {
                    // Don't show error for similar recipes, just log it
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Loading -> {
                    // Loading state for similar recipes
                }
            }
        }
    }
    
    fun toggleFavorite() {
        val currentRecipe = _uiState.value.recipe
        if (currentRecipe != null) {
            viewModelScope.launch {
                toggleFavoriteUseCase(currentRecipe.id)
                _uiState.value = _uiState.value.copy(
                    isFavorite = !_uiState.value.isFavorite
                )
            }
        }
    }
    
    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class RecipeDetailUiState(
    val recipe: Recipe? = null,
    val similarRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val isFavorite: Boolean = false,
    val error: String? = null
)
