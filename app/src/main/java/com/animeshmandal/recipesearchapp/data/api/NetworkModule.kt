package com.animeshmandal.recipesearchapp.data.api

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    
    private const val BASE_URL = "https://api.spoonacular.com/"
    private const val API_KEY = "28c8c016ec28486d82e4f441b21b18ee"
    
    @Provides
    @Singleton
    fun provideOkHttpClient(): OkHttpClient {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        
        return OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("X-API-Key", API_KEY)
                    .build()
                chain.proceed(request)
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }
    
    @Provides
    @Singleton
    fun provideSpoonacularApi(retrofit: Retrofit): SpoonacularApi {
        return retrofit.create(SpoonacularApi::class.java)
    }
}
/*

/Users/animesh.mandal/AndroidStudioProjects/RecipeSearchApp/app/build/generated/hilt/component_sources/debug/com/animeshmandal/recipesearchapp/RecipeSearchApplication_HiltComponents.java:140: error: [Dagger/DuplicateBindings] com.animeshmandal.recipesearchapp.data.api.SpoonacularApi is bound multiple times:
public abstract static class SingletonC implements RecipeSearchApplication_GeneratedInjector,
^
@Provides @Singleton com.animeshmandal.recipesearchapp.data.api.SpoonacularApi com.animeshmandal.recipesearchapp.data.api.NetworkModule.provideSpoonacularApi(retrofit2.Retrofit)
@Provides @Singleton com.animeshmandal.recipesearchapp.data.api.SpoonacularApi com.animeshmandal.recipesearchapp.di.NetworkModuleDi.provideSpoonacularApi(retrofit2.Retrofit)
com.animeshmandal.recipesearchapp.data.api.SpoonacularApi is injected at
[com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.SingletonC] com.animeshmandal.recipesearchapp.data.repository.RecipeRepositoryImpl(api, …)
com.animeshmandal.recipesearchapp.data.repository.RecipeRepositoryImpl is injected at
[com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.SingletonC] com.animeshmandal.recipesearchapp.di.RepositoryModule.bindRecipeRepository(recipeRepositoryImpl)
com.animeshmandal.recipesearchapp.domain.repository.RecipeRepository is injected at
[com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.ViewModelC] com.animeshmandal.recipesearchapp.domain.usecase.GetRecipeByIdUseCase(recipeRepository)
com.animeshmandal.recipesearchapp.domain.usecase.GetRecipeByIdUseCase is injected at
[com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.ViewModelC] com.animeshmandal.recipesearchapp.presentation.recipedetail.RecipeDetailViewModel(getRecipeByIdUseCase, …)
com.animeshmandal.recipesearchapp.presentation.recipedetail.RecipeDetailViewModel is injected at
[com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.ViewModelC] com.animeshmandal.recipesearchapp.presentation.recipedetail.RecipeDetailViewModel_HiltModules.BindsModule.binds(vm)
@dagger.hilt.android.internal.lifecycle.HiltViewModelMap java.util.Map<java.lang.Class<?>,javax.inject.Provider<androidx.lifecycle.ViewModel>> is requested at
[com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.ViewModelC] dagger.hilt.android.internal.lifecycle.HiltViewModelFactory.ViewModelFactoriesEntryPoint.getHiltViewModelMap() [com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.SingletonC → com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.ActivityRetainedC → com.animeshmandal.recipesearchapp.RecipeSearchApplication_HiltComponents.ViewModelC]
*/
