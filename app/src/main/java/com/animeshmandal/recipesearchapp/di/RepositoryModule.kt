package com.animeshmandal.recipesearchapp.di

import com.animeshmandal.recipesearchapp.data.mapper.RecipeMapper
import com.animeshmandal.recipesearchapp.data.repository.RecipeRepositoryImpl
import com.animeshmandal.recipesearchapp.domain.repository.RecipeRepository
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {
    
    @Binds
    @Singleton
    abstract fun bindRecipeRepository(
        recipeRepositoryImpl: RecipeRepositoryImpl
    ): RecipeRepository
}

@Module
@InstallIn(SingletonComponent::class)
object MapperModule {
    
    @Provides
    @Singleton
    fun provideRecipeMapper(): RecipeMapper {
        return RecipeMapper()
    }
}

