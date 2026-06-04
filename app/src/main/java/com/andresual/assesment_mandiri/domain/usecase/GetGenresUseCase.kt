package com.andresual.assesment_mandiri.domain.usecase

import com.andresual.assesment_mandiri.domain.model.Genre
import com.andresual.assesment_mandiri.domain.repository.TmdbRepository
import javax.inject.Inject

class GetGenresUseCase @Inject constructor(
    private val repository: TmdbRepository
) {
    suspend operator fun invoke(): Result<List<Genre>> {
        return repository.getGenres()
    }
}
