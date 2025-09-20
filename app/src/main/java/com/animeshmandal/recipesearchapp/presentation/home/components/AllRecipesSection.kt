package com.animeshmandal.recipesearchapp.presentation.home.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.animeshmandal.recipesearchapp.domain.entity.Recipe

@Composable
fun AllRecipesSection(
    recipes: List<Recipe>,
    onRecipeClick: (Int) -> Unit,
    isLoading: Boolean,
    onLoadMore: () -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = "All Recipes",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        if (isLoading) {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                repeat(10) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(120.dp),
                        shape = MaterialTheme.shapes.medium
                    ) {
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator()
                        }
                    }
                }
            }
        } else {
            Column(
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                recipes.forEach { recipe ->
                    RecipeListItem(
                        recipe = recipe,
                        onClick = { onRecipeClick(recipe.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeListItem(
    recipe: Recipe,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                painter = rememberAsyncImagePainter(recipe.image),
                contentDescription = recipe.title,
                modifier = Modifier
                    .size(80.dp)
                    .clip(RoundedCornerShape(8.dp)),
                contentScale = ContentScale.Crop
            )
            
            Spacer(modifier = Modifier.width(12.dp))
            
            Column(
                modifier = Modifier.weight(1f),
                verticalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Text(
                    text = recipe.title,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                
                Text(
                    text = "Ready in ${recipe.readyInMinutes} min",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                
                if (recipe.pricePerServing > 0) {
                    Text(
                        text = "₹${recipe.pricePerServing.toInt()}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.primary,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

