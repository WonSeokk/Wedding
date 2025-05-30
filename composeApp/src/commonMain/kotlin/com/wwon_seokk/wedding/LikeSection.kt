package com.wwon_seokk.wedding

import DigitCountText
import FloatingIconEffect
import androidx.compose.foundation.layout.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlin.js.Promise
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.await
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.painterResource
import wedding.composeapp.generated.resources.Res
import wedding.composeapp.generated.resources.heart2
import wedding.composeapp.generated.resources.party

@JsName("incrementDailyVisit")
external fun incrementDailyVisit()

@JsName("incrementLike")
external fun incrementLike(type: String)

@JsName("getLikeCount")
external fun getLikeCount(type: String): Promise<JsNumber>

@Composable
fun LikeSection(
    modifier: Modifier = Modifier,
    heartCount: Int,
    blessingCount: Int
) {
    var heartCount by remember { mutableIntStateOf(heartCount) }
    var blessingCount by remember { mutableIntStateOf(blessingCount) }
    val scope = rememberCoroutineScope()
    
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(4.dp, Alignment.CenterHorizontally)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingIconEffect(
                painter = painterResource(resource = Res.drawable.heart2),
                size = 52.dp,
                onClick = {
                    incrementLike("heart")
                    heartCount++
                },
                onUpdate = {
                    scope.launch(Dispatchers.Unconfined) {
                        heartCount = getLikeCount("heart").await<JsNumber>().toInt()
                    }
                }
            )
            DigitCountText(
                count = heartCount,
                style = fontFamily.bodyMedium,
                fontSize = 14
            )
        }
        Spacer(modifier = Modifier.width(20.dp))
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            FloatingIconEffect(
                size = 52.dp,
                painter = painterResource(resource = Res.drawable.party),
                onClick = {
                    incrementLike("blessing")
                    blessingCount++
                },
                onUpdate = {
                    scope.launch(Dispatchers.Unconfined) {
                        blessingCount = getLikeCount("blessing").await<JsNumber>().toInt()
                    }
                }
            )
            DigitCountText(
                count = blessingCount,
                style = fontFamily.bodyMedium,
                fontSize = 14
            )
        }
    }
}
