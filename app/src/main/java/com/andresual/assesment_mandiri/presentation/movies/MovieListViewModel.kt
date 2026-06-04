package com.andresual.assesment_mandiri.presentation.movies

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.usecase.GetMoviesByGenreUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieListViewModel @Inject constructor(
    private val getMoviesByGenreUseCase: GetMoviesByGenreUseCase,
    savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val genreId: Int = checkNotNull(savedStateHandle["genreId"])

    val movies: Flow<PagingData<Movie>> = getMoviesByGenreUseCase(genreId)
        .cachedIn(viewModelScope)
}
