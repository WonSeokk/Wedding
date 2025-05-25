import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp


@Composable
fun AnimatedBox(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    var appeared by remember { mutableStateOf(false) }

    LaunchedEffect(Unit) {
        appeared = true
    }

    val alpha by animateFloatAsState(
        targetValue = if (appeared) 1f else 0f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 200),
        label = "alphaAnimation"
    )

    val offsetY by animateFloatAsState(
        targetValue = if (appeared) 0f else 50f,
        animationSpec = tween(durationMillis = 1000, delayMillis = 200),
        label = "offsetYAnimation"
    )

    Box(
        modifier = modifier
            .alpha(alpha)
            .offset(y = offsetY.dp)
    ) {
        content.invoke()
    }
}
