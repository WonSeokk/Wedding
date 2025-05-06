package com.wwon_seokk.wedding

import Flippable
import FlippableState
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridItemSpan
import androidx.compose.foundation.lazy.staggeredgrid.itemsIndexed
import androidx.compose.foundation.lazy.staggeredgrid.rememberLazyStaggeredGridState
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.FilterQuality
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.AsyncImage
import coil3.compose.rememberAsyncImagePainter
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.w3c.dom.HTMLDivElement
import pxToDp
import rememberFlipController
import wedding.composeapp.generated.resources.Res
import wedding.composeapp.generated.resources.content_background
import wedding.composeapp.generated.resources.cover
import wedding.composeapp.generated.resources.cover_background
import wedding.composeapp.generated.resources.test


@JsName("initNaverMap")
external fun initNaverMap(elementId: String, lat: Double, lng: Double, zoom: Int)

@JsName("showNaverMap")
external fun showNaverMap(elementId: String, show: Boolean, positionY: Float)

@JsName("registerMapBox")
external fun registerMapBox(elementId: String)

const val weddingLat = 37.481504867692
const val weddingLng = 126.79853505353
const val zoomLevel = 16

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        LaunchedEffect(Unit) {
            registerMapBox("map-box")
            initNaverMap("map-container", weddingLat, weddingLng, zoomLevel)
        }
        val dimension = remember { mutableIntStateOf(2) }
        val flipController = rememberFlipController()
        var isFront by remember { mutableStateOf(true) }
        var isCoverBackground by remember { mutableStateOf(true) }
        LaunchedEffect(Unit) {
            delay(3800)
            isCoverBackground = false
        }
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .onSizeChanged {
                    dimension.intValue = it.width / window.innerWidth
                },
            contentAlignment = Alignment.Center
        ) {
            AnimatedVisibility(
                visible = isCoverBackground,
                enter = fadeIn(animationSpec = tween(durationMillis = 1500)),
                exit = fadeOut(animationSpec = tween(durationMillis = 1500))
            ) {
                Image(
                    modifier = Modifier.fillMaxSize(),
                    painter = painterResource(Res.drawable.cover_background),
                    contentScale = ContentScale.FillHeight,
                    contentDescription = null
                )
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
            Box(
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center)
            ) {
                Content(dimension = dimension)
            }

//            Flippable(
//                modifier = Modifier
//                    .width(400.dp)
//                    .fillMaxHeight()
//                    .align(Alignment.Center),
//                frontSide = {
//                    Cover(isFront = isFront)
//                },
//                backSide = {
//                    Content(dimension = dimension)
//                },
//                flipController = flipController
//            ) { isFront = it != FlippableState.BACK }
        }
    }
}

@Composable
private fun Cover(
    modifier: Modifier = Modifier,
    isFront: Boolean
) {
    Box(modifier = modifier) {
        Image(
            modifier = Modifier.fillMaxWidth(),
            painter = painterResource(Res.drawable.cover),
            contentDescription = null
        )
        SvgAnimationContainer(modifier = Modifier.matchParentSize(), isFront = isFront)
    }
}

@OptIn(ExperimentalResourceApi::class)
@Composable
private fun Content(
    dimension: MutableIntState
) {
    val items = remember {
        listOf(
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test
        ) +
            listOf(
                Res.drawable.test,
                Res.drawable.test,
                Res.drawable.test,
                Res.drawable.test,
                Res.drawable.test,
                Res.drawable.test
            )
    }

    val height = remember { minOf(window.innerWidth * 265 / 400, 265) }
    val listState = rememberLazyStaggeredGridState()
    val mapPositionY by remember {
        derivedStateOf {
            listState.layoutInfo.visibleItemsInfo.find { it.key == "map" }?.let {
                it.offset.y / dimension.intValue
            }
        }
    }
    var positionY by remember { mutableStateOf(0f) }
    LaunchedEffect(mapPositionY) {
        mapPositionY?.let { y ->
            if(y.toFloat() != positionY) {
                positionY = y.toFloat()
                println(positionY)
                showNaverMap("map-container", true, positionY)
            }
        } ?: run {
            showNaverMap("map-container", false, 0f)
        }
    }
    LazyVerticalStaggeredGrid(
        modifier = Modifier.fillMaxSize(),
        state = listState,
        columns = StaggeredGridCells.Fixed(3),
        verticalItemSpacing = 2.dp,
        horizontalArrangement = Arrangement.spacedBy(2.dp),
        content = {
            item(span = StaggeredGridItemSpan.FullLine) {
                Image(
                    painter = painterResource(Res.drawable.test),
                    contentDescription = null
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Column(
                    modifier = Modifier
                        .padding(vertical = 20.dp)
                        .padding(bottom = 12.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "초  대  합  니  다",
                            style = fontFamily.h2,
                            fontSize = 16.sp,
                            color = Color(0xFFB76E79),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "―――――――――――",
                            style = fontFamily.h2,
                            fontSize = 16.sp,
                            color = Color(0xFFCFA8A8)
                        )
                    }
                    Text(
                        modifier = Modifier.fillMaxWidth(),
                        text = "다른 공간, 다른 시간을 걷던 두 사람이\n서로를 마주한 이후 같은 공간, 같은 시간을\n꿈꾸며 걷게 되었습니다\n\n" +
                            "따뜻한 축복으로 저희 두 사람이\n함께하는 첫 걸음을 더욱 빛내주세요.",
                        style = fontFamily.body1,
                        fontSize = 14.sp,
                        color = Color(0xFF4B3621),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item(
                contentType = "title",
                span = StaggeredGridItemSpan.FullLine
            ) {
                Column(
                    modifier = Modifier
                        .padding(bottom = 20.dp)
                        .fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .padding(horizontal = 40.dp)
                            .fillMaxWidth()
                    ) {
                        Text(
                            text = "갤  러  리",
                            style = fontFamily.h2,
                            fontSize = 16.sp,
                            color = Color(0xFFB76E79),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.weight(1f))
                        Text(
                            text = "―――――――――――",
                            style = fontFamily.h2,
                            fontSize = 16.sp,
                            color = Color(0xFFCFA8A8)
                        )
                    }
                }
            }
            itemsIndexed(
                key = { i, item -> i },
                items = items,
                span = { i, item -> StaggeredGridItemSpan.SingleLane }
            ) { index, item ->
//                Image(
//                    modifier = Modifier
//                        .fillMaxWidth()
//                        .then(
//                            when (index) {
//                                2, 6, 11 -> Modifier.height(height.dp)
//                                else -> Modifier.aspectRatio(1f)
//                            }
//                        ),
//                    painter = painterResource(item),
//                    contentScale = ContentScale.Crop,
//                    contentDescription = null
//                )
                when (index) {
                    2, 6, 11 -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(height.dp)
                                .background(Color.Red)
                        )
                    }
                    else -> {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .background(Color.Blue)
                        )
                    }
                }
                Text("$index")
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Row(
                    modifier = Modifier
                        .padding(horizontal = 40.dp, vertical = 20.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        text = "오시는 길",
                        style = fontFamily.h2,
                        fontSize = 16.sp,
                        color = Color(0xFFB76E79),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "―――――――――――",
                        style = fontFamily.h2,
                        fontSize = 16.sp,
                        color = Color(0xFFCFA8A8)
                    )
                }
            }
            item(
                key = "map",
                span = StaggeredGridItemSpan.FullLine
            ) {
                Box(
                    modifier = Modifier
                        .width(360.dp)
                        .height(300.dp)
                        .padding(horizontal = 20.dp)
                )
            }
            item(span = StaggeredGridItemSpan.FullLine) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 16.dp, bottom = 32.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = "경기도 부천시 소사구 소사본동 65-7",
                        style = fontFamily.body1,
                        fontSize = 14.sp,
                        color = Color(0xFF4B3621),
                        textAlign = TextAlign.Center
                    )
                    Text(
                        text = "MJ컨벤션 그랜드볼룸",
                        style = fontFamily.body1,
                        fontSize = 14.sp,
                        color = Color(0xFF4B3621),
                        textAlign = TextAlign.Center
                    )
                }
            }
            if(dimension.intValue > 2) {
                item(span = StaggeredGridItemSpan.FullLine) {
                    Spacer(modifier = Modifier.height(100.dp * (dimension.intValue - 2)))
                }
            }
        }
    )
}

@OptIn(ExperimentalResourceApi::class)
@Composable
fun SvgAnimationContainer(
    modifier: Modifier = Modifier,
    isFront: Boolean
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
            if(composableHeight == 0)
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

