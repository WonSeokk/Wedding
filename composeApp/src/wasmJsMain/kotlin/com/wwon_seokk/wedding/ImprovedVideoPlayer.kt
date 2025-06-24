package com.wwon_seokk.wedding

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.zIndex
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface
import kotlinx.browser.document
import kotlinx.coroutines.delay
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.NodeList
import org.w3c.dom.get

private val ktorFactory by lazy { KtorNetworkFetcherFactory() }

@Composable
fun ImprovedVideoPlayer(
    thumb: String,
    playerState: VideoPlayerState
) {
    var showThumbnail by remember { mutableStateOf(true) }
    
    // 썸네일을 2초간 표시 후 비디오로 전환 (깜빡거림 방지)
    LaunchedEffect(Unit) {
        delay(800)
        showThumbnail = false
    }
    
    Box(modifier = Modifier.fillMaxSize()) {
        // 비디오 플레이어 (항상 백그라운드에서 로드)
        VideoPlayerSurface(
            modifier = Modifier.fillMaxSize(),
            playerState = playerState,
            contentScale = ContentScale.Crop
        ) {
            LaunchedEffect(Unit) {
                val documentVideos: NodeList = document.querySelectorAll("video")
                for (i in 0 until documentVideos.length) {
                    val video = documentVideos[i] as HTMLVideoElement
                    video.muted = true
                }
            }
        }
        
        // 썸네일 오버레이 (깜빡거림 방지)
        if (showThumbnail) {
            AsyncImage(
                modifier = Modifier
                    .fillMaxSize()
                    .zIndex(1f),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("${CDN_BASE_URL}asset/${thumb}.jpg")
                    .memoryCacheKey(thumb)
                    .diskCacheKey(thumb)
                    .fetcherFactory(ktorFactory)
                    .build(),
                placeholder = ColorPainter(color = Color.LightGray.copy(alpha = .3f)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
        }
    }
}
