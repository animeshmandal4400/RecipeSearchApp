package com.animeshmandal.recipesearchapp.presentation.home

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.animeshmandal.recipesearchapp.presentation.home.components.RecipeCard
import com.animeshmandal.recipesearchapp.presentation.home.components.SearchBar
import com.animeshmandal.recipesearchapp.presentation.home.components.PopularRecipesSection
import com.animeshmandal.recipesearchapp.presentation.home.components.AllRecipesSection

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNavigateToSearch: () -> Unit,
    onNavigateToRecipeDetail: (Int) -> Unit,
    onNavigateToFavorites: () -> Unit,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(Unit) {
        viewModel.loadRecipes()
    }
    
    LaunchedEffect(uiState) {
        println("🏠 HomeScreen: UI State updated - Popular: ${uiState.popularRecipes.size}, All: ${uiState.allRecipes.size}, LoadingPopular: ${uiState.isLoadingPopular}, LoadingAll: ${uiState.isLoadingAll}")
        if (uiState.popularRecipes.isNotEmpty()) {
            println("🏠 HomeScreen: Popular recipes: ${uiState.popularRecipes.map { it.title }}")
        }
        if (uiState.allRecipes.isNotEmpty()) {
            println("🏠 HomeScreen: All recipes: ${uiState.allRecipes.map { it.title }}")
        }
    }
    
    Scaffold(
        bottomBar = {
            NavigationBar {
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Home, contentDescription = "Home") },
                    label = { Text("Home") },
                    selected = true,
                    onClick = { }
                )
                NavigationBarItem(
                    icon = { Icon(Icons.Default.Favorite, contentDescription = "Favorites") },
                    label = { Text("Favorite") },
                    selected = false,
                    onClick = onNavigateToFavorites
                )
            }
        }
    ) { paddingValues ->
        val listState = rememberLazyListState()
        
        LazyColumn(
            state = listState,
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header section with greeting and description
            item {
                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(
                        text = "Hey ${uiState.userName}",
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold
                    )
                    
                    Text(
                        text = "Discover tasty and healthy recipes",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
            
            // Sticky Search Bar
            stickyHeader {
                SearchBar(
                    onSearchClick = {
                        println("🏠 HomeScreen: Search button clicked, navigating to search screen")
                        onNavigateToSearch()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            MaterialTheme.colorScheme.background,
                            MaterialTheme.shapes.medium
                        )
                        .padding(vertical = 8.dp)
                )
            }
            
            // Popular Recipes Section
            item {
                PopularRecipesSection(
                    recipes = uiState.popularRecipes,
                    onRecipeClick = onNavigateToRecipeDetail,
                    isLoading = uiState.isLoadingPopular
                )
            }
            
            // All Recipes Section
            item {
                AllRecipesSection(
                    recipes = uiState.allRecipes,
                    onRecipeClick = onNavigateToRecipeDetail,
                    isLoading = uiState.isLoadingAll,
                    onLoadMore = { viewModel.loadMoreRecipes() }
                )
            }
        }
    }
}


