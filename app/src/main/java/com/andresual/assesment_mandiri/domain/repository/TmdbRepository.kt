package com.andresual.assesment_mandiri.domain.repository

import androidx.paging.PagingData
import com.andresual.assesment_mandiri.domain.model.Genre
import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.model.Review
import com.andresual.assesment_mandiri.domain.model.Video
import kotlinx.coroutines.flow.Flow

interface TmdbRepository {
    suspend fun getGenres(): Result<List<Genre>>
    fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>>
    suspend fun getMovieDetail(movieId: Int): Result<Movie>
    fun getMovieReviews(movieId: Int): Flow<PagingData<Review>>
    suspend fun getMovieVideos(movieId: Int): Result<List<Video>>
    suspend fun getTopMovieForGenre(genreId: Int): Result<Movie?>
}
