package com.animeshmandal.recipesearchapp.presentation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import com.animeshmandal.recipesearchapp.presentation.navigation.MainNavigationScreen
import com.animeshmandal.recipesearchapp.presentation.theme.RecipeSearchAppTheme
import com.animeshmandal.recipesearchapp.presentation.welcome.WelcomeScreen
import com.animeshmandal.recipesearchapp.presentation.welcome.WelcomeViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RecipeSearchAppTheme {
                RecipeSearchApp()
            }
        }
    }
}

@Composable
fun RecipeSearchApp() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        val welcomeViewModel: WelcomeViewModel = hiltViewModel()
        val isFirstTime by welcomeViewModel.isFirstTime.collectAsState()
        
        if (isFirstTime) {
            WelcomeScreen(
                onContinueClick = {
                    welcomeViewModel.onContinueClicked()
                }
            )
        } else {
            MainNavigationScreen()
        }
    }
}
