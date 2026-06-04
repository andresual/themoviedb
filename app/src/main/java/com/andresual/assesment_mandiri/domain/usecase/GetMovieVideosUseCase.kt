package com.andresual.assesment_mandiri.domain.usecase

import com.andresual.assesment_mandiri.domain.model.Video
import com.andresual.assesment_mandiri.domain.repository.TmdbRepository
import javax.inject.Inject

class GetMovieVideosUseCase @Inject constructor(
    private val repository: TmdbRepository
) {
    suspend operator fun invoke(movieId: Int): Result<List<Video>> {
        return repository.getMovieVideos(movieId)
    }
}
