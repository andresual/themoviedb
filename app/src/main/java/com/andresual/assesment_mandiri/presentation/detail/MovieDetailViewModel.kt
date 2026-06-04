package com.andresual.assesment_mandiri.presentation.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.model.Review
import com.andresual.assesment_mandiri.domain.model.Video
import com.andresual.assesment_mandiri.domain.usecase.GetMovieDetailUseCase
import com.andresual.assesment_mandiri.domain.usecase.GetMovieReviewsUseCase
import com.andresual.assesment_mandiri.domain.usecase.GetMovieVideosUseCase
import com.andresual.assesment_mandiri.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

@HiltViewModel
class MovieDetailViewModel @Inject constructor(
    private val getMovieDetailUseCase: GetMovieDetailUseCase,
    private val getMovieReviewsUseCase: GetMovieReviewsUseCase,
    private val getMovieVideosUseCase: GetMovieVideosUseCase,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<MovieDetailData>() {

    private val movieId: Int = checkNotNull(savedStateHandle["movieId"])

    val reviews: Flow<PagingData<Review>> = getMovieReviewsUseCase(movieId)
        .cachedIn(viewModelScope)

    init {
        loadDetail()
    }

    fun loadDetail() {
        safeLaunch {
            val movieResult = getMovieDetailUseCase(movieId)
            handleResult(movieResult, onSuccess = { movie ->
                val videosResult = getMovieVideosUseCase(movieId)
                val trailer = videosResult.getOrNull()?.find { it.site == "YouTube" && it.type == "Trailer" }
                setSuccess(MovieDetailData(movie, trailer))
            })
        }
    }
}
