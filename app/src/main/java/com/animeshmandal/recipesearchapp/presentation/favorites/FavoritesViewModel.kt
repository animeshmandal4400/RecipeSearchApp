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
    
    fun removeFromFavorites(recipeId: Int) {
        viewModelScope.launch {
            toggleFavoriteUseCase(recipeId)
            // The flow will automatically update the UI
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


