package com.animeshmandal.recipesearchapp.presentation.search

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.focusable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.animeshmandal.recipesearchapp.presentation.search.components.RecipeSearchItem
import com.animeshmandal.recipesearchapp.presentation.search.components.SearchResultItem
import com.animeshmandal.recipesearchapp.presentation.search.components.SearchSuggestions
import com.animeshmandal.recipesearchapp.R
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.presentation.favorites.FavoritesViewModel
import com.animeshmandal.recipesearchapp.presentation.search.SearchViewModel.BottomSheetScreen
import com.animeshmandal.recipesearchapp.presentation.search.SearchViewModel.BottomSheetScreen.*
import com.animeshmandal.recipesearchapp.presentation.search.components.RecipeDetailExpandableDialog
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.map

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNavigateBack: () -> Unit,
    onNavigateToRecipeDetail: (Int) -> Unit,
    viewModel: SearchViewModel = hiltViewModel(),
    favoritesViewModel: FavoritesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val query by viewModel.query.collectAsState()
    val bottomSheetStack by viewModel.bottomSheetStack.collectAsState()

    LaunchedEffect(Unit) {
        println("🔍 SearchScreen: Screen loaded")
    }

    Scaffold { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            // Search Field with Back Icon inside
            OutlinedTextField(
                value = query,
                onValueChange = { newQuery ->
                    println("🔍 SearchScreen: Input changed to: '$newQuery'")
                    viewModel.updateQuery(newQuery)
                },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search recipes") },
                singleLine = true,
                leadingIcon = {
                    IconButton(onClick = onNavigateBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                trailingIcon = {
                    if (query.isNotEmpty()) {
                        IconButton(onClick = { viewModel.updateQuery("") }) {
                            Icon(Icons.Default.Close, contentDescription = "Clear search")
                        }
                    }
                },
                shape = RoundedCornerShape(16.dp),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color(0xFFFEF7F3),
                    focusedLabelColor = MaterialTheme.colorScheme.primary,
                    unfocusedTextColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent
                )
            )

            // Search results / suggestions / error / empty states
            when {
                uiState.isLoading -> {
                    Box(
                        modifier = Modifier.fillMaxSize(),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator()
                    }
                }
                uiState.query.isEmpty() -> {
                    Text(
                        text = "Start typing to search for recipes...",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                uiState.suggestions.isNotEmpty() && uiState.searchResults.isEmpty() -> {
                    SearchSuggestions(
                        suggestions = uiState.suggestions,
                        onSuggestionClick = { suggestion ->
                            viewModel.updateQuery(suggestion)
                        }
                    )
                }
                uiState.searchResults.isNotEmpty() -> {
                    LazyColumn(
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        items(uiState.searchResults) { recipe ->
                            SearchResultItem(
                                title = recipe.title,
                                onClick = {
                                    viewModel.pushBottomSheet(RecipeDetail(recipe))
                                }
                            )
                            Divider()
                        }
                    }
                }
                uiState.error != null -> {
                    Text(
                        text = "Error: ${uiState.error}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.error
                    )
                }
                else -> {
                    Text(
                        text = "No recipes found for '${uiState.query}'",
                        style = MaterialTheme.typography.bodyLarge,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        }

        // Bottom Sheet Stack with slide up animation
//        bottomSheetStack.forEach { screen ->
//            AnimatedVisibility(
//                visible = true,
//                enter = slideInVertically(initialOffsetY = { it }),
//                exit = slideOutVertically(targetOffsetY = { it }),
//                modifier = Modifier.fillMaxSize()
//            ) {
//                when (screen) {
//                    is RecipeDetail -> RecipeDetailDialog(
//                        recipeName = screen.recipe.title,
//                        readyInMinutes = screen.recipe.readyInMinutes,
//                        servings = screen.recipe.servings,
//                        pricePerServing = screen.recipe.pricePerServing,
//                        imageUrl = R.drawable.ic_imageplaceholder,
//                        onDismiss = { viewModel.popBottomSheet() },
//                        onGetIngredientsClick = {
//                            viewModel.pushBottomSheet(Ingredients(screen.recipe))
//                        }
//                    )
//                    is Ingredients -> {
//                        RecipeDetailExpandableDialog(
//                            recipe = screen.recipe,
//                            onDismiss = { viewModel.popBottomSheet() },
//                            onGetSimilarRecipeClick = { /* TODO: Handle similar recipe click */ },
//                        )
//                    }
//                    is FullRecipe -> FullRecipeDialog(
//                        recipe = screen.recipe,
//                        onDismiss = { viewModel.popBottomSheet() },
//                        onGetSimilar = {
//                            viewModel.pushBottomSheet(SimilarRecipe(screen.recipe))
//                        }
//                    )
//                    is SimilarRecipe -> SimilarRecipeDialog(
//                        recipe = screen.recipe,
//                        onDismiss = { viewModel.popBottomSheet() }
//                    )
//
//                    is IngredientsExpandable -> TODO()
//                }
//            }
//        }
        if (bottomSheetStack.isNotEmpty()) {
            val currentScreen = bottomSheetStack.last()

            AnimatedVisibility(
                visible = true, // This will always be true for the current screen
                enter = slideInVertically(initialOffsetY = { it }),
                exit = slideOutVertically(targetOffsetY = { it }),
                modifier = Modifier.fillMaxSize()
            ) {
                when (currentScreen) {
                    is RecipeDetail -> {
                        val isFavorite by favoritesViewModel.uiState
                            .map { it.favoriteRecipes.any { r -> r.id == currentScreen.recipe.id } }
                            .collectAsState(initial = false)

                        RecipeDetailDialog(
                            recipeName = currentScreen.recipe.title,
                            readyInMinutes = currentScreen.recipe.readyInMinutes,
                            servings = currentScreen.recipe.servings,
                            pricePerServing = currentScreen.recipe.pricePerServing,
                            imageUrl = currentScreen.recipe.image,
                            isFavorite = isFavorite,
                            onDismiss = { viewModel.popBottomSheet() },
                            onGetIngredientsClick = {
                                viewModel.pushBottomSheet(Ingredients(currentScreen.recipe))
                            },
                            onToggleFavorite = {
                                favoritesViewModel.toggleFavorite(currentScreen.recipe.id)
                            }
                        )
                    }

                    is Ingredients -> {
                        val isFavorite = favoritesViewModel.isFavorite(currentScreen.recipe.id)
                        RecipeDetailExpandableDialog(
                            recipe = currentScreen.recipe,
                            isFavorite = isFavorite,
                            onDismiss = { viewModel.popBottomSheet() },
                            onGetSimilarRecipeClick = { /* TODO */ },
                            onToggleFavorite = {
                                favoritesViewModel.toggleFavorite(currentScreen.recipe.id)
                            }
                        )
                    }


                    // ... other cases for FullRecipe, SimilarRecipe, etc.
                    else -> {} // Handle other screens if needed
                }
            }
        }
    }
}

@Composable
fun RecipeDetailDialog(
    recipeName: String,
    readyInMinutes: Int,
    servings: Int,
    pricePerServing: Double,
    imageUrl: String?, // Accept URL string
    isFavorite: Boolean, // New parameter
    onDismiss: () -> Unit,
    onGetIngredientsClick: () -> Unit,
    onToggleFavorite: () -> Unit // New parameter
) {
    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight(),
                shape = RoundedCornerShape(topStart = 24.dp, topEnd = 24.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 32.dp, bottom = 16.dp)
                ) {
                    // Title Row with Favorite Icon
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = recipeName,
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onToggleFavorite, // Toggle favorite on click
                            modifier = Modifier.size(48.dp)
                        ) {
                            Icon(
                                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                                contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Spacer(Modifier.height(16.dp))

                    // Recipe Image
                    val painter = if (!imageUrl.isNullOrEmpty()) {
                        rememberAsyncImagePainter(imageUrl)
                    } else {
                        painterResource(id = R.drawable.ic_imageplaceholder)
                    }

                    Image(
                        painter = painter,
                        contentDescription = recipeName,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(220.dp)
                            .clip(RoundedCornerShape(0.dp)),
                        contentScale = ContentScale.Crop
                    )

                    Spacer(Modifier.height(16.dp))

                    // Info Cards
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        InfoCard(title = "Ready In", value = "$readyInMinutes min", modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        InfoCard(title = "Servings", value = servings.toString(), modifier = Modifier.weight(1f))
                        Spacer(Modifier.width(8.dp))
                        InfoCard(title = "Price/serving", value = "₹${pricePerServing.toInt()}", modifier = Modifier.weight(1f))
                    }

                    Spacer(Modifier.height(24.dp))

                    // Get Ingredients Button
                    Button(
                        onClick = onGetIngredientsClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFE5734C)),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp)
                    ) {
                        Text(
                            text = "Get Ingredients",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold,
                            color = Color.White,
                            maxLines = 1
                        )
                        Spacer(Modifier.width(8.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = "Get Ingredients",
                            tint = Color.White
                        )
                    }

                    Spacer(Modifier.height(16.dp))
                }
            }

            val cardHeight = 1050.dp
            IconButton(
                onClick = onDismiss,
                modifier = Modifier
                    .align(Alignment.BottomCenter)
                    .offset(y = (-cardHeight / 2) - 24.dp)
                    .background(Color.Black, shape = CircleShape)
                    .size(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close",
                    tint = Color.White
                )
            }
        }
    }
}


@Composable
fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.height(90.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(12.dp),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

//@Composable
//fun IngredientsDialog(recipe: Recipe, onDismiss: () -> Unit, onGetFullRecipe: () -> Unit) {
//    RecipeDetailDialog(
//        recipeName = "Ingredients of ${recipe.title}",
//        readyInMinutes = recipe.readyInMinutes,
//        servings = recipe.servings,
//        pricePerServing = recipe.pricePerServing,
//        imageUrl = R.drawable.ic_imageplaceholder,
//        onDismiss = onDismiss,
//        onGetIngredientsClick = onGetFullRecipe
//    )
//}
//
//@Composable
//fun FullRecipeDialog(recipe: Recipe, onDismiss: () -> Unit, onGetSimilar: () -> Unit) {
//    RecipeDetailDialog(
//        recipeName = "Full Recipe ${recipe.title}",
//        readyInMinutes = recipe.readyInMinutes,
//        servings = recipe.servings,
//        pricePerServing = recipe.pricePerServing,
//        imageUrl = R.drawable.ic_imageplaceholder,
//        onDismiss = onDismiss,
//        onGetIngredientsClick = onGetSimilar
//    )
//}

//@Composable
//fun SimilarRecipeDialog(recipe: Recipe, onDismiss: () -> Unit) {
//    RecipeDetailDialog(
//        recipeName = "Similar Recipes of ${recipe.title}",
//        readyInMinutes = recipe.readyInMinutes,
//        servings = recipe.servings,
//        pricePerServing = recipe.pricePerServing,
//        imageUrl = R.drawable.ic_imageplaceholder,
//        onDismiss = onDismiss,
//        onGetIngredientsClick = {}
//    )
//}


