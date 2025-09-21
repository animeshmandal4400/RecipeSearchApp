# Recipe Search App(Reciiipiie)

## Overview
The **Recipe Search App** allows users to browse, search, and save recipes using the **Spoonacular API**. Users can view detailed recipe information, mark recipes as favourites, and receive local notifications reminding them about saved recipes.

---

## Features

### Home Screen
- Fetch and display **popular recipes** using *Get Random Recipes* API.
- Display **all recipes** using *Search Recipes* API.
- Ad integration after every 5 recipes.

### Recipe Details
- View **full recipe information**: ingredients, instructions, nutrients, and similar recipes.
- Navigate between different states using **bottom sheet stack animation**.
- Swipe down to return to previous recipe state.

### Search & Favourites
- Search for any recipe using the search bar.
- Mark recipes as **favourite**, saved locally.
- View all favourite recipes with caching in **Room DB**.
- Offline cache-first approach:
  - Shows cached recipes on app start.
  - Refreshes data from API in background.

### Notifications
- Trigger **local notifications** after a delay (e.g., 1 hour) when a recipe is saved:
  
  > “You saved Pasta Carbonara — have you tried it yet?” 🍝

### Architecture & Tech Stack
- **Jetpack Compose** for UI
- **MVVM + UDF + EDA** architecture
- **Room DB** for caching recipes and favourites
- **Coroutine + Flow** for async and reactive programming
- **Navigation Component** & **Bottom Sheet Stack** for smooth state transitions

---

## Screens & Navigation
- **Home Screen**: Browse popular and all recipes.
- **Search Screen**: Search recipes with suggestions and results.
- **Recipe Detail**: Full recipe details in bottom sheet with ingredient and similar recipe navigation.
- **Favourites Screen**: List of user’s favourite recipes.
- Bottom sheet navigation stack with slide-up animation.

---

## Installation & Setup
1. Clone the repository:

## Resources
- API Documentation: [Spoonacular API Docs](https://spoonacular.com/food-api/docs)
- Figma Design: [Figma Link](https://www.figma.com/design/3osDPZ0cy0AMfynjkRFOoV/MathOnGo---Android---Assignment?node-id=0-1&p=f)

## APK 
- APK file here: [Install Apk](https://github.com/animeshmandal4400/RecipeSearchApp/releases/download/v1.0.0/app-debug.apk)

