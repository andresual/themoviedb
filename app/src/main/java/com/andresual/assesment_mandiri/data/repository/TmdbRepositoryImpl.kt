package com.andresual.assesment_mandiri.data.repository

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import com.andresual.assesment_mandiri.data.paging.MoviePagingSource
import com.andresual.assesment_mandiri.data.paging.ReviewPagingSource
import com.andresual.assesment_mandiri.data.remote.TmdbApi
import com.andresual.assesment_mandiri.domain.model.Genre
import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.model.Review
import com.andresual.assesment_mandiri.domain.model.Video
import com.andresual.assesment_mandiri.domain.repository.TmdbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class TmdbRepositoryImpl @Inject constructor(
    private val api: TmdbApi
) : TmdbRepository {

    override suspend fun getGenres(): Result<List<Genre>> {
        return try {
            val response = api.getGenres()
            Result.success(response.genres.map { it.toGenre() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMoviesByGenre(genreId: Int): Flow<PagingData<Movie>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { MoviePagingSource(api, genreId) }
        ).flow
    }

    override suspend fun getMovieDetail(movieId: Int): Result<Movie> {
        return try {
            val response = api.getMovieDetail(movieId)
            Result.success(response.toMovie())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun getMovieReviews(movieId: Int): Flow<PagingData<Review>> {
        return Pager(
            config = PagingConfig(pageSize = 20, enablePlaceholders = false),
            pagingSourceFactory = { ReviewPagingSource(api, movieId) }
        ).flow
    }

    override suspend fun getMovieVideos(movieId: Int): Result<List<Video>> {
        return try {
            val response = api.getMovieVideos(movieId)
            Result.success(response.results.map { it.toVideo() })
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getTopMovieForGenre(genreId: Int): Result<Movie?> {
        return try {
            val response = api.getMoviesByGenre(genreId, 1)
            val firstMovie = response.results.firstOrNull()?.toMovie()
            Result.success(firstMovie)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
