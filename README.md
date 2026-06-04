# Assesment Mandiri - Movie App

A modern Android application built to display movies by consuming the [TMDB (The Movie Database) API](https://developer.themoviedb.org/docs). This project was created as an assessment project and demonstrates modern Android development practices, architecture, and UI paradigms.

## Features

- **Genre List**: Browse a list of available movie genres.
- **Movie List**: View a paginated list of movies based on the selected genre.
- **Movie Details**: See detailed information about a selected movie, including trailers (via YouTube).

## Tech Stack & Architecture

This project is built using the latest modern Android development stack:

- **Language**: [Kotlin](https://kotlinlang.org/)
- **UI Toolkit**: [Jetpack Compose](https://developer.android.com/jetpack/compose) for fully declarative UI.
- **Architecture**: **Clean Architecture** (Data, Domain, Presentation layers) combined with **MVVM** (Model-View-ViewModel).
- **Dependency Injection**: [Hilt](https://dagger.dev/hilt/) for robust dependency injection.
- **Networking**: [Retrofit](https://square.github.io/retrofit/) & [OkHttp](https://square.github.io/okhttp/) for API requests.
- **Pagination**: [Paging 3](https://developer.android.com/topic/libraries/architecture/paging/v3-overview) for efficient data loading and endless scrolling.
- **Image Loading**: [Coil Compose](https://coil-kt.github.io/coil/compose/) for loading and caching images from the network.
- **Navigation**: [Navigation Compose](https://developer.android.com/jetpack/compose/navigation) for type-safe screen transitions.

## Project Structure

The project follows a standard Clean Architecture approach modularized by packages:

- `data`: Contains API services, repositories implementations, data sources, and DTOs.
- `domain`: Contains business logic, use cases, repository interfaces, and models.
- `presentation`: Contains UI components, Jetpack Compose screens, ViewModels, and theming.
  - `genres`: UI for displaying movie genres.
  - `movies`: UI for displaying the paginated list of movies.
  - `detail`: UI for displaying specific movie details.
- `di`: Contains Hilt modules for providing dependencies across the app.

## Requirements
- Minimum SDK: 25
- Target SDK: 35
- Kotlin Compiler Extension (Compose): latest compatible with the project setup.
