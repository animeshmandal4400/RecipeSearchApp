package com.animeshmandal.recipesearchapp.presentation.home.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun SearchBar(
    onSearchClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        value = "",
        onValueChange = { },
        modifier = modifier
            .fillMaxWidth()
            .height(56.dp)
            .clickable { 
                println("🔍 SearchBar: Clicked!")
                onSearchClick() 
            },
        placeholder = {
            Text("Search Any Recipe")
        },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = "Search"
            )
        },
        readOnly = true,
        shape = MaterialTheme.shapes.medium,
        enabled = true
    )
}


