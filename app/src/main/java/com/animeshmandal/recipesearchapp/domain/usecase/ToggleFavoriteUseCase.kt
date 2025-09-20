package com.animeshmandal.recipesearchapp.domain.usecase

import com.animeshmandal.recipesearchapp.domain.repository.RecipeRepository
import javax.inject.Inject

class ToggleFavoriteUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(recipeId: Int) {
        val isFavorite = recipeRepository.isFavorite(recipeId)
        if (isFavorite) {
            recipeRepository.removeFromFavorites(recipeId)
        } else {
            recipeRepository.addToFavorites(recipeId)
        }
    }
}

