package com.andresual.assesment_mandiri.presentation.genres

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage
import com.andresual.assesment_mandiri.BuildConfig
import com.andresual.assesment_mandiri.R
import com.andresual.assesment_mandiri.domain.model.Genre
import com.andresual.assesment_mandiri.presentation.base.UiState
import com.andresual.assesment_mandiri.presentation.components.CineAndreTopBar
import com.andresual.assesment_mandiri.presentation.components.ErrorContent
import com.andresual.assesment_mandiri.presentation.components.LoadingContent

@Composable
fun GenresScreen(
    onGenreClick: (Int, String) -> Unit,
    viewModel: GenresViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(
        topBar = { CineAndreTopBar() },
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = Alignment.Center
        ) {
            when (val state = uiState) {
                is UiState.Loading -> LoadingContent()
                is UiState.Error -> ErrorContent(
                    message = state.message,
                    onRetry = { viewModel.loadGenres() }
                )
                is UiState.Success -> {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        contentPadding = PaddingValues(16.dp),
                        horizontalArrangement = Arrangement.spacedBy(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item(span = { GridItemSpan(2) }) {
                            HeroFeaturedGenre()
                        }
                        
                        item(span = { GridItemSpan(2) }) {
                            Text(
                                text = stringResource(R.string.browse_genres),
                                style = MaterialTheme.typography.titleMedium,
                                color = MaterialTheme.colorScheme.onBackground,
                                modifier = Modifier.padding(bottom = 8.dp, top = 16.dp)
                            )
                        }
                        
                        itemsIndexed(state.data, span = { index, _ -> 
                            if (index % 5 == 2) GridItemSpan(2) else GridItemSpan(1)
                        }) { index, genre ->
                            val isWide = index % 5 == 2
                            GenreCard(
                                genre = genre, 
                                isWide = isWide,
                                onClick = { onGenreClick(genre.id, genre.name) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun HeroFeaturedGenre() {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surfaceContainerLowest)
    ) {
        AsyncImage(
            model = BuildConfig.FEATURED_GENRE_IMAGE_URL,
            contentDescription = stringResource(R.string.cd_featured),
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0.9f))
                    )
                )
        )
        Column(
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.featured_genre_label),
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Text(
                text = stringResource(R.string.featured_genre_title),
                style = MaterialTheme.typography.displayLarge,
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Composable
fun GenreCard(genre: Genre, isWide: Boolean, onClick: () -> Unit) {
    val aspectRatio = if (isWide) 2f else 0.75f
    val imageUrl = genre.imageUrl ?: "${BuildConfig.PICSUM_BASE_URL}${genre.id}/400/600"
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceContainer)
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            AsyncImage(
                model = imageUrl,
                contentDescription = genre.name,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, MaterialTheme.colorScheme.background.copy(alpha = 0.8f))
                        )
                    )
            )
            Column(
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp)
            ) {
                Text(
                    text = genre.name,
                    style = if (isWide) MaterialTheme.typography.headlineLarge else MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
    }
}
