package com.animeshmandal.recipesearchapp.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.animeshmandal.recipesearchapp.presentation.favorites.FavoritesScreen
import com.animeshmandal.recipesearchapp.presentation.home.HomeScreen
import com.animeshmandal.recipesearchapp.presentation.recipedetail.RecipeDetailScreen
import com.animeshmandal.recipesearchapp.presentation.search.SearchScreen

@Composable
fun RecipeNavigation(navController: NavHostController) {
    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNavigateToSearch = { navController.navigate("search") },
                onNavigateToRecipeDetail = { recipeId -> 
                    navController.navigate("recipe_detail/$recipeId")
                },
                onNavigateToFavorites = { navController.navigate("favorites") }
            )
        }
        
        composable("search") {
            SearchScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecipeDetail = { recipeId -> 
                    navController.navigate("recipe_detail/$recipeId")
                }
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
        
        composable("favorites") {
            FavoritesScreen(
                onNavigateBack = { navController.popBackStack() },
                onNavigateToRecipeDetail = { recipeId -> 
                    navController.navigate("recipe_detail/$recipeId")
                }
            )
        }
    }
}
