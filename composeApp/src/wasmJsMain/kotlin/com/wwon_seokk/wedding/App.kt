package com.wwon_seokk.wedding

import AnimatedBox
import DigitCountText
import Flippable
import Media
import Media.MediaType
import RemainTime
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedContentScope
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.ExperimentalSharedTransitionApi
import androidx.compose.animation.SharedTransitionLayout
import androidx.compose.animation.SharedTransitionScope
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideIn
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.ripple.RippleAlpha
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalAbsoluteTonalElevation
import androidx.compose.material3.LocalRippleConfiguration
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RippleConfiguration
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.TileMode
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.ColorPainter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.util.lerp
import androidx.compose.ui.zIndex
import coil3.ImageLoader
import coil3.compose.AsyncImage
import coil3.compose.LocalPlatformContext
import coil3.network.ktor3.KtorNetworkFetcherFactory
import coil3.request.ImageRequest
import coil3.request.maxBitmapSize
import coil3.size.Size
import getRemainTime
import io.github.kdroidfilter.composemediaplayer.VideoPlayerState
import io.github.kdroidfilter.composemediaplayer.VideoPlayerSurface
import io.github.kdroidfilter.composemediaplayer.rememberVideoPlayerState
import isMobileDevice
import kotlin.math.absoluteValue
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.internal.JSJoda.YearMonth
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.w3c.dom.HTMLDivElement
import org.w3c.dom.HTMLVideoElement
import org.w3c.dom.NodeList
import org.w3c.dom.get
import pxToDp
import rememberFlipController
import wedding.composeapp.generated.resources.Res
import wedding.composeapp.generated.resources.content_background
import wedding.composeapp.generated.resources.cover_background
import wedding.composeapp.generated.resources.heart
import wedding.composeapp.generated.resources.kakao_icon
import wedding.composeapp.generated.resources.navermap_icon
import wedding.composeapp.generated.resources.tmap_icon


@JsName("initNaverMap")
external fun initNaverMap(elementId: String, lat: Double, lng: Double, zoom: Int)

@JsName("showNaverMap")
external fun showNaverMap(elementId: String, show: Boolean, positionY: Float)

@JsName("registerMapBox")
external fun registerMapBox(elementId: String)

@JsName("hideInitialLoading")
external fun hideInitialLoading()

const val weddingLat = 37.481504867692
const val weddingLng = 126.79853505353
const val zoomLevel = 16

private val ktorFactory by lazy { KtorNetworkFetcherFactory() }

val medias = listOf(
    Media(key = 1, type = MediaType.IMAGE, fileName = "image1"),
    Media(key = 2, type = MediaType.IMAGE, fileName = "image2"),
    Media(key = 3, type = MediaType.IMAGE, fileName = "image3"),
    Media(key = 4, type = MediaType.IMAGE, fileName = "image4"),
    Media(key = 6, type = MediaType.IMAGE, fileName = "image5"),
    Media(key = 7, type = MediaType.IMAGE, fileName = "image6"),
    Media(key = 8, type = MediaType.IMAGE, fileName = "image7"),
    Media(key = 9, type = MediaType.IMAGE, fileName = "image8"),
    Media(key = 11, type = MediaType.IMAGE, fileName = "image9"),
    Media(key = 12, type = MediaType.IMAGE, fileName = "image10"),
    Media(key = 13, type = MediaType.IMAGE, fileName = "image11"),
    Media(key = 14, type = MediaType.IMAGE, fileName = "image12"),
    Media(key = 16, type = MediaType.IMAGE, fileName = "image13"),
    Media(key = 17, type = MediaType.IMAGE, fileName = "image14"),
    Media(key = 18, type = MediaType.IMAGE, fileName = "image15"),
    Media(key = 19, type = MediaType.IMAGE, fileName = "image16"),
    Media(key = 21, type = MediaType.IMAGE, fileName = "image17"),
    Media(key = 22, type = MediaType.IMAGE, fileName = "image18"),
    Media(key = 23, type = MediaType.IMAGE, fileName = "image19"),
    Media(key = 24, type = MediaType.IMAGE, fileName = "image20"),
    Media(key = 26, type = MediaType.IMAGE, fileName = "image21"),
    Media(key = 27, type = MediaType.IMAGE, fileName = "image22"),
    Media(key = 28, type = MediaType.IMAGE, fileName = "image23"),
    Media(key = 29, type = MediaType.IMAGE, fileName = "image24"),
    Media(key = 1, type = MediaType.VIDEO, fileName = "video_content1", thumb = "video_thumb1"),
    Media(key = 6, type = MediaType.VIDEO, fileName = "video_content2", thumb = "video_thumb2"),
    Media(key = 3, type = MediaType.VIDEO, fileName = "video_content3", thumb = "video_thumb3"),
    Media(key = 4, type = MediaType.VIDEO, fileName = "video_content4", thumb = "video_thumb4"),
    Media(key = 5, type = MediaType.VIDEO, fileName = "video_content5", thumb = "video_thumb5"),
    Media(key = 2, type = MediaType.VIDEO, fileName = "video_content6", thumb = "video_thumb6"),
)

val imageMedias = medias.filter { it.type == MediaType.IMAGE }
val videoMedias = medias.filter { it.type == MediaType.VIDEO }

@OptIn(ExperimentalResourceApi::class, ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class, ExperimentalFoundationApi::class,
    ExperimentalSharedTransitionApi::class
)
@Composable
fun App() {
    MaterialTheme {
        val context = LocalPlatformContext.current
        var heartCount by remember { mutableIntStateOf(0) }
        var blessingCount by remember { mutableIntStateOf(0) }
        var isImageLoaded by remember { mutableStateOf(false) }
        var isAppReady by remember { mutableStateOf(false) }
        val coverImageRequest = remember {
            ImageRequest.Builder(context)
                .data(Res.getUri("/drawable/cover.png"))
                .diskCacheKey("cover")
                .fetcherFactory(ktorFactory)
                .build()
        }
        val mainImageRequest = remember {
            ImageRequest.Builder(context)
                .data("${window.location.href}/asset/image1_org.jpg")
                .diskCacheKey("image1_org")
                .fetcherFactory(ktorFactory)
                .listener(onSuccess = { _, result -> isImageLoaded = true })
                .build()
        }
        var loaded by remember { mutableIntStateOf(0) }
        LaunchedEffect(Unit) {
            registerMapBox("map-box")
            initNaverMap("map-container", weddingLat, weddingLng, zoomLevel)
            // 백그라운드에서 이미지 로딩
            ImageLoader(context).execute(coverImageRequest)
            ImageLoader(context).execute(mainImageRequest)
            launch(Dispatchers.Unconfined) {
                heartCount = getLikeCount("heart").await<JsNumber>().toInt()
                blessingCount = getLikeCount("blessing").await<JsNumber>().toInt()
                incrementDailyVisit()
                medias.forEach { media ->
                    val url = when(media.type) {
                        MediaType.IMAGE -> "${window.location.href}/asset/${media.fileName}.jpg"
                        MediaType.VIDEO -> "${window.location.href}/asset/${media.thumb}.jpg"
                    }
                    val request = ImageRequest.Builder(context)
                        .data(url)
                        .memoryCacheKey(media.thumb)
                        .diskCacheKey(media.thumb)
                        .fetcherFactory(ktorFactory)
                        .listener(onSuccess = { _, result ->
                            loaded++
                            println("test :: $loaded")
                            if(loaded == medias.size)
                                isAppReady = true
                        })
                        .build()
                    ImageLoader(context).execute(request)
                }
            }
        }
        
        val dimension = remember { mutableFloatStateOf(2f) }
        val flipController = rememberFlipController()
        var isFront by remember { mutableStateOf(false) }
        var isCoverBackground by remember { mutableStateOf(true) }

        LaunchedEffect(isAppReady) {
            if(isAppReady) {
                hideInitialLoading()
                delay(500)
                isFront = true
            }
        }

        LaunchedEffect(isImageLoaded, isAppReady) {
            if(isImageLoaded && isAppReady) {
                delay(4300)
                flipController.flipToBack()
                isFront = false
                isCoverBackground = false
            }
        }

        CompositionLocalProvider(
            LocalAbsoluteTonalElevation provides 0.dp,
            LocalRippleConfiguration provides RippleConfiguration(color = Color.Unspecified, rippleAlpha = RippleAlpha(0.0f,0.0f,0.0f,0.0f))
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .onSizeChanged {
                        dimension.floatValue = it.width / window.innerWidth.toFloat()
                    },
                contentAlignment = Alignment.Center
            ) {
                AnimatedVisibility(
                    visible = isCoverBackground,
                    enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 1500))
                ) {
                    Box {
                        Image(
                            modifier = Modifier
                                .zIndex(1f)
                                .fillMaxSize(),
                            painter = painterResource(Res.drawable.cover_background),
                            contentScale = ContentScale.FillHeight,
                            contentDescription = null
                        )
                        val playerState = rememberVideoPlayerState()
                        LaunchedEffect(Unit) {
                            playerState.openUri("${window.location.href}/asset/snow.mp4")
                            playerState.volume = 0f
                            playerState.loop = true
                        }
                        VideoPlayerSurface(
                            modifier = Modifier
                                .width(550.dp)
                                .align(alignment = Alignment.Center)
                                .fillMaxHeight(),
                            playerState = playerState,
                            contentScale = ContentScale.Crop
                        ) {
                            LaunchedEffect(Unit) {
                                val documentVideos: NodeList = document.querySelectorAll("video")
                                for (i in 0 until documentVideos.length) {
                                    val video = documentVideos[i] as HTMLVideoElement
                                    video.style.cssText = "position: absolute; mix-blend-mode: screen; background-color: black; margin: 0px; left: 0px; top: 0px; object-fit: fill;"
                                    video.muted = true
                                }
                            }
                        }
                    }
                }
                AnimatedVisibility(
                    visible = isCoverBackground.not(),
                    enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
                    exit = fadeOut(animationSpec = tween(durationMillis = 1500))
                ) {
                    Image(
                        modifier = Modifier.fillMaxSize(),
                        painter = painterResource(Res.drawable.content_background),
                        contentScale = ContentScale.FillHeight,
                        contentDescription = null
                    )
                }
                Flippable(
                    modifier = Modifier
                        .width(400.dp)
                        .fillMaxHeight()
                        .align(Alignment.Center),
                    autoFlip = false,
                    frontSide = {
                        Cover(isFront = isFront)
                    },
                    backSide = {
                        var showDetails by remember { mutableStateOf(false) }
                        var detailKey by remember { mutableStateOf("") }
                        val listState = rememberLazyListState()
                        SharedTransitionLayout {
                            AnimatedContent(showDetails) {
                                if(!it)
                                    Content(
                                        heartCount = heartCount,
                                        blessingCount = blessingCount,
                                        listState = listState,
                                        sharedTransitionScope = this@SharedTransitionLayout,
                                        animatedContentScope = this@AnimatedContent,
                                        detailKey = detailKey,
                                        dimension = dimension
                                    ) {
                                        showNaverMap("map-container", false, 0f)
                                        detailKey = it
                                        showDetails = true
                                    }
                                if(it) {
                                    val media = imageMedias.find { it.fileName == detailKey }
                                    val index = imageMedias.indexOf(media)
                                    val horizontalState = rememberPagerState(index) { imageMedias.size }
                                    LaunchedEffect(horizontalState.currentPage) {
                                        val page = horizontalState.currentPage
                                        detailKey = imageMedias[page].fileName
                                        println(detailKey)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .background(Color.Transparent),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Column {
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(8.dp)
                                            ) {
                                                Text(
                                                    modifier = Modifier
                                                        .align(Alignment.Center)
                                                        .sharedBounds(
                                                            sharedContentState = rememberSharedContentState("gallery"),
                                                            animatedVisibilityScope = this@AnimatedContent,
                                                            enter = fadeIn(),
                                                            exit = fadeOut(),
                                                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                                        ),
                                                    text = "Gallery",
                                                    style = enFontFamily.bodyLarge,
                                                    fontSize = 32.sp,
                                                    color = Color(0xFFB76E79),
                                                    textAlign = TextAlign.Center
                                                )
                                                Icon(
                                                    modifier = Modifier
                                                        .clickable { showDetails = false }
                                                        .size(28.dp)
                                                        .align(Alignment.CenterEnd),
                                                    imageVector = Icons.Default.Close,
                                                    contentDescription = null,
                                                    tint = Color(0xFF574B40)
                                                )
                                            }
                                            HorizontalPager(
                                                modifier = Modifier
                                                    .weight(.6f, false)
                                                    .widthIn(max = 420.dp),
                                                state = horizontalState,
                                                pageSpacing = 1.dp,
                                                beyondViewportPageCount = 3,
                                                contentPadding = PaddingValues(horizontal = 24.dp)
                                            ) { page ->
                                                Box(
                                                    modifier = Modifier
                                                        .graphicsLayer {
                                                            val pageOffset = horizontalState.currentPage - page + horizontalState.currentPageOffsetFraction
                                                            alpha = lerp(
                                                                start = .5f,
                                                                stop = 1f,
                                                                fraction = 1f - pageOffset.absoluteValue.coerceIn(0f, 1f),
                                                            )
                                                            lerp(
                                                                start = 1f,
                                                                stop = .7f,
                                                                fraction = pageOffset.absoluteValue.coerceIn(0f, 1f),
                                                            ).let {
                                                                scaleX = it
                                                                scaleY = it
                                                                val sign = if (pageOffset > 0) 1 else -1
                                                                translationX = sign * size.width * (1 - it) / 2
                                                            }
                                                            val blur = (pageOffset * 20f).coerceAtLeast(.1f)
                                                            renderEffect = BlurEffect(blur, blur, TileMode.Decal)
                                                        }
                                                        .background(color = Color.Transparent, shape = RoundedCornerShape(20.dp))
                                                        .clip(RoundedCornerShape(20.dp)),
                                                    contentAlignment = Alignment.Center,
                                                ) {
                                                    val media = imageMedias[page]
                                                    AsyncImage(
                                                        modifier = Modifier
                                                            .background(color = Color.Transparent, shape = RoundedCornerShape(20.dp))
                                                            .clip(RoundedCornerShape(20.dp))
                                                            .heightIn(max = 500.dp)
                                                            .then(
                                                                if(detailKey == media.fileName)
                                                                    Modifier.sharedBounds(
                                                                        sharedContentState = rememberSharedContentState(media.fileName),
                                                                        animatedVisibilityScope = this@AnimatedContent,
                                                                        enter = fadeIn(),
                                                                        exit = fadeOut(),
                                                                        resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                                                    )
                                                                else
                                                                    Modifier
                                                            ),
                                                        model = ImageRequest.Builder(LocalPlatformContext.current)
                                                            .data("${window.location.href}/asset/${media.fileName}_org.jpg")
                                                            .memoryCacheKey("${media.fileName}_org")
                                                            .diskCacheKey("${media.fileName}_org")
                                                            .maxBitmapSize(Size.ORIGINAL)
                                                            .fetcherFactory(ktorFactory)
                                                            .build(),
                                                        placeholder = ColorPainter(color = Color.LightGray.copy(alpha = .4f)),
                                                        contentDescription = null,
                                                        contentScale = ContentScale.Fit
                                                    )
                                                }
                                            }
                                            Box(
                                                modifier = Modifier
                                                    .fillMaxWidth()
                                                    .padding(16.dp)
                                            ) {
                                                DigitCountText(
                                                    modifier = Modifier.align(Alignment.Center),
                                                    backText = "/${imageMedias.size}",
                                                    count = horizontalState.currentPage + 1,
                                                    textColor = Color(0xFF574B40),
                                                    fontSize = 20,
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }

                    },
                    flipController = flipController
                )
            }
        }
    }
}

@Composable
private fun Cover(
    modifier: Modifier = Modifier,
    isFront: Boolean,
) {
    Box(modifier = modifier) {
        AsyncImage(
            model = ImageRequest.Builder(LocalPlatformContext.current)
                .data(Res.getUri("/drawable/cover.png"))
                .diskCacheKey("cover")
                .fetcherFactory(ktorFactory)
                .build(),
            contentDescription = null
        )
        SvgAnimationContainer(modifier = Modifier.matchParentSize(), isFront = isFront)
    }
}

@OptIn(ExperimentalResourceApi::class, ExperimentalSharedTransitionApi::class)
@Composable
private fun Content(
    heartCount: Int,
    blessingCount: Int,
    listState: LazyListState,
    sharedTransitionScope: SharedTransitionScope,
    animatedContentScope: AnimatedContentScope,
    dimension: MutableFloatState,
    detailKey: String,
    showDetail: (String) -> Unit,
) {
    val playerStates = (1..videoMedias.size).map { i ->
        val state = rememberVideoPlayerState()
        LaunchedEffect(i) {
            val filename = videoMedias.find { it.key == i }?.fileName
            state.openUri("${window.location.href}/asset/${filename}.mp4")
            state.volume = 0f
            state.loop = true
        }
        state
    }

    val timeMillis = remember {
        val date = LocalDateTime.parse("2025-08-30T08:30:00")
        val instant = date.toInstant(TimeZone.UTC)
        instant.toEpochMilliseconds()
    }
    val dayMillis = remember {
        val date = LocalDateTime.parse("2025-08-29T15:00:00")
        val instant = date.toInstant(TimeZone.UTC)
        instant.toEpochMilliseconds()
    }
    var remainDay: RemainTime by remember { mutableStateOf(RemainTime())}
    var remainTime: RemainTime by remember { mutableStateOf(RemainTime())}
    LaunchedEffect(Unit) {
        launch(Dispatchers.Default) {
            while(true) {
                remainTime = timeMillis.getRemainTime()
                remainDay = dayMillis.getRemainTime()
                delay(1000L)
            }
        }
    }
    val hour = remember(remainTime.hour) {
        val (first, second) = remainTime.hour.toList().let {
            if(it.size > 2)
                it.first() to it.filterIndexed { index, _ -> index != 0 }.joinToString("").toInt()
            else
                it.first() to it.last().digitToInt()
        }
        if(first == '0')
            first.toString() to second
        else
            "" to remainTime.hour.toInt()
    }
    val min = remember(remainTime.min) {
        val (first, second) = remainTime.min.toList().let { it.first() to it.last() }
        if(first == '0')
            first.toString() to second.digitToInt()
        else
            "" to remainTime.min.toInt()
    }
    val sec = remember(remainTime.sec) {
        val (first, second) = remainTime.sec.toList().let { it.first() to it.last() }
        if(first == '0')
            first.toString() to second.digitToInt()
        else
            "" to remainTime.sec.toInt()
    }

    val mapPositionY by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.find { it.key == "map" }?.let {
                it.offset / dimension.floatValue
            }
        }
    }
    var positionY by remember { mutableStateOf(0f) }
    LaunchedEffect(mapPositionY) {
        mapPositionY?.let { y ->
            if(y.toFloat() != positionY) {
                positionY = y.toFloat()
                showNaverMap("map-container", true, positionY)
            }
        } ?: run {
            showNaverMap("map-container", false, 0f)
        }
    }


    LazyColumn (
        modifier = Modifier.fillMaxSize(),
        state = listState,
        content = {
            item(
                key = "main"
            ) {
                Box(
                    modifier = Modifier.heightIn(min = 400.dp)
                ) {
                    AsyncImage(
                        modifier = Modifier.fillMaxWidth(),
                        model = ImageRequest.Builder(LocalPlatformContext.current)
                            .data("${window.location.href}/asset/image1_org.jpg")
                            .diskCacheKey("image1_org")
                            .fetcherFactory(ktorFactory)
                            .build(),
                        placeholder = ColorPainter(color = Color.LightGray.copy(alpha = .4f)),
                        contentDescription = null,
                        contentScale = ContentScale.Fit
                    )
                }
            }
            item(
                key = "main_text"
            ) {
                AnimatedBox {
                    Column(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(top = 60.dp, bottom = 12.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .padding(horizontal = 40.dp)
                                .fillMaxWidth(),
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Text(
                                text = "Invitation",
                                style = enFontFamily.bodyLarge,
                                fontSize = 32.sp,
                                color = Color(0xFFB76E79),
                                textAlign = TextAlign.Center
                            )
                        }
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "다른 공간, 다른 시간을 걷던 두 사람이\n서로를 마주한 이후 같은 공간, 같은 시간을\n꿈꾸며 걷게 되었습니다",
                            style = fontFamily.bodyLarge,
                            fontSize = 16.sp,
                            color = Color(0xFF574B40),
                            lineHeight = 30.sp,
                            textAlign = TextAlign.Center
                        )
                        Spacer( modifier = Modifier.size(36.dp))
                        Text(
                            modifier = Modifier.fillMaxWidth(),
                            text = "따뜻한 축복으로 저희 두 사람이\n함께하는 첫 걸음을 더욱 빛내주세요",
                            style = fontFamily.bodyLarge,
                            fontSize = 16.sp,
                            color = Color(0xFF574B40),
                            lineHeight = 30.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            item(key = "contact") {
                AnimatedBox {
                    Column(
                        modifier = Modifier
                            .padding(top = 16.dp, bottom = 32.dp)
                            .fillMaxWidth(),
                        verticalArrangement = Arrangement.spacedBy(8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 24.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "최상범·이혜정",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Color(0xFF574B40)
                                )
                                Text(
                                    text = "의 아들",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 15.sp,
                                    color = Color(0xFF574B40)
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = "원석",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Color(0xFF574B40)
                                )
                            }

                            Image(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { window.location.href = "tel://010-6778-2939" },
                                imageVector = Icons.Filled.Call,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFFB76E79))
                            )
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                        ) {
                            Row(verticalAlignment = Alignment.Bottom) {
                                Text(
                                    text = "윤태욱·박성숙",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Color(0xFF574B40)
                                )
                                Text(
                                    text = "의   딸 ",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 15.sp,
                                    color = Color(0xFF574B40)
                                )
                                Text(
                                    modifier = Modifier.padding(start = 4.dp),
                                    text = "서영",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Color(0xFF574B40)
                                )
                            }
                            Image(
                                modifier = Modifier
                                    .size(20.dp)
                                    .clickable { window.location.href = "tel://010-9264-7479" },
                                imageVector = Icons.Filled.Call,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFFB76E79))
                            )
                        }

                        var isParentContactOpened by remember { mutableStateOf(false) }
                        val stateTransition = updateTransition(targetState = isParentContactOpened, label = "")
                        val rotation: Float by stateTransition.animateFloat(
                            transitionSpec = {
                                if (isParentContactOpened) {
                                    spring(stiffness = Spring.StiffnessLow)
                                } else {
                                    spring(stiffness = Spring.StiffnessMedium)
                                }
                            },
                            label = ""
                        ) { openState ->
                            if (openState) 180f else 0f
                        }
                        Column(
                            modifier = Modifier.padding(top = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                modifier = Modifier.clickable { isParentContactOpened = !isParentContactOpened },
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "혼주에게 연락하기",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 15.sp,
                                    color = Color(0xFF574B40)
                                )
                                Image(
                                    modifier = Modifier
                                        .rotate(rotation)
                                        .size(21.dp),
                                    imageVector = Icons.Filled.ArrowDropDown,
                                    contentDescription = null,
                                    colorFilter = ColorFilter.tint(Color(0xFF574B40))
                                )
                            }
                            AnimatedVisibility(
                                visible = isParentContactOpened,
                                enter = slideIn(animationSpec = tween(durationMillis = 500)) { IntOffset(0, -80) }
                            ) {
                                Row(
                                    modifier = Modifier,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            text = "신랑측",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF5f8b9b)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.Bottom,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "아버지",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                                Text(
                                                    text = "최상범",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                            }
                                            Image(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clickable { window.location.href = "tel://010-3627-2939" },
                                                imageVector = Icons.Filled.Call,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(Color(0xFF5f8b9b))
                                            )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.Bottom,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "어머니",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                                Text(
                                                    text = "이혜정",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                            }
                                            Image(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clickable { window.location.href = "tel://010-5696-2939" },
                                                imageVector = Icons.Filled.Call,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(Color(0xFF5f8b9b))
                                            )
                                        }
                                    }
                                    Column(
                                        modifier = Modifier.weight(1f),
                                        horizontalAlignment = Alignment.CenterHorizontally
                                    ) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 8.dp),
                                            text = "신부측",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFFBB7273)
                                        )
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.Bottom,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "아버지",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                                Text(
                                                    text = "윤태욱",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                            }
                                            Image(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clickable { window.location.href = "tel://010-6614-7514" },
                                                imageVector = Icons.Filled.Call,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(Color(0xFFBB7273))
                                            )
                                        }
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                                        ) {
                                            Row(
                                                verticalAlignment = Alignment.Bottom,
                                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                                            ) {
                                                Text(
                                                    text = "어머니",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 14.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                                Text(
                                                    text = "박성숙",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 16.sp,
                                                    color = Color(0xFF574B40)
                                                )
                                            }
                                            Image(
                                                modifier = Modifier
                                                    .size(18.dp)
                                                    .clickable { window.location.href = "tel://010-9254-3181" },
                                                imageVector = Icons.Filled.Call,
                                                contentDescription = null,
                                                colorFilter = ColorFilter.tint(Color(0xFFBB7273))
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
            }
            item(key = "calendar_text") {
                AnimatedBox {
                    Row(
                        modifier = Modifier
                            .padding(12.dp)
                            .padding(top = 40.dp),
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = "8",
                            style = fontFamily.titleMedium,
                            fontSize = 28.sp,
                            color = Color(0xFFB76E79)
                        )
                        Text(
                            text = "August",
                            style = fontFamily.titleMedium,
                            fontSize = 22.sp,
                            color = Color(0xFFB76E79)
                        )
                    }
                }
            }
            item(
                key = "calendar_content"
            ) {
                val weeks = remember { listOf("일","월","화","수","목","금","토") }
                val calendarDays = remember {
                    val year = 2025
                    val month = 8
                    val firstDayOfMonth = LocalDate(year, month, 1)
                    val daysInMonth = YearMonth.of(year, month).lengthOfMonth()
                    val firstDayOfWeek = firstDayOfMonth.dayOfWeek.ordinal + 1
                    val startOffset = if (firstDayOfWeek == 7) 0 else firstDayOfWeek

                    val days = (1..daysInMonth).toList()
                    List(startOffset) { 0 } + days + List(42 - (startOffset + daysInMonth)) { 0 }
                }
                AnimatedBox {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp)
                    ) {
                        HorizontalDivider(
                            color = Color(0xFFCFA8A8),
                            thickness = 2.dp
                        )
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 20.dp),
                            verticalArrangement = Arrangement.spacedBy(24.dp, Alignment.CenterVertically),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            repeat(7) { row ->
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    repeat(7) { col ->
                                        val text = remember {
                                            val index = row * 7 + (col - 7)
                                            val day = calendarDays.getOrNull(index)
                                            if (row == 0) {
                                                weeks[col]
                                            } else {
                                                if (day != null && day > 0) day.toString() else ""
                                            }
                                        }
                                        if(text != "30")
                                            Text(
                                                modifier = Modifier.weight(1f),
                                                text = text,
                                                style = fontFamily.bodyLarge,
                                                fontSize = 16.sp,
                                                color = if(text == "일") Color(0xFFB76E79) else Color(0xFF574B40),
                                                textAlign = TextAlign.Center
                                            )
                                        else {
                                            var timeTextPosition by remember { mutableStateOf(0f) }
                                            Box(
                                                modifier = Modifier.onSizeChanged {
                                                    timeTextPosition = it.height / dimension.floatValue
                                                }.weight(1f),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Box(
                                                    modifier = Modifier
                                                        .clip(CircleShape)
                                                        .sizeIn(maxWidth = 32.dp, maxHeight = 32.dp)
                                                        .background(color = Color(0xFFCFA8A8)),
                                                    contentAlignment = Alignment.Center
                                                ) {
                                                    Text(
                                                        modifier = Modifier
                                                            .padding(4.dp)
                                                            .aspectRatio(1f),
                                                        text = text,
                                                        style = fontFamily.bodyLarge,
                                                        fontSize = 16.sp,
                                                        color = Color(0xB3FFFFFF),
                                                        textAlign = TextAlign.Center
                                                    )
                                                }
                                                Text(
                                                    modifier = Modifier.offset(y = timeTextPosition.dp - 2.dp),
                                                    text = "오후5:30",
                                                    style = fontFamily.bodyLarge,
                                                    fontSize = 12.sp,
                                                    color = Color(0xFF574B40),
                                                    lineHeight = 11.sp,
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                        HorizontalDivider(
                            modifier = Modifier.padding(top = 4.dp),
                            color = Color(0xFFCFA8A8),
                            thickness = 2.dp
                        )
                    }
                }
            }
            item(
                key = "remain_time"
            ) {
                AnimatedBox {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 16.dp, bottom = 24.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp, Alignment.CenterVertically),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Row(
                            verticalAlignment = Alignment.Bottom,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(2.dp, Alignment.CenterHorizontally)
                            ) {
                                Text(
                                    text = "원석",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Color(0xFF574B40),
                                    textAlign = TextAlign.Center
                                )
                                Icon(
                                    modifier = Modifier.size(18.dp),
                                    painter = painterResource(Res.drawable.heart),
                                    contentDescription = null
                                )
                                Text(
                                    text = "서영",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 18.sp,
                                    color = Color(0xFF574B40),
                                    textAlign = TextAlign.Center
                                )
                            }
                            Text(
                                text = "의 결혼식",
                                style = fontFamily.bodyLarge,
                                fontSize = 14.sp,
                                color = Color(0xFF574B40),
                                lineHeight = 11.sp,
                                textAlign = TextAlign.Center
                            )
                        }
                        Column(
                            verticalArrangement = Arrangement.spacedBy(1.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            DdayContent(time = when {
                                remainDay.day == 0 -> "D-DAY"
                                remainDay.isPlus -> "D+"
                                else -> "D-"
                            } to remainDay.day)
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                            ) {
                                hour?.let {
                                    TimerContent(time = hour, backText = "시간")
                                }
                                TimerContent(time = min, backText = "분")
                                TimerContent(time = sec, backText = if(remainTime.isPlus) "초 지남" else "초 남음")
                            }
                        }
                    }
                }
            }
            item(
                key = "gallery_text",
                contentType = "gallery"
            ) {
                AnimatedBox {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(top = 80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        with(sharedTransitionScope) {
                            Text(
                                modifier = Modifier.sharedBounds(
                                    sharedContentState = rememberSharedContentState("gallery"),
                                    animatedVisibilityScope = animatedContentScope,
                                    enter = fadeIn(),
                                    exit = fadeOut(),
                                    resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                ),
                                text = "Gallery",
                                style = enFontFamily.bodyLarge,
                                fontSize = 32.sp,
                                color = Color(0xFFB76E79),
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
            items(
                count = 6,
                key = { i -> "gallery_item_$i" }
            ) { column ->
                val isOdd = column % 2 == 1
                val firstIndex = (column * 3) + (column * 2)
                val first = imageMedias.find { it.key == firstIndex + 1 }
                val second = imageMedias.find { it.key == firstIndex + 2 }
                val third = imageMedias.find { it.key == firstIndex + 3 }
                val forth = imageMedias.find { it.key == firstIndex + 4 }
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .animateItem(
                            fadeInSpec = spring(stiffness = Spring.StiffnessVeryLow),
                            fadeOutSpec = spring(stiffness = Spring.StiffnessHigh)
                        )
                        .padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if(isOdd) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(.5f)
                        ) {
                            playerStates.getOrNull(column)?.let {
                                videoMedias.find { it.key == column + 1}?.thumb?.let { thumb ->
                                    VideoPlayer(
                                        thumb = thumb,
                                        playerState = it
                                    )
                                }
                            }
                        }
                    }
                    Column(
                        modifier = Modifier.weight(2f),
                        verticalArrangement = Arrangement.spacedBy(2.dp)
                    ) {
                        repeat(2) { i ->
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(2.dp)
                            ) {
                                repeat(2) { j ->
                                    val item = when(i) {
                                        0 -> if(j == 0) first else second
                                        else -> if(j == 0) third else forth
                                    }
                                    with(sharedTransitionScope) {
                                        AsyncImage(
                                            modifier = Modifier
                                                .weight(1f)
                                                .fillMaxWidth()
                                                .clickable { showDetail.invoke(item?.fileName ?: "") }
                                                .aspectRatio(1f)
                                                .then(
                                                    if(detailKey.isBlank() || detailKey == item?.fileName)
                                                        Modifier.sharedBounds(
                                                            sharedContentState = rememberSharedContentState(key = item?.fileName ?: ""),
                                                            animatedVisibilityScope = animatedContentScope,
                                                            enter = fadeIn(),
                                                            exit = fadeOut(),
                                                            resizeMode = SharedTransitionScope.ResizeMode.ScaleToBounds()
                                                        )
                                                    else
                                                        Modifier
                                                ),
                                            model = ImageRequest.Builder(LocalPlatformContext.current)
                                                .data("${window.location.href}/asset/${item?.fileName}.jpg")
                                                .memoryCacheKey(item?.fileName)
                                                .diskCacheKey(item?.fileName)
                                                .fetcherFactory(ktorFactory)
                                                .build(),
                                            placeholder = ColorPainter(color = Color.LightGray.copy(alpha = .2f)),
                                            contentScale = ContentScale.Crop,
                                            contentDescription = null
                                        )
                                    }

                                }
                            }
                        }
                    }
                    if(isOdd.not()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .zIndex(-1f)
                                .aspectRatio(.5f)
                        ) {
                            playerStates.getOrNull(column)?.let {
                                videoMedias.find { it.key == column + 1}?.thumb?.let { thumb ->
                                    VideoPlayer(
                                        thumb = thumb,
                                        playerState = it
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item(key = "like") {
                AnimatedBox {
                    LikeSection(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 4.dp),
                        heartCount = heartCount,
                        blessingCount = blessingCount
                    )
                }
            }
            item(
                key = "bank_title"
            ) {
                AnimatedBox {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(top = 80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Account",
                            style = enFontFamily.bodyLarge,
                            fontSize = 32.sp,
                            color = Color(0xFFB76E79),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            item(key = "bank_text") {
                AnimatedBox {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 20.dp)
                            .fillMaxWidth(),
                        text = "보내주신 마음에 깊이 감사드립니다\n" +
                            "축하해주신 마음 소중히 간직하겠습니다",
                        style = fontFamily.bodyLarge,
                        fontSize = 16.sp,
                        color = Color(0xFF574B40),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item(
                key = "bank_content"
            ) {
                var isGroomOpened by remember { mutableStateOf(false) }
                val groomStateTransition = updateTransition(targetState = isGroomOpened, label = "")
                val groomRotation: Float by groomStateTransition.animateFloat(
                    transitionSpec = {
                        if (isGroomOpened) {
                            spring(stiffness = Spring.StiffnessLow)
                        } else {
                            spring(stiffness = Spring.StiffnessMedium)
                        }
                    },
                    label = ""
                ) { openState ->
                    if (openState) 180f else 0f
                }
                var isBrideOpened by remember { mutableStateOf(false) }
                val brideStateTransition = updateTransition(targetState = isBrideOpened, label = "")
                val brideRotation: Float by brideStateTransition.animateFloat(
                    transitionSpec = {
                        if (isBrideOpened) {
                            spring(stiffness = Spring.StiffnessLow)
                        } else {
                            spring(stiffness = Spring.StiffnessMedium)
                        }
                    },
                    label = ""
                ) { openState ->
                    if (openState) 180f else 0f
                }
                AnimatedBox {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .padding(horizontal = 16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .clickable { isGroomOpened = !isGroomOpened }
                                .align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "신랑측 계좌번호",
                                style = fontFamily.bodyLarge,
                                fontSize = 18.sp,
                                color = Color(0xFF5f8b9b)
                            )
                            Image(
                                modifier = Modifier
                                    .rotate(groomRotation)
                                    .size(21.dp),
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFF5f8b9b))
                            )
                        }
                        AnimatedVisibility(
                            visible = isGroomOpened,
                            enter = slideIn(animationSpec = tween(durationMillis = 500)) { IntOffset(0, -80) }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 4.dp),
                                            text = "신랑",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF5f8b9b)
                                        )
                                        Text(
                                            text = "1002-047-772519",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                        Text(
                                            text = "우리은행 최원석",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                    }
                                    Text(
                                        modifier = Modifier.clickable {
                                            window.navigator.clipboard.writeText("1002047772519")
                                            window.alert("[1002047772519] 복사 완료")
                                        },
                                        text = "복사",
                                        style = fontFamily.bodyLarge,
                                        fontSize = 16.sp,
                                        color = Color(0xFF574B40)
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = Color(0xFF574B40).copy(alpha = .3f)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 4.dp),
                                            text = "신랑 부모님",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF5f8b9b)
                                        )
                                        Text(
                                            text = "117-910010410-05",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                        Text(
                                            text = "하나은행 최상범",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                    }
                                    Text(
                                        modifier = Modifier.clickable {
                                            window.navigator.clipboard.writeText("11791001041005")
                                            window.alert("[11791001041005] 복사 완료")
                                        },
                                        text = "복사",
                                        style = fontFamily.bodyLarge,
                                        fontSize = 16.sp,
                                        color = Color(0xFF574B40)
                                    )
                                }
                            }
                        }
                        Row(
                            modifier = Modifier
                                .clickable { isBrideOpened = !isBrideOpened }
                                .align(Alignment.CenterHorizontally),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "신부측 계좌번호",
                                style = fontFamily.bodyLarge,
                                fontSize = 18.sp,
                                color = Color(0xFFBB7273)
                            )
                            Image(
                                modifier = Modifier
                                    .rotate(brideRotation)
                                    .size(21.dp),
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFFBB7273))
                            )
                        }
                        AnimatedVisibility(
                            visible = isBrideOpened,
                            enter = slideIn(animationSpec = tween(durationMillis = 500)) { IntOffset(0, -80) }
                        ) {
                            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 4.dp),
                                            text = "신부",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFFBB7273)
                                        )
                                        Text(
                                            text = "210702-04-371383",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                        Text(
                                            text = "국민은행 윤서영",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                    }
                                    Text(
                                        modifier = Modifier.clickable {
                                            window.navigator.clipboard.writeText("21070204371383")
                                            window.alert("[21070204371383] 복사 완료")
                                        },
                                        text = "복사",
                                        style = fontFamily.bodyLarge,
                                        fontSize = 16.sp,
                                        color = Color(0xFF574B40)
                                    )
                                }
                                HorizontalDivider(
                                    modifier = Modifier.padding(vertical = 4.dp),
                                    color = Color(0xFF574B40).copy(alpha = .3f)
                                )
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            modifier = Modifier.padding(bottom = 4.dp),
                                            text = "신부 부모님",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFFBB7273)
                                        )
                                        Text(
                                            text = "029-21-0791-565",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                        Text(
                                            text = "국민은행 박성숙",
                                            style = fontFamily.bodyLarge,
                                            fontSize = 16.sp,
                                            color = Color(0xFF574B40)
                                        )
                                    }
                                    Text(
                                        modifier = Modifier.clickable {
                                            window.navigator.clipboard.writeText("029210791565")
                                            window.alert("[029210791565] 복사 완료")
                                        },
                                        text = "복사",
                                        style = fontFamily.bodyLarge,
                                        fontSize = 16.sp,
                                        color = Color(0xFF574B40)
                                    )
                                }
                            }
                        }
                    }
                }
            }
            item(
                key = "map_text"
            ) {
                AnimatedBox {
                    Row(
                        modifier = Modifier
                            .padding(vertical = 20.dp)
                            .padding(top = 80.dp)
                            .fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = "Location",
                            style = enFontFamily.bodyLarge,
                            fontSize = 32.sp,
                            color = Color(0xFFB76E79),
                            textAlign = TextAlign.Center
                        )
                    }
                }
            }
            item(
                key = "map_location"
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "경기도 부천시 소사구 소사본동 65-7(경인로 386)",
                        style = fontFamily.bodyLarge,
                        fontSize = 14.sp,
                        color = Color(0xFF574B40),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "MJ컨벤션 그랜드볼룸 5층",
                        style = fontFamily.bodyLarge,
                        fontSize = 14.sp,
                        color = Color(0xFF574B40),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        modifier = Modifier
                            .clickable {
                                window.navigator.clipboard.writeText("경기도 부천시 소사구 소사본동 65-7")
                                window.alert("[경기도 부천시 소사구 소사본동 65-7]\n복사 완료")
                            },
                        text = "주소복사",
                        style = fontFamily.bodyLarge,
                        fontSize = 10.sp,
                        color = Color(0xFF574B40)
                    )
                }
            }
            item(
                key = "map"
            ) {
                Box(
                    modifier = Modifier
                        .width(360.dp)
                        .height(300.dp)
                        .padding(horizontal = 20.dp)
                )
            }
            item(
                key = "map_direct"
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 16.dp, horizontal = 16.dp)
                        .padding(top = 8.dp, bottom = 20.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                window.location.href = if(isMobileDevice())
                                    "kakaomap://place?id=27339651"
                                else
                                    "https://map.kakao.com/link/map/27339651"
                            },
                        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                        border = BorderStroke(1.dp, Color(0xFF574B40))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                        ) {
                            Image(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(Res.drawable.kakao_icon),
                                contentDescription = null
                            )
                            Text(
                                text = "카카오맵",
                                style = fontFamily.bodyLarge,
                                fontSize = 14.sp,
                                color = Color(0xFF574B40)
                            )
                        }
                    }
                    Card(
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                window.location.href = if(isMobileDevice())
                                    "nmap://place?lng=${weddingLng}&lat=${weddingLat}&name=MJ컨벤션&appname=https://wonseokk.github.io/Wedding/"
                                else
                                    "https://map.naver.com?lng=${weddingLng}&lat=${weddingLat}&title=MJ컨벤션"
                            },
                        colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent),
                        shape = RoundedCornerShape(4.dp),
                        elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                        border = BorderStroke(1.dp, Color(0xFF574B40))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                        ) {
                            Image(
                                modifier = Modifier.size(16.dp),
                                painter = painterResource(Res.drawable.navermap_icon),
                                contentDescription = null
                            )
                            Text(
                                text = "네이버지도",
                                style = fontFamily.bodyLarge,
                                fontSize = 14.sp,
                                color = Color(0xFF574B40)
                            )
                        }
                    }
                    if(isMobileDevice())
                        Card(
                            modifier = Modifier
                                .weight(1f)
                                .clickable { window.location.href = "tmap://route?rGoName=MJ컨벤션&rGoX=${weddingLng}&rGoY=${weddingLat}" },
                            colors = CardDefaults.cardColors().copy(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(4.dp),
                            elevation = CardDefaults.elevatedCardElevation(defaultElevation = 0.dp),
                            border = BorderStroke(1.dp, Color(0xFF574B40))
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                            ) {
                                Image(
                                    modifier = Modifier.size(16.dp),
                                    painter = painterResource(Res.drawable.tmap_icon),
                                    contentDescription = null
                                )
                                Text(
                                    text = "티맵",
                                    style = fontFamily.bodyLarge,
                                    fontSize = 14.sp,
                                    color = Color(0xFF574B40)
                                )
                            }
                        }
                }
            }
            item(
                key = "map_location_guide"
            ) {
                AnimatedBox {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 28.dp),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "지하철",
                                style = fontFamily.bodyLarge,
                                fontSize = 15.sp,
                                color = Color(0xFFB76E79)
                            )
                            Text(
                                text = "1호선, 서해선 > 소사역 1번출구 건너편 좌측(70m)",
                                style = fontFamily.bodyLarge,
                                fontSize = 14.sp,
                                color = Color(0xFF574B40)
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "버스",
                                style = fontFamily.bodyLarge,
                                fontSize = 15.sp,
                                color = Color(0xFFB76E79)
                            )
                            Text(
                                text = "소사구청삼거리·MJ 컨벤션: 19, 83, 88\n" +
                                    "소사구청삼거리: 53, 60-1\n" +
                                    "소사역: 19, 53, 83, 88\n" +
                                    "소사 푸르지오: 56, 56-1, 60",
                                style = fontFamily.bodyLarge,
                                fontSize = 14.sp,
                                color = Color(0xFF574B40)
                            )
                        }
                        Column(modifier = Modifier.fillMaxWidth()) {
                            Text(
                                text = "자가용",
                                style = fontFamily.bodyLarge,
                                fontSize = 15.sp,
                                color = Color(0xFFB76E79)
                            )
                            Text(
                                text = "본관 및 주차타워 이용 가능 (2시간 주차 무료)",
                                style = fontFamily.bodyLarge,
                                fontSize = 14.sp,
                                color = Color(0xFF574B40)
                            )
                        }
                    }
                }
            }
            item(
                key = "spacer"
            ) {
                if(dimension.floatValue > 2f) {
                    Spacer(modifier = Modifier.height(80.dp * (dimension.floatValue - 2)))
                } else {
                    Spacer(modifier = Modifier.height(50.dp))
                }
            }
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SvgAnimationContainer(
    modifier: Modifier = Modifier,
    isFront: Boolean,
) {
    val density = LocalDensity.current
    var svgContent: String? by remember { mutableStateOf(null) }

    var positionX by remember { mutableStateOf(0f) }
    var positionY by remember { mutableStateOf(0f) }
    var composableWidth by remember { mutableStateOf(0) }
    var composableHeight by remember { mutableStateOf(0) }
    var svgContainer by remember { mutableStateOf<HTMLDivElement?>(null) }

    LaunchedEffect(isFront, positionX, positionY, composableWidth, composableHeight) {
        try {
            if(composableHeight == 0 || composableWidth == 0)
                return@LaunchedEffect
            if(svgContent == null)
                svgContent = Res.readBytes("drawable/wedding_animation.svg").decodeToString()
            svgContent?.let {
                setupSvgAnimation(isFront, it, composableHeight)
            }
        } catch (e: Exception) {
            println("SVG 로드 중 오류 발생: ${e.message}")
        }
    }

    DisposableEffect(Unit) {
        document.getElementById("svg-animation-container")?.let {
            it.parentNode?.removeChild(it)
        }
        val container = document.createElement("div") as HTMLDivElement
        container.id = "svg-animation-container"
        document.body?.appendChild(container)
        svgContainer = container

        onDispose {
            container.parentNode?.removeChild(container)
        }
    }
    Box(
        modifier = modifier.onSizeChanged { size ->
            composableWidth = size.width.pxToDp(density).toInt()
            composableHeight = size.height.pxToDp(density).toInt()
            svgContainer?.style?.apply {
                left = "0px"
                top = "0px"
                width = "${composableWidth}px"
                height = "${composableHeight}px"
            }
        }.onGloballyPositioned { coordinates ->
            val position = coordinates.positionInRoot()
            positionX = position.x
            positionY = position.y
        }
    )
}

fun setupSvgAnimation(isFront: Boolean, svgContent: String, height: Int) {
    try {
        var container = document.getElementById("svg-animation-container") as? HTMLDivElement
        container?.remove()
        if(isFront.not())
            return
        container = document.createElement("div") as HTMLDivElement
        container.id = "svg-animation-container"

        container.style.position = "absolute"
        container.style.top = "0px"
        container.style.left = "50%"
        container.style.transform = "translateX(-50%)"
        container.style.width = "400px"
        container.style.height = "${height}px"
        container.style.zIndex = "100"

        document.body?.appendChild(container)
        container.innerHTML = svgContent
        var svgContainer = document.getElementById("Layer_1") as? HTMLDivElement
        svgContainer?.style?.apply {
            this.position = "absolute"
            this.top = "0px"
            this.left = "50%"
            this.transform = "translateX(-50%)"
            this.width = "400px"
            this.height = "${height}px"
            this.zIndex = "100"
        }
        
    } catch (e: Exception) {
        println("SVG 애니메이션 설정 중 오류 발생: ${e.message}")
    }
}


@Composable
private fun TimerContent(
    time: Pair<String, Int>,
    fontSize: Int = 15,
    backText: String,
    color: Color = Color(0xFF574B40),
) {
    DigitCountText(
        frontText = time.first,
        count = time.second,
        backText = backText,
        textColor = color,
        fontSize = fontSize,
    )
}

@Composable
private fun DdayContent(
    time: Pair<String, Int>,
    fontSize: Int = 28,
    color: Color = Color(0xFFB76E79),
) {
    if(time.second == 0){
        Text(
            text = time.first,
            textAlign = TextAlign.Center,
            style = fontFamily.bodyLarge,
            color = color,
            fontSize = fontSize.sp
        )
    } else {
        DigitCountText(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            frontText = time.first,
            count = time.second,
            textColor = color,
            fontSize = fontSize,
        )
    }

}

@Composable
private fun VideoPlayer(
    thumb: String,
    playerState: VideoPlayerState,
) {
    var isReady by remember { mutableStateOf(false) }
    LaunchedEffect(isReady) {
        if(playerState.isPlaying.not())
            playerState.play()
    }
    LaunchedEffect(playerState.isPlaying) {
        if(playerState.isPlaying) {
            delay(600)
            isReady = true
        } else
            isReady = false
    }
    DisposableEffect(Unit) {
        onDispose { isReady = false }
    }
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
        if(isReady.not())
            AsyncImage(
                modifier = Modifier.fillMaxSize(),
                model = ImageRequest.Builder(LocalPlatformContext.current)
                    .data("${window.location.href}/asset/$thumb.jpg")
                    .memoryCacheKey(thumb)
                    .diskCacheKey(thumb)
                    .fetcherFactory(ktorFactory)
                    .build(),
                placeholder = ColorPainter(color = Color.LightGray.copy(alpha = .2f)),
                contentScale = ContentScale.Crop,
                contentDescription = null
            )
    }
}

