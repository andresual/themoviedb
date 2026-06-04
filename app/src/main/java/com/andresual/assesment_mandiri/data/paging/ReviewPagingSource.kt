package com.andresual.assesment_mandiri.data.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.andresual.assesment_mandiri.data.remote.TmdbApi
import com.andresual.assesment_mandiri.domain.model.Review

class ReviewPagingSource(
    private val api: TmdbApi,
    private val movieId: Int
) : PagingSource<Int, Review>() {

    override fun getRefreshKey(state: PagingState<Int, Review>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            state.closestPageToPosition(anchorPosition)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(anchorPosition)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Review> {
        return try {
            val page = params.key ?: 1
            val response = api.getMovieReviews(movieId = movieId, page = page)
            val reviews = response.results.map { it.toReview() }
            
            LoadResult.Page(
                data = reviews,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (reviews.isEmpty() || page >= response.totalPages) null else page + 1
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}
