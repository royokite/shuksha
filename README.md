---

# 📱 Shuksha – Android Recipe App (Learning Project)

This is a **learning Android app built with Kotlin + Jetpack Compose**.
It fetches recipe data from a free API and displays it in a simple UI.

The project is being built step-by-step to learn modern Android development.

---

# 🚀 Tech Stack

* Kotlin
* Jetpack Compose (UI)
* Retrofit (Networking)
* Coroutines (Async tasks)
* ViewModel (State management)
* Coil (Image loading)
* TheMealDB API (Free recipe API)

---

# 🧠 What This Project Covers

## Phase 1 – Setup & Basics

* Installing Android Studio (Narwhal)
* Creating first Compose project
* Understanding Gradle (`build.gradle.kts`)
* Project structure (`data`, `network`, `repository`, `viewmodel`, `presentation`)

---

## Phase 2 – Networking

* Setting up Retrofit
* Creating API interface
* Building Retrofit instance
* Making first API call:

```text
https://www.themealdb.com/api/json/v1/1/search.php?s=chicken
```

* Handling JSON response with data classes

---

## Phase 3 – Architecture

* Repository pattern
* ViewModel with `viewModelScope`
* Using `mutableStateOf` for UI state
* Separating concerns:

   * UI → ViewModel → Repository → API

---

## Phase 4 – UI (Jetpack Compose)

* `LazyColumn` for lists
* `Card` UI components
* `AsyncImage` (Coil)
* Composable functions:

   * `RecipeCard`
   * `RecipeScreen` (planned/refactor stage)

---

## Phase 5 – Git & GitHub Setup

* Setting up GitHub repository
* Fixing SSH authentication issues
* Creating personal SSH key:

```bash
ssh-keygen -t ed25519 -C "email@example.com"
```

* Configuring Git identity for personal vs work projects

---

# 📂 Project Structure

```text
com.example.shuksha

├── data
│   ├── Meal.kt
│   ├── MealResponse.kt
│
├── network
│   ├── RecipeApi.kt
│   ├── RetrofitInstance.kt
│
├── repository
│   ├── RecipeRepository.kt
│
├── viewmodel
│   ├── RecipeViewModel.kt
│
├── presentation
│   ├── RecipeCard.kt
│   ├── RecipeScreen.kt (future)
│
├── MainActivity.kt
```

---

# 🔌 API Used

We use **TheMealDB free API**:

### Search meals by name

```text
https://www.themealdb.com/api/json/v1/1/search.php?s=chicken
```

---

# ⚙️ Setup Instructions

## 1. Clone the project

```bash
git clone git@github-personal:USERNAME/REPO.git
```

(or HTTPS if needed)

---

## 2. Open in Android Studio

* Open project folder
* Wait for Gradle sync

---

## 3. Run on device/emulator

* Use Pixel emulator OR physical Android device
* Minimum SDK: 24+

---

## 4. Internet permission

Ensure this exists in `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

---

# 🧪 Current Features

* Fetch recipes from API
* Display recipe name
* Display recipe image
* Basic Compose UI list

---

# 🧭 Next Steps (Learning Roadmap)

Planned improvements:

## UI Enhancements

* Loading spinner
* Empty state UI
* Error handling UI

## Features

* Search bar
* Recipe details screen
* Favorites system

## Architecture

* Full MVVM cleanup
* UI state wrapper (`Loading / Success / Error`)
* Dependency injection (Hilt)

---

# 🧑‍💻 Notes

This project is part of a learning journey into Android development.

Mistakes, experiments, and refactors are expected and documented.

---



