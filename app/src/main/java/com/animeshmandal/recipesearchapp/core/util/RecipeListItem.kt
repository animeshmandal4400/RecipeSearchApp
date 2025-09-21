package com.animeshmandal.recipesearchapp.core.util

import com.animeshmandal.recipesearchapp.domain.entity.Recipe

sealed class RecipeOrAd {
    data class RecipeItem(val recipe: Recipe) : RecipeOrAd()
    object AdItem : RecipeOrAd()
}


fun List<Recipe>.toListWithAds(): List<RecipeOrAd> {
    val result = mutableListOf<RecipeOrAd>()
    this.forEachIndexed { index, recipe ->
        result.add(RecipeOrAd.RecipeItem(recipe))
        if ((index + 1) % 5 == 0) {
            result.add(RecipeOrAd.AdItem)
        }
    }
    return result
}
