package com.animeshmandal.recipesearchapp.data.datastore

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "user_preferences")

@Singleton
class UserPreferences @Inject constructor(
    private val context: Context
) {
    
    private val isFirstTimeKey = booleanPreferencesKey("is_first_time")
    
    val isFirstTime: Flow<Boolean> = context.dataStore.data.map { preferences ->
        preferences[isFirstTimeKey] ?: true
    }
    
    suspend fun setFirstTimeCompleted() {
        context.dataStore.edit { preferences ->
            preferences[isFirstTimeKey] = false
        }
    }
}

