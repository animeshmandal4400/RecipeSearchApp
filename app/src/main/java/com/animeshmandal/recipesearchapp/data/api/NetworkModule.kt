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
    private const val API_KEY = "ea16602c082648fab38a0980ec168401"
    
    init {
        println("🌐 NetworkModule: Initializing with BASE_URL: $BASE_URL")
        println("🌐 NetworkModule: API_KEY: $API_KEY")
    }
    
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
                println("🌐 API Request: ${request.method} ${request.url}")
                println("🌐 Headers: ${request.headers}")
                println("🌐 API Key: $API_KEY")
                
                val response = chain.proceed(request)
                println("🌐 API Response: ${response.code} ${response.message}")
                println("🌐 Response Headers: ${response.headers}")
                
                response
            }
            .connectTimeout(30, TimeUnit.SECONDS)
            .readTimeout(30, TimeUnit.SECONDS)
            .writeTimeout(30, TimeUnit.SECONDS)
            .build()
    }
    
    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        println("🌐 NetworkModule: Creating Retrofit with BASE_URL: $BASE_URL")
        return Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideSpoonacularApi(retrofit: Retrofit): SpoonacularApi {
        println("🌐 NetworkModule: Creating SpoonacularApi")
        return retrofit.create(SpoonacularApi::class.java)
    }
}

