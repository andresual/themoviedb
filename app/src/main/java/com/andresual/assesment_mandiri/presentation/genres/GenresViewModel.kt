package com.andresual.assesment_mandiri.presentation.genres

import com.andresual.assesment_mandiri.domain.model.Genre
import com.andresual.assesment_mandiri.domain.usecase.GetGenresUseCase
import com.andresual.assesment_mandiri.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import javax.inject.Inject

@HiltViewModel
class GenresViewModel @Inject constructor(
    private val getGenresUseCase: GetGenresUseCase,
    private val repository: com.andresual.assesment_mandiri.domain.repository.TmdbRepository
) : BaseViewModel<List<Genre>>() {

    init {
        loadGenres()
    }

    fun loadGenres() {
        safeLaunch {
            val result = getGenresUseCase()
            handleResult(result, onSuccess = { genres ->
                setSuccess(genres)
                val genresWithImages = coroutineScope {
                    val deferredImages = genres.map { genre ->
                        async {
                            val topMovieResult = repository.getTopMovieForGenre(genre.id)
                            val movie = topMovieResult.getOrNull()
                            val imageUrl = if (movie?.backdropPath != null) {
                                "https://image.tmdb.org/t/p/w780${movie.backdropPath}"
                            } else if (movie?.posterPath != null) {
                                "https://image.tmdb.org/t/p/w780${movie.posterPath}"
                            } else {
                                null
                            }
                            genre.copy(imageUrl = imageUrl)
                        }
                    }
                    deferredImages.awaitAll()
                }

                setSuccess(genresWithImages)
            })
        }
    }
}
