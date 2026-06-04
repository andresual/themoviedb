package com.andresual.assesment_mandiri.data.remote.dto

import com.andresual.assesment_mandiri.domain.model.Review
import com.google.gson.annotations.SerializedName

data class ReviewsResponse(
    @SerializedName("page") val page: Int,
    @SerializedName("results") val results: List<ReviewDto>,
    @SerializedName("total_pages") val totalPages: Int,
    @SerializedName("total_results") val totalResults: Int
)

data class ReviewDto(
    @SerializedName("id") val id: String,
    @SerializedName("author") val author: String,
    @SerializedName("content") val content: String,
    @SerializedName("created_at") val createdAt: String?
) {
    fun toReview(): Review {
        return Review(
            id = id,
            author = author,
            content = content,
            createdAt = createdAt ?: ""
        )
    }
}
