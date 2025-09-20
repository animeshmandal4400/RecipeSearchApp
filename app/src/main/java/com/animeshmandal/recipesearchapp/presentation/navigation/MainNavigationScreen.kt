package com.animeshmandal.recipesearchapp.presentation.navigation

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.animeshmandal.recipesearchapp.presentation.favorites.FavoritesScreen
import com.animeshmandal.recipesearchapp.presentation.home.HomeScreen
import com.animeshmandal.recipesearchapp.presentation.recipedetail.RecipeDetailScreen
import com.animeshmandal.recipesearchapp.presentation.search.SearchScreen
import com.animeshmandal.recipesearchapp.presentation.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainNavigationScreen() {
    val navController = rememberNavController()
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    Scaffold(
        bottomBar = {
            // Hide bottom navigation on search and detail screens
            val isSearchOrDetailScreen = currentDestination?.route == "search" || 
                                       currentDestination?.route?.startsWith("recipe_detail/") == true
            if (!isSearchOrDetailScreen) {
                NavigationBar {
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Home, 
                            contentDescription = "Home",
                            tint = if (currentDestination?.route == "home") Orange else Color.Unspecified
                        ) 
                    },
                    label = { 
                        Text(
                            "Home",
                            color = if (currentDestination?.route == "home") Orange else Color.Unspecified,
                            fontWeight = if (currentDestination?.route == "home") FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    selected = currentDestination?.route == "home",
                    onClick = {
                        navController.navigate("home") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
                
                NavigationBarItem(
                    icon = { 
                        Icon(
                            Icons.Default.Favorite, 
                            contentDescription = "Favorites",
                            tint = if (currentDestination?.route == "favorites") Orange else Color.Unspecified
                        ) 
                    },
                    label = { 
                        Text(
                            "Favorites",
                            color = if (currentDestination?.route == "favorites") Orange else Color.Unspecified,
                            fontWeight = if (currentDestination?.route == "favorites") FontWeight.Bold else FontWeight.Normal
                        ) 
                    },
                    selected = currentDestination?.route == "favorites",
                    onClick = {
                        navController.navigate("favorites") {
                            popUpTo(navController.graph.findStartDestination().id) {
                                saveState = true
                            }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                )
            }
            }
        }
    ) { paddingValues ->
        NavHost(
            navController = navController,
            startDestination = "home"
        ) {
            composable("home") {
                HomeScreen(
                    onNavigateToSearch = { 
                        println("🧭 Navigation: Navigating to search screen")
                        println("🧭 Navigation: Current destination: ${navController.currentDestination?.route}")
                        try {
                            navController.navigate("search")
                            println("🧭 Navigation: Navigation call successful")
                        } catch (e: Exception) {
                            println("🧭 Navigation: Navigation failed: ${e.message}")
                        }
                    },
                    onNavigateToRecipeDetail = { recipeId -> 
                        println("🧭 Navigation: Navigating to recipe detail: $recipeId")
                        navController.navigate("recipe_detail/$recipeId")
                    },
                    onNavigateToFavorites = { 
                        println("🧭 Navigation: Navigating to favorites screen")
                        navController.navigate("favorites") 
                    }
                )
            }
            
            composable("search") {
                println("🧭 Navigation: SearchScreen composable is being called")
                SearchScreen(
                    onNavigateBack = { 
                        println("🧭 Navigation: SearchScreen back button clicked")
                        navController.popBackStack() 
                    },
                    onNavigateToRecipeDetail = { recipeId -> 
                        println("🧭 Navigation: SearchScreen navigating to recipe detail: $recipeId")
                        navController.navigate("recipe_detail/$recipeId")
                    }
                )
            }
            
            composable("favorites") {
                FavoritesScreen(
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToRecipeDetail = { recipeId -> 
                        navController.navigate("recipe_detail/$recipeId")
                    },
                    showBackButton = false // No back button when accessed via bottom navigation
                )
            }
            
            composable("recipe_detail/{recipeId}") { backStackEntry ->
                val recipeId = backStackEntry.arguments
                    ?.getString("recipeId")
                    ?.toIntOrNull() ?: 0
                RecipeDetailScreen(
                    recipeId = recipeId,
                    onNavigateBack = { navController.popBackStack() },
                    onNavigateToSimilarRecipe = { similarRecipeId -> 
                        navController.navigate("recipe_detail/$similarRecipeId")
                    }
                )
            }
        }
    }
}
