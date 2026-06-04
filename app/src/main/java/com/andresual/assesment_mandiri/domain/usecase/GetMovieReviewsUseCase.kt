package com.andresual.assesment_mandiri.domain.usecase

import androidx.paging.PagingData
import com.andresual.assesment_mandiri.domain.model.Review
import com.andresual.assesment_mandiri.domain.repository.TmdbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMovieReviewsUseCase @Inject constructor(
    private val repository: TmdbRepository
) {
    operator fun invoke(movieId: Int): Flow<PagingData<Review>> {
        return repository.getMovieReviews(movieId)
    }
}
