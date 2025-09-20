package com.animeshmandal.recipesearchapp.presentation.welcome

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.animeshmandal.recipesearchapp.data.datastore.UserPreferences
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeViewModel @Inject constructor(
    private val userPreferences: UserPreferences
) : ViewModel() {
    
    private val _isFirstTime = MutableStateFlow(true)
    val isFirstTime: StateFlow<Boolean> = _isFirstTime.asStateFlow()
    
    init {
        viewModelScope.launch {
            userPreferences.isFirstTime.collect { isFirst ->
                _isFirstTime.value = isFirst
            }
        }
    }
    
    fun onContinueClicked() {
        viewModelScope.launch {
            userPreferences.setFirstTimeCompleted()
        }
    }
}
