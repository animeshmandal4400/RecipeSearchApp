package com.animeshmandal.recipesearchapp.presentation.recipedetail

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.rememberAsyncImagePainter
import com.animeshmandal.recipesearchapp.domain.entity.Recipe
import com.animeshmandal.recipesearchapp.presentation.theme.Orange

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RecipeDetailScreen(
    recipeId: Int,
    onNavigateBack: () -> Unit,
    onNavigateToSimilarRecipe: (Int) -> Unit,
    viewModel: RecipeDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    
    LaunchedEffect(recipeId) {
        viewModel.loadRecipe(recipeId)
    }
    
    when {
        uiState.isLoading -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
        
        uiState.error != null -> {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "Error: ${uiState.error}",
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
        
        uiState.recipe != null -> {
            RecipeDetailContent(
                recipe = uiState.recipe!!,
                isFavorite = uiState.isFavorite,
                onNavigateBack = onNavigateBack,
                onToggleFavorite = { viewModel.toggleFavorite() },
                onNavigateToSimilarRecipe = onNavigateToSimilarRecipe,
                onSetReminder = { minutes -> viewModel.setReminder(minutes) }
            )
        }
    }
}

@Composable
private fun RecipeDetailContent(
    recipe: Recipe,
    isFavorite: Boolean,
    onNavigateBack: () -> Unit,
    onToggleFavorite: () -> Unit,
    onNavigateToSimilarRecipe: (Int) -> Unit,
    onSetReminder: (Int) -> Unit
) {
    var showFavoritePopup by remember { mutableStateOf(false) }
    var showReminderDialog by remember { mutableStateOf(false) }
    var showConfirmationDialog by remember { mutableStateOf(false) }
    var selectedReminderTime by remember { mutableStateOf("") }
    
    // Show favorite popup when favorite is toggled
    LaunchedEffect(isFavorite) {
        if (isFavorite) {
            showFavoritePopup = true
        }
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 80.dp) // Add padding for bottom action bar
        ) {
            // Recipe Image with Overlay
            item {
                RecipeImageHeader(
                    recipe = recipe,
                    isFavorite = isFavorite,
                    onNavigateBack = onNavigateBack,
                    onToggleFavorite = onToggleFavorite
                )
            }
            
            // Info Cards
            item {
                RecipeInfoCards(recipe = recipe)
            }
            
            // Ingredients Section
            item {
                RecipeIngredientsSection(ingredients = recipe.extendedIngredients ?: emptyList())
            }
            
            // Instructions Section
            item {
                RecipeInstructionsSection(instructions = recipe.instructions ?: "")
            }
            
            // Equipment Section
            item {
                RecipeEquipmentSection(equipment = recipe.equipment ?: emptyList())
            }
            
            // Quick Summary Section
            item {
                RecipeQuickSummarySection(summary = recipe.summary)
            }
            
            // Nutrition Section
            item {
                RecipeNutritionSection(nutrition = recipe.nutrition)
            }
            
            // Health Information Sections
            item {
                RecipeHealthInfoSections(nutrition = recipe.nutrition)
            }
        }
        
        // Bottom Action Bar
        BottomActionBar(
            isFavorite = isFavorite,
            onToggleFavorite = onToggleFavorite,
            onSetReminder = { showReminderDialog = true },
            modifier = Modifier.align(Alignment.BottomCenter)
        )
        
        // Favorite Popup
        if (showFavoritePopup) {
            FavoritePopup(
                onDismiss = { showFavoritePopup = false }
            )
        }
        
        // Reminder Dialog
        if (showReminderDialog) {
            ReminderDialog(
                onDismiss = { showReminderDialog = false },
                onTimeSelected = { timeText, minutes ->
                    selectedReminderTime = timeText
                    onSetReminder(minutes)
                    showReminderDialog = false
                    showConfirmationDialog = true
                }
            )
        }
        
        // Confirmation Dialog
        if (showConfirmationDialog) {
            ConfirmationDialog(
                reminderTime = selectedReminderTime,
                onDismiss = { showConfirmationDialog = false }
            )
        }
    }
}

@Composable
private fun RecipeImageHeader(
    recipe: Recipe,
    isFavorite: Boolean,
    onNavigateBack: () -> Unit,
    onToggleFavorite: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
    ) {
        // Recipe Image
        Image(
            painter = rememberAsyncImagePainter(recipe.image),
            contentDescription = recipe.title,
            modifier = Modifier.fillMaxSize(),
            contentScale = ContentScale.Crop
        )
        
        // Gradient Overlay
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Color.Black.copy(alpha = 0.3f)
                )
        )
        
        // Top Bar with Back and Favorite
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Top
        ) {
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back",
                    tint = Color.White
                )
            }
            
            IconButton(
                onClick = onToggleFavorite,
                modifier = Modifier
                    .background(
                        Color.Black.copy(alpha = 0.5f),
                        CircleShape
                    )
            ) {
                Icon(
                    imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                    contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites",
                    tint = if (isFavorite) Color.Red else Color.White
                )
            }
        }
        
        // Recipe Title at Bottom
        Text(
            text = recipe.title,
            style = MaterialTheme.typography.headlineMedium,
            fontWeight = FontWeight.Bold,
            color = Color.White,
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        )
    }
}

@Composable
private fun RecipeInfoCards(recipe: Recipe) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        // Ready in Minutes Card
        InfoCard(
            title = "Ready in",
            value = "${recipe.readyInMinutes} min",
            modifier = Modifier.weight(1f),
            valueColor = Orange
        )
        
        // Servings Card
        InfoCard(
            title = "Servings",
            value = recipe.servings.toString(),
            modifier = Modifier.weight(1f)
        )
        
        // Price per Serving Card
        InfoCard(
            title = "Price/serving",
            value = "₹${recipe.pricePerServing.toInt()}",
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
private fun InfoCard(
    title: String,
    value: String,
    modifier: Modifier = Modifier,
    valueColor: Color = Color.Unspecified
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = if (valueColor != Color.Unspecified) valueColor else Color.Black
            )
        }
    }
}

@Composable
private fun RecipeIngredientsSection(
    ingredients: List<com.animeshmandal.recipesearchapp.domain.entity.Ingredient>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Ingredients",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(ingredients) { ingredient ->
                IngredientItem(ingredient = ingredient)
            }
        }
    }
}

@Composable
private fun IngredientItem(
    ingredient: com.animeshmandal.recipesearchapp.domain.entity.Ingredient
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Circular ingredient image
        Image(
            painter = rememberAsyncImagePainter(ingredient.image),
            contentDescription = ingredient.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Text(
            text = ingredient.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
private fun RecipeInstructionsSection(instructions: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Instructions",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = instructions,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun RecipeEquipmentSection(
    equipment: List<com.animeshmandal.recipesearchapp.domain.entity.Equipment>
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Equipments",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(equipment) { equipment ->
                EquipmentItem(equipment = equipment)
            }
        }
    }
}

@Composable
private fun EquipmentItem(
    equipment: com.animeshmandal.recipesearchapp.domain.entity.Equipment
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Circular equipment image
        Image(
            painter = rememberAsyncImagePainter(equipment.image),
            contentDescription = equipment.name,
            modifier = Modifier
                .size(60.dp)
                .clip(CircleShape),
            contentScale = ContentScale.Crop
        )
        
        Text(
            text = equipment.name,
            style = MaterialTheme.typography.bodySmall,
            maxLines = 2,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.width(60.dp)
        )
    }
}

@Composable
private fun RecipeQuickSummarySection(summary: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Text(
            text = "Quick Summary",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold
        )
        
        Text(
            text = summary,
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 20.sp
        )
    }
}

@Composable
private fun RecipeNutritionSection(
    nutrition: com.animeshmandal.recipesearchapp.domain.entity.Nutrition?
) {
    var isExpanded by remember { mutableStateOf(false) }
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clickable { isExpanded = !isExpanded },
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                // Blue dot icon
                Box(
                    modifier = Modifier
                        .size(8.dp)
                        .background(Color.Blue, CircleShape)
                )
                
                Text(
                    text = "Nutrition",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold
                )
            }
            
            Icon(
                imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                contentDescription = if (isExpanded) "Collapse" else "Expand",
                tint = Color.Gray
            )
        }
        
        if (isExpanded && nutrition != null) {
            nutrition.nutrients?.forEach { nutrient ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = nutrient.name,
                        style = MaterialTheme.typography.bodyMedium
                    )
                    Text(
                        text = "${nutrient.amount} ${nutrient.unit}",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun RecipeHealthInfoSections(
    nutrition: com.animeshmandal.recipesearchapp.domain.entity.Nutrition?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        // Good for health nutrients
        HealthInfoRow(
            title = "Good for health nutrition",
            onClick = { /* Handle click */ }
        )
        
        // Bad for health nutrients
        HealthInfoRow(
            title = "Bad for health nutrition",
            onClick = { /* Handle click */ }
        )
    }
}

@Composable
private fun HealthInfoRow(
    title: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Blue dot icon
            Box(
                modifier = Modifier
                    .size(8.dp)
                    .background(Color.Blue, CircleShape)
            )
            
            Text(
                text = title,
                style = MaterialTheme.typography.bodyMedium
            )
        }
        
        // Blue dot icon on the right
        Box(
            modifier = Modifier
                .size(8.dp)
                .background(Color.Blue, CircleShape)
        )
    }
}

@Composable
private fun BottomActionBar(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    onSetReminder: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = Color.White
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Favorite Button
            Button(
                onClick = onToggleFavorite,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isFavorite) Orange else Color.Transparent,
                    contentColor = if (isFavorite) Color.White else Orange
                ),
                border = if (!isFavorite) ButtonDefaults.outlinedButtonBorder.copy(
                    brush = androidx.compose.ui.graphics.SolidColor(Orange)
                ) else null,
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                        contentDescription = if (isFavorite) "Remove from favorites" else "Add to favorites"
                    )
                    Text(
                        text = if (isFavorite) "Added to Favourite" else "Add to Favourite",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
            
            // Reminder Button
            Button(
                onClick = onSetReminder,
                colors = ButtonDefaults.buttonColors(
                    containerColor = Orange,
                    contentColor = Color.White
                ),
                shape = RoundedCornerShape(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Reminder"
                    )
                    Text(
                        text = "Add Reminder",
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}

@Composable
private fun FavoritePopup(
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Favorite,
                    contentDescription = "Favorite",
                    tint = Orange,
                    modifier = Modifier.size(48.dp)
                )
                
                Text(
                    text = "Added to Favourite",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center
                )
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}

@Composable
private fun ReminderDialog(
    onDismiss: () -> Unit,
    onTimeSelected: (String, Int) -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
                Text(
                    text = "Set a Reminder",
                    style = MaterialTheme.typography.headlineSmall,
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Text(
                    text = "You will be reminded in",
                    style = MaterialTheme.typography.bodyLarge,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.fillMaxWidth()
                )
                
                Column(
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    ReminderTimeOption(
                        timeText = "30m",
                        minutes = 30,
                        onClick = { onTimeSelected("30m", 30) }
                    )
                    
                    ReminderTimeOption(
                        timeText = "1h 30m",
                        minutes = 90,
                        onClick = { onTimeSelected("1h 30m", 90) }
                    )
                    
                    ReminderTimeOption(
                        timeText = "2h",
                        minutes = 120,
                        onClick = { onTimeSelected("2h", 120) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ReminderTimeOption(
    timeText: String,
    minutes: Int,
    onClick: () -> Unit
) {
    OutlinedButton(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.outlinedButtonColors(
            contentColor = Orange
        ),
        border = ButtonDefaults.outlinedButtonBorder.copy(
            brush = androidx.compose.ui.graphics.SolidColor(Orange)
        ),
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = timeText,
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Medium
        )
    }
}

@Composable
private fun ConfirmationDialog(
    reminderTime: String,
    onDismiss: () -> Unit
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color.White
            ),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Text(
                    text = "You will be reminded in $reminderTime",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Medium,
                    textAlign = TextAlign.Center
                )
                
                Button(
                    onClick = onDismiss,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Orange,
                        contentColor = Color.White
                    ),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text("OK")
                }
            }
        }
    }
}