package com.animeshmandal.recipesearchapp.core.util

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButtonDefaults.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import kotlinx.coroutines.delay

@Composable
fun AnimatedFavoriteButton(
    isFavorite: Boolean,
    onToggleFavorite: () -> Unit
) {
    var animateClick by remember { mutableStateOf(false) }
    val scale by animateFloatAsState(
        targetValue = if (animateClick) 1.3f else 1f,
        animationSpec = tween(durationMillis = 150)
    )

    IconButton(
        onClick = {
            animateClick = true
            onToggleFavorite()
        },
        modifier = Modifier.scale(scale)
    ) {
        Icon(
            imageVector = if (isFavorite) Icons.Default.Favorite else Icons.Default.FavoriteBorder,
            contentDescription = if (isFavorite) "Remove from Favorites" else "Add to Favorites",
            tint = MaterialTheme.colorScheme.primary
        )
    }

    LaunchedEffect(animateClick) {
        if (animateClick) {
            delay(150)
            animateClick = false
        }
    }
}
