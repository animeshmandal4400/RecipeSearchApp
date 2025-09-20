package com.animeshmandal.recipesearchapp.domain.usecase

import com.animeshmandal.recipesearchapp.core.util.Result
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.domain.repository.RecipeRepository
import javax.inject.Inject

class GetSimilarRecipesUseCase @Inject constructor(
    private val recipeRepository: RecipeRepository
) {
    suspend operator fun invoke(id: Int): Result<List<Recipe>> {
        return recipeRepository.getSimilarRecipes(id)
    }
}
