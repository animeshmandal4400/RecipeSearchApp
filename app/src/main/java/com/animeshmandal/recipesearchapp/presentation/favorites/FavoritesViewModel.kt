package com.animeshmandal.recipesearchapp.presentation.favorites

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.domain.usecase.GetFavoriteRecipesUseCase
import com.animeshmandal.recipesearchapp.domain.usecase.ToggleFavoriteUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class FavoritesViewModel @Inject constructor(
    private val getFavoriteRecipesUseCase: GetFavoriteRecipesUseCase,
    private val toggleFavoriteUseCase: ToggleFavoriteUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(FavoritesUiState())
    val uiState: StateFlow<FavoritesUiState> = _uiState.asStateFlow()

    init {
        // Load favorites when ViewModel is created
        loadFavorites()
    }

    fun loadFavorites() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            getFavoriteRecipesUseCase().collect { recipes ->
                _uiState.value = _uiState.value.copy(
                    favoriteRecipes = recipes,
                    isLoading = false
                )
            }
        }
    }

    /** ✅ Helper to check if recipe is in favorites */
    fun isFavorite(recipeId: Int): Boolean {
        return _uiState.value.favoriteRecipes.any { it.id == recipeId }
    }

    /** ✅ Toggle favorite (add/remove) */
    fun toggleFavorite(recipeId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
            // UI updates automatically via Flow
        }
    }

    /** Optional: explicit remove */
    fun removeFromFavorites(recipeId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
        }
    }

    fun clearError() {
        _uiState.value = _uiState.value.copy(error = null)
    }
}

data class FavoritesUiState(
    val favoriteRecipes: List<Recipe> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)
