package com.andresual.assesment_mandiri

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.andresual.assesment_mandiri.presentation.detail.MovieDetailScreen
import com.andresual.assesment_mandiri.presentation.genres.GenresScreen
import com.andresual.assesment_mandiri.presentation.movies.MovieListScreen
import com.andresual.assesment_mandiri.ui.theme.Assesment_mandiriTheme
import dagger.hilt.android.AndroidEntryPoint
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout

@OptIn(ExperimentalSharedTransitionApi::class)
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Assesment_mandiriTheme {
                Surface(color = MaterialTheme.colorScheme.background) {
                    val navController = rememberNavController()

                    SharedTransitionLayout {
                        NavHost(navController = navController, startDestination = "genres") {
                            composable("genres") {
                                GenresScreen(
                                    onGenreClick = { genreId, genreName ->
                                        navController.navigate("movies/$genreId/$genreName")
                                    }
                                )
                            }
                            
                            composable(
                                route = "movies/{genreId}/{genreName}",
                                arguments = listOf(
                                    navArgument("genreId") { type = NavType.IntType },
                                    navArgument("genreName") { type = NavType.StringType }
                                )
                            ) { backStackEntry ->
                                val genreName = backStackEntry.arguments?.getString("genreName") ?: ""
                                MovieListScreen(
                                    genreName = genreName,
                                    onNavigateBack = { navController.popBackStack() },
                                    onMovieClick = { movieId ->
                                        navController.navigate("detail/$movieId")
                                    },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable
                                )
                            }

                            composable(
                                route = "detail/{movieId}",
                                arguments = listOf(navArgument("movieId") { type = NavType.IntType })
                            ) {
                                MovieDetailScreen(
                                    onNavigateBack = { navController.popBackStack() },
                                    sharedTransitionScope = this@SharedTransitionLayout,
                                    animatedVisibilityScope = this@composable
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}