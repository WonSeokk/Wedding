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
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlin.math.ceil
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.delay
import kotlinx.datetime.LocalDate
import kotlinx.datetime.internal.JSJoda.YearMonth
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
        val dimension = remember { mutableFloatStateOf(2f) }
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
                    dimension.floatValue = it.width / window.innerWidth.toFloat()
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
            Flippable(
                modifier = Modifier
                    .width(400.dp)
                    .fillMaxHeight()
                    .align(Alignment.Center),
                frontSide = {
                    Cover(isFront = isFront)
                },
                backSide = {
                    Content(dimension = dimension)
                },
                flipController = flipController
            ) { isFront = it != FlippableState.BACK }
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
    dimension: MutableFloatState
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
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test,
            Res.drawable.test
        )
    }

    val listState = rememberLazyListState()
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
                println(positionY)
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
                Image(
                    painter = painterResource(Res.drawable.test),
                    contentDescription = null
                )
            }
            item(
                key = "main_text"
            ) {
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
            item(key = "calendar_text") {
                Row(
                    modifier = Modifier.padding(12.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        text = "8",
                        style = fontFamily.h2,
                        fontSize = 28.sp,
                        color = Color(0xFFB76E79)
                    )
                    Text(
                        text = "August",
                        style = fontFamily.h2,
                        fontSize = 20.sp,
                        color = Color(0xFFB76E79)
                    )
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
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp)
                ) {
                    Divider(
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
                                            style = fontFamily.body1,
                                            fontSize = 16.sp,
                                            color = if(text == "일") Color(0xFFB76E79) else Color(0xFF4B3621),
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
                                                    style = fontFamily.body1,
                                                    fontSize = 16.sp,
                                                    color = Color(0xB3FFFFFF),
                                                    textAlign = TextAlign.Center
                                                )
                                            }
                                            Text(
                                                modifier = Modifier.offset(y = timeTextPosition.dp - 2.dp),
                                                text = "오후5:30",
                                                style = fontFamily.body1,
                                                fontSize = 12.sp,
                                                color = Color(0xFF4B3621),
                                                lineHeight = 11.sp,
                                                textAlign = TextAlign.Center
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }
                    Divider(
                        modifier = Modifier.padding(top = 4.dp),
                        color = Color(0xFFCFA8A8),
                        thickness = 2.dp
                    )
                }
            }
            item(
                key = "gallery_text",
                contentType = "gallery"
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
            val columns = ceil(items.size / 5.toDouble()).toInt()
            items(
                count = columns,
                key = { i -> "gallery_item_$i" }
            ) { column ->
                val isOdd = column % 2 == 1
                val firstIndex = column * 3
                val first =  0 to items.getOrNull(firstIndex)
                val second = 1 to items.getOrNull(firstIndex + 1)
                val third = 2 to items.getOrNull(firstIndex + 2)
                val forth = 3 to items.getOrNull(firstIndex + 3)
                val fifth = 3 to items.getOrNull(firstIndex + 4)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 2.dp),
                    horizontalArrangement = Arrangement.spacedBy(2.dp)
                ) {
                    if(isOdd) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(.5f)
                        ) {
                            fifth.second?.let {
                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    painter = painterResource(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                                Text("5")
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
                                    val item = when(i + j) {
                                        0 -> first
                                        1 -> second
                                        2 -> third
                                        else -> forth
                                    }
                                    Box(modifier = Modifier.weight(1f)) {
                                        item.second?.let {
                                            Image(
                                                modifier = Modifier.aspectRatio(1f),
                                                painter = painterResource(it),
                                                contentScale = ContentScale.Crop,
                                                contentDescription = null
                                            )
                                            Text("${i + j}")
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(isOdd.not()) {
                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(.5f)
                        ) {
                            fifth.second?.let {
                                Image(
                                    modifier = Modifier.fillMaxWidth(),
                                    painter = painterResource(it),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = null
                                )
                                Text("5")
                            }
                        }
                    }
                }
            }
            item(
                key = "map_text"
            ) {
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
                key = "map_location"
            ) {
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
            if(dimension.floatValue > 2f) {
                item(
                    key = "spacer"
                ) {
                    Spacer(modifier = Modifier.height(100.dp * (dimension.floatValue - 2)))
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

