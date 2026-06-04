package com.andresual.assesment_mandiri.domain.usecase

import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.repository.TmdbRepository
import javax.inject.Inject

class GetMovieDetailUseCase @Inject constructor(
    private val repository: TmdbRepository
) {
    suspend operator fun invoke(movieId: Int): Result<Movie> {
        return repository.getMovieDetail(movieId)
    }
}
