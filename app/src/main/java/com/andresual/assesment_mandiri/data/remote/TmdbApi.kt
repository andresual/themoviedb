package com.andresual.assesment_mandiri.data.remote

import com.andresual.assesment_mandiri.data.remote.dto.GenresResponse
import com.andresual.assesment_mandiri.data.remote.dto.MovieDto
import com.andresual.assesment_mandiri.data.remote.dto.MoviesResponse
import com.andresual.assesment_mandiri.data.remote.dto.ReviewsResponse
import com.andresual.assesment_mandiri.data.remote.dto.VideosResponse
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface TmdbApi {

    @GET("3/genre/movie/list")
    suspend fun getGenres(): GenresResponse

    @GET("3/discover/movie")
    suspend fun getMoviesByGenre(
        @Query("with_genres") genreId: Int,
        @Query("page") page: Int
    ): MoviesResponse

    @GET("3/movie/{movie_id}")
    suspend fun getMovieDetail(
        @Path("movie_id") movieId: Int
    ): MovieDto

    @GET("3/movie/{movie_id}/reviews")
    suspend fun getMovieReviews(
        @Path("movie_id") movieId: Int,
        @Query("page") page: Int
    ): ReviewsResponse

    @GET("3/movie/{movie_id}/videos")
    suspend fun getMovieVideos(
        @Path("movie_id") movieId: Int
    ): VideosResponse


}
