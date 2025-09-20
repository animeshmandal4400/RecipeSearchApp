# Recipe Search App

A modern Android recipe application built with Jetpack Compose, following MVVM + UDF + EDA architecture patterns. The app fetches recipes from the Spoonacular API and provides features like recipe browsing, searching, favoriting, and local notifications.

## Features

- **Recipe Browsing**: View popular and all recipes on the home screen
- **Search**: Search for specific recipes with suggestions
- **Recipe Details**: Complete recipe information including ingredients, instructions, equipment, and nutrition
- **Favorites**: Save and manage favorite recipes locally
- **Local Notifications**: Get reminded about saved recipes after a delay
- **Offline Support**: Cache-first approach with Room database
- **Modern UI**: Beautiful, pixel-perfect UI built with Jetpack Compose

## Architecture

The app follows a modular architecture with the following structure:

### Core Modules
- **core:common**: Shared domain entities, use cases, and utilities
- **core:network**: API services and data transfer objects
- **core:database**: Room database entities, DAOs, and repositories
- **core:notification**: Local notification and WorkManager integration

### Feature Modules
- **feature:home**: Home screen with popular and all recipes
- **feature:search**: Recipe search functionality
- **feature:recipe-detail**: Detailed recipe view with all information
- **feature:favorites**: Favorite recipes management

## Tech Stack

- **UI**: Jetpack Compose, Material 3
- **Architecture**: MVVM + UDF + EDA
- **Dependency Injection**: Hilt
- **Database**: Room
- **Networking**: Retrofit + OkHttp
- **Image Loading**: Coil
- **Background Tasks**: WorkManager
- **Navigation**: Navigation Compose

## Project Structure

```
app/
├── src/main/java/com/reciiipiie/app/
│   ├── presentation/
│   │   ├── MainActivity.kt
│   │   ├── navigation/
│   │   └── theme/
│   └── RecipeSearchApplication.kt

core/
├── common/
│   ├── domain/
│   │   ├── entity/
│   │   ├── repository/
│   │   └── usecase/
│   ├── extension/
│   └── util/
├── network/
│   ├── api/
│   ├── model/
│   └── di/
├── database/
│   ├── entity/
│   ├── dao/
│   ├── repository/
│   ├── mapper/
│   └── di/
└── notification/
    ├── di/
    └── ...

feature/
├── home/
│   └── presentation/
├── search/
│   └── presentation/
├── recipe-detail/
│   └── presentation/
└── favorites/
    └── presentation/
```

## Setup

1. Clone the repository
2. Open in Android Studio
3. Add your Spoonacular API key in `core/network/src/main/java/com/reciiipiie/core/network/di/NetworkModule.kt`
4. Build and run the project

## API Integration

The app integrates with the Spoonacular API for recipe data. Make sure to:
1. Get an API key from [Spoonacular](https://spoonacular.com/food-api)
2. Replace `YOUR_API_KEY_HERE` in the NetworkModule with your actual API key

## Features Implementation

### Offline Cache-First
- Recipes are cached in Room database
- App shows cached data first, then refreshes from API
- Favorites are persisted locally

### Local Notifications
- WorkManager schedules notifications for saved recipes
- Notifications deep link to recipe detail screen
- Configurable reminder times

### Modern UI
- Built with Jetpack Compose
- Material 3 design system
- Responsive and accessible design
- Smooth animations and transitions

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## License

This project is licensed under the MIT License - see the LICENSE file for details.