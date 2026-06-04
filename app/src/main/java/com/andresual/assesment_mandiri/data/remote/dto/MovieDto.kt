package com.andresual.assesment_mandiri.data.remote.dto

import com.andresual.assesment_mandiri.domain.model.Movie
import com.google.gson.annotations.SerializedName

data class MoviesResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<MovieDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class MovieDto(
    @SerializedName("id") val id: Int,
    @SerializedName("title") val title: String,
    @SerializedName("overview") val overview: String?,
    @SerializedName("poster_path") val posterPath: String?,
    @SerializedName("backdrop_path") val backdropPath: String?,
    @SerializedName("vote_average") val voteAverage: Double?,
    @SerializedName("release_date") val releaseDate: String?
) {
    fun toMovie(): Movie {
        return Movie(
            id = id,
            title = title,
            overview = overview ?: "",
            posterPath = posterPath,
            backdropPath = backdropPath,
            voteAverage = voteAverage ?: 0.0,
            releaseDate = releaseDate ?: ""
        )
    }
}
