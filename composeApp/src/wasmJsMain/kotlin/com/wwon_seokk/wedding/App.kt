package com.wwon_seokk.wedding

import DigitCountText
import Flippable
import FlippableState
import RemainTime
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.scaleIn
import androidx.compose.animation.scaleOut
import androidx.compose.animation.slideIn
import androidx.compose.animation.slideOut
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.sizeIn
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Call
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableFloatState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil3.compose.LocalPlatformContext
import getRemainTime
import kotlin.math.ceil
import kotlinx.browser.document
import kotlinx.browser.window
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.datetime.LocalDate
import kotlinx.datetime.LocalDateTime
import kotlinx.datetime.TimeZone
import kotlinx.datetime.internal.JSJoda.YearMonth
import kotlinx.datetime.toInstant
import org.jetbrains.compose.resources.ExperimentalResourceApi
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.vectorResource
import org.w3c.dom.HTMLDivElement
import pxToDp
import rememberFlipController
import wedding.composeapp.generated.resources.Res
import wedding.composeapp.generated.resources.content_background
import wedding.composeapp.generated.resources.cover
import wedding.composeapp.generated.resources.cover_background
import wedding.composeapp.generated.resources.heart
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
    isFront: Boolean,
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
    dimension: MutableFloatState,
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
    val timeMillis = remember {
        val date = LocalDateTime.parse("2025-08-30T08:30:00")
        val instant = date.toInstant(TimeZone.UTC)
        instant.toEpochMilliseconds()
    }
    var remainTime: RemainTime by remember { mutableStateOf(RemainTime())}
    LaunchedEffect(Unit) {
        launch(Dispatchers.Default) {
            while(true) {
                remainTime = timeMillis.getRemainTime()
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
                        .padding(top = 20.dp, bottom = 12.dp)
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
                            "따뜻한 축복으로 저희 두 사람이\n함께하는 첫 걸음을 더욱 빛내주세요",
                        style = fontFamily.body1,
                        fontSize = 14.sp,
                        color = Color(0xFF4B3621),
                        textAlign = TextAlign.Center
                    )
                }
            }
            item(key = "contact") {
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
                                style = fontFamily.body1,
                                fontSize = 18.sp,
                                color = Color(0xFF4B3621)
                            )
                            Text(
                                text = "의 아들",
                                style = fontFamily.body1,
                                fontSize = 15.sp,
                                color = Color(0xFF4B3621)
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = "원석",
                                style = fontFamily.body1,
                                fontSize = 18.sp,
                                color = Color(0xFF4B3621)
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
                                style = fontFamily.body1,
                                fontSize = 18.sp,
                                color = Color(0xFF4B3621)
                            )
                            Text(
                                text = "의   딸 ",
                                style = fontFamily.body1,
                                fontSize = 15.sp,
                                color = Color(0xFF4B3621)
                            )
                            Text(
                                modifier = Modifier.padding(start = 4.dp),
                                text = "서영",
                                style = fontFamily.body1,
                                fontSize = 18.sp,
                                color = Color(0xFF4B3621)
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
                                style = fontFamily.body1,
                                fontSize = 15.sp,
                                color = Color(0xFF4B3621)
                            )
                            Image(
                                modifier = Modifier
                                    .rotate(rotation)
                                    .size(21.dp),
                                imageVector = Icons.Filled.ArrowDropDown,
                                contentDescription = null,
                                colorFilter = ColorFilter.tint(Color(0xFF4B3621))
                            )
                        }
                        AnimatedVisibility(
                            visible = isParentContactOpened,
                            enter = slideIn(animationSpec = tween(durationMillis = 500)) { IntOffset(0, -80) }
                        ) {
                            Row(
                                modifier = Modifier.animateContentSize(
                                    animationSpec = tween(
                                        durationMillis = 250,
                                        easing = LinearEasing
                                    )
                                ),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Column(
                                    modifier = Modifier.weight(1f),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        modifier = Modifier.padding(bottom = 8.dp),
                                        text = "신랑측",
                                        style = fontFamily.body1,
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
                                                style = fontFamily.body1,
                                                fontSize = 14.sp,
                                                color = Color(0xFF4B3621)
                                            )
                                            Text(
                                                text = "최상범",
                                                style = fontFamily.body1,
                                                fontSize = 16.sp,
                                                color = Color(0xFF4B3621)
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
                                                style = fontFamily.body1,
                                                fontSize = 14.sp,
                                                color = Color(0xFF4B3621)
                                            )
                                            Text(
                                                text = "이혜정",
                                                style = fontFamily.body1,
                                                fontSize = 16.sp,
                                                color = Color(0xFF4B3621)
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
                                        style = fontFamily.body1,
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
                                                style = fontFamily.body1,
                                                fontSize = 14.sp,
                                                color = Color(0xFF4B3621)
                                            )
                                            Text(
                                                text = "윤태욱",
                                                style = fontFamily.body1,
                                                fontSize = 16.sp,
                                                color = Color(0xFF4B3621)
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
                                                style = fontFamily.body1,
                                                fontSize = 14.sp,
                                                color = Color(0xFF4B3621)
                                            )
                                            Text(
                                                text = "박성숙",
                                                style = fontFamily.body1,
                                                fontSize = 16.sp,
                                                color = Color(0xFF4B3621)
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
                key = "remain_time"
            ) {
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
                               style = fontFamily.body1,
                               fontSize = 18.sp,
                               color = Color(0xFF4B3621),
                               textAlign = TextAlign.Center
                           )
                           Icon(
                               modifier = Modifier.size(18.dp),
                               painter = painterResource(Res.drawable.heart),
                               tint = Color.Black,
                               contentDescription = null
                           )
                           Text(
                               text = "서영",
                               style = fontFamily.body1,
                               fontSize = 18.sp,
                               color = Color(0xFF4B3621),
                               textAlign = TextAlign.Center
                           )
                       }
                        Text(
                            text = "의 결혼식",
                            style = fontFamily.body1,
                            fontSize = 14.sp,
                            color = Color(0xFF4B3621),
                            lineHeight = 11.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                    Row(
                        verticalAlignment = Alignment.Bottom,
                        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
                    ) {
                        DdayContent(time = "D-" to remainTime.day)
                        Row(
                            modifier = Modifier.height(28.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(1.dp, Alignment.CenterHorizontally)
                        ) {
                            hour?.let {
                                TimerContent(time = hour)
                                TimerMiddle()
                            }
                            TimerContent(time = min)
                            TimerMiddle()
                            TimerContent(time = sec)
                        }
                    }
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
                key = "map_direct"
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Text(
                        modifier = Modifier.clickable {
                            window.location.href = "tmap://route?rGoName=MJ컨벤션&rGoX=${weddingLng}&rGoY=${weddingLat}"
                        },
                        text = "tmap"
                    )
                    Text(
                        modifier = Modifier.clickable {
                            window.location.href = "kakaomap://place?id=27339651"
                        },
                        text = "kakao"
                    )
                    Text(
                        modifier = Modifier.clickable {
                            window.location.href = "https://map.naver.com?lng=${weddingLng}&lat=${weddingLat}&title=MJ컨벤션"
                        },
                        text = "naver"
                    )
                    Text(
                        modifier = Modifier.clickable {
                            window.location.href = "nmap://place?lng=${weddingLng}&lat=${weddingLat}&name=MJ컨벤션&appname=https://wonseokk.github.io/Wedding/"
                        },
                        text = "naver2"
                    )
                }
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



@Composable
private fun TimerMiddle(
    padding: Int = 3,
    middleFontSize: Int = 2,
    color: Color = Color(0xFF4B3621)
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(padding.dp, Alignment.CenterVertically)
    ) {
        Spacer(
            modifier = Modifier
                .size(middleFontSize.dp)
                .clip(CircleShape)
                .background(color = color)
        )
        Spacer(
            modifier = Modifier
                .size(middleFontSize.dp)
                .clip(CircleShape)
                .background(color = color)
        )
    }
}

@Composable
private fun TimerContent(
    time: Pair<String, Int>,
    fontSize: Int = 18,
    contentColor: Color = Color.Transparent,
    color: Color = Color(0xFF4B3621)
) {
    Surface(
        modifier = Modifier
            .fillMaxHeight()
            .aspectRatio(1f),
        shape = RoundedCornerShape(7.dp),
        color = contentColor
    ) {
        DigitCountText(
            modifier = Modifier.fillMaxWidth(),
            frontText = time.first,
            count = time.second,
            textColor = color,
            fontSize = fontSize,
        )
    }
}

@Composable
private fun DdayContent(
    time: Pair<String, Int>,
    fontSize: Int = 24,
    contentColor: Color = Color(0xFFCFA8A8),
    color: Color = Color(0xFF4B3621)
) {
    Surface(
        modifier = Modifier.fillMaxHeight(),
        shape = RoundedCornerShape(7.dp),
        color = contentColor.copy(alpha = .4f)
    ) {
        DigitCountText(
            modifier = Modifier.padding(vertical = 4.dp, horizontal = 8.dp),
            frontText = time.first,
            count = time.second,
            textColor = color,
            fontSize = fontSize,
        )
    }
}
