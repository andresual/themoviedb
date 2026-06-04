package com.andresual.assesment_mandiri.presentation.detail

import com.andresual.assesment_mandiri.domain.model.Movie
import com.andresual.assesment_mandiri.domain.model.Video

data class MovieDetailData(
    val movie: Movie,
    val trailer: Video?
)
