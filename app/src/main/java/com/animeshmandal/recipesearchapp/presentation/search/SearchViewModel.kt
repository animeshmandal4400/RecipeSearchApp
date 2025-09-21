package com.animeshmandal.recipesearchapp.presentation.search

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
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
    val query: StateFlow<String> = _query.asStateFlow()

    private val _bottomSheetStack = MutableStateFlow<List<BottomSheetScreen>>(emptyList())
    val bottomSheetStack: StateFlow<List<BottomSheetScreen>> = _bottomSheetStack.asStateFlow()

    fun pushBottomSheet(screen: BottomSheetScreen) {
        _bottomSheetStack.value = _bottomSheetStack.value + screen
    }

    fun popBottomSheet() {
        _bottomSheetStack.value = _bottomSheetStack.value.dropLast(1)
    }

    sealed class BottomSheetScreen {
        data class RecipeDetail(val recipe: Recipe) : BottomSheetScreen()
        data class Ingredients(val recipe: Recipe) : BottomSheetScreen()
        data class IngredientsExpandable(val recipe: Recipe) : BottomSheetScreen()
        data class FullRecipe(val recipe: Recipe) : BottomSheetScreen()
        data class SimilarRecipe(val recipe: Recipe) : BottomSheetScreen()
    }

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
        println("🔍 SearchViewModel: updateQuery called with: '$query'")
        _query.value = query
    }

    private fun searchRecipes(query: String) {
        viewModelScope.launch {
            println("🔍 SearchViewModel: Searching for: '$query'")
            _uiState.value = _uiState.value.copy(isLoading = true, error = null)

            when (val result = searchRecipesUseCase.invoke(query)) {
                is com.animeshmandal.recipesearchapp.core.util.Result.Success -> {
                    println("✅ Success: received ${result.data.size} recipes")
                    result.data.forEach { println("🍲 Recipe -> ${it.id}: ${it.title}") }

                    _uiState.value = _uiState.value.copy(
                        searchResults = result.data,
                        isLoading = false,
                        suggestions = emptyList()
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Error -> {
                    println("❌ Error: ${result.exception.message}")
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = result.exception.message,
                        suggestions = emptyList()
                    )
                }
                is com.animeshmandal.recipesearchapp.core.util.Result.Loading -> {
                    println("⏳ Loading...")
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


