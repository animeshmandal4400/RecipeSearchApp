package com.animeshmandal.recipesearchapp.data.database

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import android.content.Context
import com.animeshmandal.recipesearchapp.data.database.converter.*
import com.animeshmandal.recipesearchapp.data.database.dao.*
import com.animeshmandal.recipesearchapp.data.database.*

@Database(
    entities = [
        RecipeEntity::class,
        IngredientEntity::class,
        EquipmentEntity::class,
        NutritionEntity::class,
        NutrientEntity::class,
        PropertyEntity::class,
        FlavonoidEntity::class,
        CaloricBreakdownEntity::class,
        WeightPerServingEntity::class
    ],
    version = 1,
    exportSchema = false
)
@TypeConverters(
    ListStringConverter::class,
    NutritionConverter::class,
    IngredientConverter::class,
    EquipmentConverter::class
)
abstract class RecipeDatabase : RoomDatabase() {
    
    abstract fun recipeDao(): RecipeDao
    
    companion object {
        const val DATABASE_NAME = "recipe_database"
        
        @Volatile
        private var INSTANCE: RecipeDatabase? = null
        
        fun getDatabase(context: Context): RecipeDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RecipeDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}

