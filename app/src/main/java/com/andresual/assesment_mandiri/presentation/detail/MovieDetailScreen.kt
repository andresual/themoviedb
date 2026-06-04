package com.andresual.assesment_mandiri.presentation.detail

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.widget.FrameLayout
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import coil.compose.AsyncImage
import com.andresual.assesment_mandiri.BuildConfig
import com.andresual.assesment_mandiri.R
import com.andresual.assesment_mandiri.domain.model.Review
import com.andresual.assesment_mandiri.presentation.base.UiState
import com.andresual.assesment_mandiri.presentation.components.CineAndreTopBar
import com.andresual.assesment_mandiri.presentation.components.ErrorContent
import com.andresual.assesment_mandiri.presentation.components.GlassSurface
import com.andresual.assesment_mandiri.presentation.components.InfoChip
import com.andresual.assesment_mandiri.presentation.components.LoadingContent
import com.andresual.assesment_mandiri.presentation.components.RatingBadge

fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@OptIn(androidx.compose.animation.ExperimentalSharedTransitionApi::class)
@Composable
fun MovieDetailScreen(
    onNavigateBack: () -> Unit,
    sharedTransitionScope: androidx.compose.animation.SharedTransitionScope,
    animatedVisibilityScope: androidx.compose.animation.AnimatedVisibilityScope,
    viewModel: MovieDetailViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val reviews = viewModel.reviews.collectAsLazyPagingItems()
    val listState = rememberLazyListState()

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background
    ) { padding ->
        Box(modifier = Modifier.fillMaxSize().padding(bottom = padding.calculateBottomPadding())) {
            when (val state = uiState) {
                is UiState.Loading -> LoadingContent()
                is UiState.Error -> ErrorContent(message = state.message)
                is UiState.Success -> {
                    val data = state.data
                    LazyColumn(
                        state = listState,
                        modifier = Modifier.fillMaxSize()
                    ) {
                        item {
                            val scrollOffset = if (listState.firstVisibleItemIndex == 0) {
                                listState.firstVisibleItemScrollOffset.toFloat()
                            } else {
                                1000f
                            }
                            
                            val imageTranslationY = scrollOffset * 0.5f
                            val titleAlpha = (1f - (scrollOffset / 400f)).coerceIn(0f, 1f)
                            
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(400.dp)
                            ) {
                                with(sharedTransitionScope) {
                                    AsyncImage(
                                        model = "${BuildConfig.TMDB_IMAGE_BASE_URL_W1280}${data.movie.backdropPath}",
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .sharedElement(
                                                state = rememberSharedContentState(key = "image-${data.movie.id}"),
                                                animatedVisibilityScope = animatedVisibilityScope
                                            )
                                            .graphicsLayer {
                                                translationY = imageTranslationY
                                            }
                                    )
                                }
                                Box(
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .background(
                                            Brush.verticalGradient(
                                                colors = listOf(
                                                    Color.Transparent,
                                                    MaterialTheme.colorScheme.background.copy(alpha = 0.8f),
                                                    MaterialTheme.colorScheme.background
                                                ),
                                                startY = 200f
                                            )
                                        )
                                )
                                Column(
                                    modifier = Modifier
                                        .align(Alignment.BottomCenter)
                                        .padding(16.dp)
                                        .padding(bottom = 16.dp)
                                        .graphicsLayer {
                                            alpha = titleAlpha
                                            translationY = imageTranslationY * 0.3f
                                        },
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        horizontalArrangement = Arrangement.Center,
                                        modifier = Modifier.padding(bottom = 8.dp)
                                    ) {
                                        RatingBadge(rating = data.movie.voteAverage)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        InfoChip(text = data.movie.releaseDate.take(4))
                                    }
                                    
                                    Text(
                                        text = data.movie.title, 
                                        style = MaterialTheme.typography.displayLarge,
                                        color = Color.White,
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                            
                            Column(modifier = Modifier.padding(16.dp)) {
                                if (data.trailer != null) {
                                    YouTubePlayerEmbedded(
                                        videoId = data.trailer.key,
                                        modifier = Modifier.padding(bottom = 24.dp)
                                    )
                                }

                                GlassSurface(modifier = Modifier.fillMaxWidth()) {
                                    Column(modifier = Modifier.padding(20.dp)) {
                                        Text(
                                            stringResource(R.string.synopsis),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.secondary
                                        )
                                        Spacer(modifier = Modifier.height(12.dp))
                                        Text(
                                            text = data.movie.overview, 
                                            style = MaterialTheme.typography.bodyLarge,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                                
                                Spacer(modifier = Modifier.height(32.dp))
                                Text(
                                    stringResource(R.string.user_reviews), 
                                    style = MaterialTheme.typography.titleLarge, 
                                    color = MaterialTheme.colorScheme.onBackground
                                )
                                Spacer(modifier = Modifier.height(16.dp))
                            }
                        }

                        if (reviews.itemCount == 0 && reviews.loadState.append is LoadState.NotLoading && reviews.loadState.refresh is LoadState.NotLoading) {
                            item {
                                GlassSurface(
                                    alpha = 0.3f,
                                    borderAlpha = 0.05f,
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(horizontal = 16.dp, vertical = 8.dp)
                                ) {
                                    Column(
                                        modifier = Modifier.padding(24.dp),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            text = stringResource(R.string.no_reviews),
                                            style = MaterialTheme.typography.titleMedium,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }
                            }
                        } else {
                            items(reviews.itemCount) { index ->
                                reviews[index]?.let { review ->
                                    ReviewItem(review = review)
                                }
                            }
                        }

                        when (val loadState = reviews.loadState.append) {
                            is LoadState.Loading -> {
                                item {
                                    Box(modifier = Modifier.fillMaxWidth().padding(16.dp), contentAlignment = Alignment.Center) {
                                        CircularProgressIndicator(color = MaterialTheme.colorScheme.secondary)
                                    }
                                }
                            }
                            is LoadState.Error -> {
                                item {
                                    Text(
                                        stringResource(R.string.error_prefix, loadState.error.message ?: ""), 
                                        color = MaterialTheme.colorScheme.error, 
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            else -> {}
                        }
                    }
                }
            }

            // Floating Back Button
            IconButton(
                onClick = onNavigateBack,
                modifier = Modifier
                    .padding(top = WindowInsets.statusBars.asPaddingValues().calculateTopPadding() + 8.dp, start = 16.dp)
                    .background(Color.Black.copy(alpha = 0.3f), RoundedCornerShape(50))
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.cd_back),
                    tint = Color.White
                )
            }
        }
    }
}

@Composable
fun YouTubePlayerEmbedded(videoId: String, modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .aspectRatio(16f / 9f)
            .background(Color.Black)
    ) {
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { context ->
                android.webkit.WebView(context).apply {
                    setBackgroundColor(android.graphics.Color.BLACK)
                    settings.javaScriptEnabled = true
                    settings.mediaPlaybackRequiresUserGesture = false
                    settings.domStorageEnabled = true
                    layoutParams = FrameLayout.LayoutParams(
                        ViewGroup.LayoutParams.MATCH_PARENT,
                        ViewGroup.LayoutParams.MATCH_PARENT
                    )
                    webViewClient = android.webkit.WebViewClient()
                    
                    webChromeClient = object : WebChromeClient() {
                        private var customView: View? = null
                        private var customViewCallback: CustomViewCallback? = null

                        override fun onShowCustomView(view: View?, callback: CustomViewCallback?) {
                            if (customView != null) {
                                callback?.onCustomViewHidden()
                                return
                            }
                            customView = view
                            customViewCallback = callback
                            
                            val activity = context.findActivity() ?: return
                            val decorView = activity.window.decorView as FrameLayout
                            decorView.addView(customView, FrameLayout.LayoutParams(
                                ViewGroup.LayoutParams.MATCH_PARENT,
                                ViewGroup.LayoutParams.MATCH_PARENT
                            ))
                            
                            val windowController = WindowCompat.getInsetsController(activity.window, decorView)
                            windowController.hide(WindowInsetsCompat.Type.systemBars())
                            windowController.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
                        }

                        override fun onHideCustomView() {
                            val activity = context.findActivity() ?: return
                            val decorView = activity.window.decorView as FrameLayout
                            decorView.removeView(customView)
                            customView = null
                            customViewCallback?.onCustomViewHidden()
                            
                            val windowController = WindowCompat.getInsetsController(activity.window, decorView)
                            windowController.show(WindowInsetsCompat.Type.systemBars())
                        }
                    }
                }
            },
            update = { webView ->
                val html = """
                    <!DOCTYPE html>
                    <html>
                        <head>
                            <style>
                                body { margin: 0; padding: 0; background-color: black; }
                                iframe { width: 100%; height: 100%; border: none; }
                            </style>
                        </head>
                        <body>
                            <iframe src="${BuildConfig.YOUTUBE_EMBED_URL}$videoId?autoplay=1&playsinline=1&enablejsapi=1&origin=${BuildConfig.YOUTUBE_ORIGIN_URL}" 
                                    allow="autoplay; fullscreen">
                            </iframe>
                        </body>
                    </html>
                """.trimIndent()
                webView.loadDataWithBaseURL(BuildConfig.YOUTUBE_ORIGIN_URL, html, "text/html", "UTF-8", null)
            }
        )
    }
}

@Composable
fun ReviewItem(review: Review) {
    GlassSurface(
        alpha = 0.5f,
        borderAlpha = 0.05f,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = review.author, 
                style = MaterialTheme.typography.titleMedium,
                color = MaterialTheme.colorScheme.primaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = review.content, 
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }
    }
}
