package com.andresual.assesment_mandiri.data.remote.dto

import com.andresual.assesment_mandiri.domain.model.Video
import com.google.gson.annotations.SerializedName

data class VideosResponse(
    @SerializedName("id") val id: Int,
    @SerializedName("results") val results: List<VideoDto>
)

data class VideoDto(
    @SerializedName("id") val id: String,
    @SerializedName("key") val key: String,
    @SerializedName("name") val name: String,
    @SerializedName("site") val site: String,
    @SerializedName("type") val type: String
) {
    fun toVideo(): Video {
        return Video(
            id = id,
            key = key,
            name = name,
            site = site,
            type = type
        )
    }
}
