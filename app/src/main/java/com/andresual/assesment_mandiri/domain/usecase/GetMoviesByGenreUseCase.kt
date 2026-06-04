package com.andresual.assesment_mandiri.domain.usecase

import androidx.paging.PagingData
import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.repository.TmdbRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetMoviesByGenreUseCase @Inject constructor(
    private val repository: TmdbRepository
) {
    operator fun invoke(genreId: Int): Flow<PagingData<Movie>> {
        return repository.getMoviesByGenre(genreId)
    }
}
