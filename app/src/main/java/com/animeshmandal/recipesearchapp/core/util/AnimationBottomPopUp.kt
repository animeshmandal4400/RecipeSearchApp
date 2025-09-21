package com.animeshmandal.recipesearchapp.core.util

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.MutableTransitionState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.animeshmandal.recipesearchapp.presentation.theme.DarkBlack
import com.animeshmandal.recipesearchapp.presentation.theme.DarkOrange
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FavoriteButtonWithPopup(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit,
    showFavoritePopup: Boolean,
    onDismissPopup: () -> Unit,
    onAddReminderClick: () -> Unit
) {
    var animateClick by remember { mutableStateOf(false) }

    // Scale animation for click effect
    val scale by animateFloatAsState(
        targetValue = if (animateClick) 1.3f else 1f,
        animationSpec = tween(durationMillis = 150)
    )

    Box {
        IconButton(
            onClick = {
                animateClick = true
                onToggleFavorite() // Toggle favorite state
            },
            modifier = Modifier.scale(scale)
        ) {
            Icon(
                imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
                contentDescription = if (isFavorite) "Added to Favorites" else "Add to Favorites",
                tint = if (isFavorite) Color.Red else MaterialTheme.colorScheme.onSurface
            )
        }

        // Reset scale after animation
        LaunchedEffect(animateClick) {
            if (animateClick) {
                delay(150)
                animateClick = false
            }
        }
    }

    // Show popup only when newly added to favorites
    if (showFavoritePopup && isFavorite) {
        FavoriteBottomPopup(
            onDismiss = onDismissPopup,
            onAddReminderClick = onAddReminderClick
        )
    }
}


@Composable
private fun FavoriteBottomPopup(
    onDismiss: () -> Unit,
    onAddReminderClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .clickable { onDismiss() }
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .align(Alignment.BottomCenter)
                .padding(16.dp)
                .padding(WindowInsets.navigationBars.asPaddingValues()),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = DarkBlack),
            elevation = CardDefaults.cardElevation(8.dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Added to Favorite Text
                Text(
                    text = "Added to Favourite",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = Color.White
                )

                // Add Reminder Row
                Row(
                    modifier = Modifier
                        .clickable { onAddReminderClick() }
                        .padding(vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Add Reminder",
                        style = MaterialTheme.typography.bodyMedium,
                        fontWeight = FontWeight.Medium,
                        color = DarkOrange
                    )
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = "Add Reminder",
                        tint = DarkOrange
                    )
                }
            }
        }
    }
}

