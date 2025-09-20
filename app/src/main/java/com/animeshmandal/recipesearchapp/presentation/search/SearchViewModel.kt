package com.animeshmandal.recipesearchapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.domain.usecase.SearchRecipesUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val searchRecipesUseCase: SearchRecipesUseCase
) : ViewModel() {
    
    private val _uiState = MutableStateFlow(SearchUiState())
    val uiState: StateFlow<SearchUiState> = _uiState.asStateFlow()
    
    private val _query = MutableStateFlow("")
    
    init {
        observeQuery()
    }
    
    @OptIn(FlowPreview::class)
    private fun observeQuery() {
        viewModelScope.launch {
            _query
                .debounce(500)
                .distinctUntilChanged()
                .collect { query ->
                    _uiState.value = _uiState.value.copy(query = query)
                    
                    if (query.isNotEmpty()) {
                        searchRecipes(query)
                    } else {
                        _uiState.value = _uiState.value.copy(
                            searchResults = emptyList(),
                            suggestions = getDefaultSuggestions()
                        )
                    }
                }
        }
    }
    
    fun updateQuery(query: String) {
        _query.value = query
    }
    
    private fun searchRecipes(query: String) {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)
            
            when (val result = searchRecipesUseCase(query)) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        searchResults = result.data,
                        isLoading = false,
                        suggestions = emptyList()
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message,
                        suggestions = emptyList()
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Loading -> {
                    _uiState.value = _uiState.value.copy(isLoading = true)
                }
            }
        }
    }
    
    private fun getDefaultSuggestions(): List<String> {
        return listOf(
            "Shahi paneer",
            "Paneer butter masala",
            "Chicken curry",
            "Biryani",
            "Pasta",
            "Pizza",
            "Salad",
            "Soup"
        )
    }
}

data class SearchUiState(
    val query: String = "",
    val searchResults: List<Recipe> = emptyList(),
    val suggestions: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)


