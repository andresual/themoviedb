package com.andresual.assesment_mandiri.data.remote.dto

import com.andresual.assesment_mandiri.domain.model.Genre
import com.google.gson.annotations.SerializedName

data class GenresResponse(
    @SerializedName("genres") val genres: List<GenreDto>
)

data class GenreDto(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String
) {
    fun toGenre(): Genre {
        return Genre(id = id, name = name)
    }
}
