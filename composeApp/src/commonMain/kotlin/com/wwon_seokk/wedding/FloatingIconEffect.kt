import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.LinearOutSlowInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.size
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.zIndex
import kotlin.math.sin
import kotlin.random.Random
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun FloatingIconEffect(
    modifier: Modifier = Modifier,
    size: Dp,
    painter: Painter,
    onClick: () -> Unit,
    onUpdate: () -> Unit
) {
    var isClicked by remember { mutableStateOf(false) }
    var iconId by remember { mutableStateOf(0) }
    val icons = remember { mutableStateListOf<Int>() }

    LaunchedEffect(isClicked, icons.isEmpty()) {
        if(isClicked && icons.isEmpty()) {
            isClicked = false
            onUpdate.invoke()
        }
    }

    Box(modifier = modifier, contentAlignment = Alignment.Center) {
        IconButton(onClick = {
            isClicked = true
            icons.add(iconId++)
            onClick()
        }) {
            Image(
                modifier = Modifier.size(size),
                painter = painter,
                contentDescription = null
            )
        }
        icons.forEach { id ->
            FloatingAnimatedIcon(
                size = size,
                id = id,
                painter = painter,
                onAnimationEnd = { icons.remove(id) }
            )
        }
    }
}

@Composable
fun FloatingAnimatedIcon(
    size: Dp,
    id: Int,
    painter: Painter,
    onAnimationEnd: () -> Unit
) {
    val offsetY = remember { Animatable(0f) }
    val alpha = remember { Animatable(1f) }
    val scale = remember { Animatable(1f) }

    val random = remember(id) { Random(id) }
    val waveAmplitude = remember { random.nextInt(5, 20) }     // 진폭
    val waveFrequency = remember { random.nextDouble(15.0, 30.0) } // 주기
    val direction = remember { if (random.nextBoolean()) 1 else -1 } // 방향


    LaunchedEffect(id) {
        launch {
            offsetY.animateTo(
                targetValue = -350f,
                animationSpec = tween(3000, easing = LinearOutSlowInEasing)
            )
        }
        launch {
            alpha.animateTo(0f, tween(3000))
        }
        launch {
            scale.animateTo(10f, tween(3000))
        }
        delay(3000)
        onAnimationEnd()
    }
    Image(
        modifier = Modifier
            .zIndex(100f)
            .size(size)
            .offset(
                y = offsetY.value.dp,
                x = (direction * sin(offsetY.value / waveFrequency) * waveAmplitude).dp
            )
            .graphicsLayer(
                scaleX = scale.value,
                scaleY = scale.value,
                alpha = alpha.value
            )
            .alpha(alpha.value),
        painter = painter,
        contentDescription = null,
    )
}
