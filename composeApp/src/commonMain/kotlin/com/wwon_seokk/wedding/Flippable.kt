import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.snap
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.zIndex
import kotlinx.coroutines.delay

@Composable
fun Flippable(
    frontSide: @Composable () -> Unit,
    backSide: @Composable () -> Unit,
    flipController: FlippableController,
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.Center,
    flipDurationMs: Int = 800,
    flipOnTouch: Boolean = false,
    flipEnabled: Boolean = true,
    autoFlip: Boolean = true,
    autoFlipDurationMs: Int = 3800,
    cameraDistance: Float = 30.0F,
    flipAnimationType: FlipAnimationType = FlipAnimationType.HORIZONTAL_CLOCKWISE,
    onFlippedListener: (currentSide: FlippableState) -> Unit = { _, -> }
) {
    var prevViewState by remember { mutableStateOf(FlippableState.INITIALIZED) }
    var flippableState by remember { mutableStateOf(FlippableState.INITIALIZED) }
    val transition: Transition<FlippableState> = updateTransition(
        targetState = flippableState,
        label = "Flip Transition",
    )
    flipController.setConfig(
        flipEnabled = flipEnabled
    )

    LaunchedEffect(flipController.currentSide) {
        prevViewState = flippableState
        flippableState = flipController.currentSide
    }

    val flipCall: () -> Unit = {
        if (transition.isRunning.not() && flipEnabled) {
            prevViewState = flippableState
            if (flippableState == FlippableState.FRONT)
                flipController.flipToBack()
            else flipController.flipToFront()
        }
    }

    LaunchedEffect(transition.currentState) {
        if (transition.currentState == FlippableState.INITIALIZED) {
            prevViewState = FlippableState.INITIALIZED
            flippableState = FlippableState.FRONT
            return@LaunchedEffect
        }
        println("flip ${prevViewState != FlippableState.INITIALIZED && transition.currentState == flippableState}")
        onFlippedListener.invoke(flippableState)

        if (autoFlip && flippableState != FlippableState.BACK) {
            delay(autoFlipDurationMs.toLong())
            flipCall()
        }
    }

    val frontRotation: Float by transition.animateFloat(
        transitionSpec = {
            when {
                FlippableState.FRONT isTransitioningTo FlippableState.BACK -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        0f at 0
                        90f at flipDurationMs / 2
                        90f at flipDurationMs
                    }
                }

                FlippableState.BACK isTransitioningTo FlippableState.FRONT -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        90f at 0
                        90f at flipDurationMs / 2
                        0f at flipDurationMs
                    }
                }

                else -> snap()
            }
        },
        label = "Front Rotation"
    ) { state ->
        when(state) {
            FlippableState.INITIALIZED, FlippableState.FRONT -> 0f
            FlippableState.BACK -> 180f
        }
    }

    val backRotation: Float by transition.animateFloat(
        transitionSpec = {
            when {
                FlippableState.FRONT isTransitioningTo FlippableState.BACK -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        -90f at 0
                        -90f at flipDurationMs / 2
                        0f at flipDurationMs
                    }
                }

                FlippableState.BACK isTransitioningTo FlippableState.FRONT -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        0f at 0
                        -90f at flipDurationMs / 2
                        -90f at flipDurationMs
                    }
                }

                else -> snap()
            }
        },
        label = "Back Rotation"
    ) { state ->
        when(state) {
            FlippableState.INITIALIZED, FlippableState.FRONT -> 180f
            FlippableState.BACK -> 0f
        }
    }

    val frontOpacity: Float by transition.animateFloat(
        transitionSpec = {
            when {
                FlippableState.FRONT isTransitioningTo FlippableState.BACK -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        1f at 0
                        1f at (flipDurationMs / 2) - 1
                        0f at flipDurationMs / 2
                        0f at flipDurationMs
                    }
                }

                FlippableState.BACK isTransitioningTo FlippableState.FRONT -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        0f at 0
                        0f at (flipDurationMs / 2) - 1
                        1f at flipDurationMs / 2
                        1f at flipDurationMs
                    }
                }

                else -> snap()
            }
        },
        label = "Front Opacity"
    ) { state ->
        when(state) {
            FlippableState.INITIALIZED, FlippableState.FRONT -> 1f
            FlippableState.BACK -> 0f
        }
    }

    val backOpacity: Float by transition.animateFloat(
        transitionSpec = {
            when {
                FlippableState.FRONT isTransitioningTo FlippableState.BACK -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        0f at 0
                        0f at (flipDurationMs / 2) - 1
                        1f at flipDurationMs / 2
                        1f at flipDurationMs
                    }
                }

                FlippableState.BACK isTransitioningTo FlippableState.FRONT -> {
                    keyframes {
                        durationMillis = flipDurationMs
                        1f at 0
                        1f at (flipDurationMs / 2) - 1
                        0f at flipDurationMs / 2
                        0f at flipDurationMs
                    }
                }

                else -> snap()
            }
        },
        label = "Back Opacity"
    ) { state ->
        when(state) {
            FlippableState.INITIALIZED, FlippableState.FRONT -> 0f
            FlippableState.BACK -> 1f
        }
    }

    Box(
        modifier = modifier
            .clickable(
                enabled = flipOnTouch,
                onClick = {
                    flipCall()
                },
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ),
        contentAlignment = contentAlignment
    ) {

        Box(modifier = Modifier
            .graphicsLayer {
                this.cameraDistance = cameraDistance
                when (flipAnimationType) {
                    FlipAnimationType.HORIZONTAL_CLOCKWISE -> rotationY = backRotation
                    FlipAnimationType.HORIZONTAL_ANTI_CLOCKWISE -> rotationY = -backRotation
                    FlipAnimationType.VERTICAL_CLOCKWISE -> rotationX = backRotation
                    FlipAnimationType.VERTICAL_ANTI_CLOCKWISE -> rotationX = -backRotation
                }
            }
            .alpha(backOpacity)
            .zIndex(1F - backOpacity)
        ) {
            backSide()
        }

        Box(modifier = Modifier
            .graphicsLayer {
                this.cameraDistance = cameraDistance
                when (flipAnimationType) {
                    FlipAnimationType.HORIZONTAL_CLOCKWISE -> rotationY = frontRotation
                    FlipAnimationType.HORIZONTAL_ANTI_CLOCKWISE -> rotationY = -frontRotation
                    FlipAnimationType.VERTICAL_CLOCKWISE -> rotationX = frontRotation
                    FlipAnimationType.VERTICAL_ANTI_CLOCKWISE -> rotationX = -frontRotation
                }
            }
            .alpha(frontOpacity)
            .zIndex(1F - frontRotation)
        ) {
            frontSide()
        }
    }
}

class FlippableController {

    var currentSide: FlippableState by mutableStateOf(FlippableState.FRONT)
    private var _flipEnabled: Boolean = true

    fun flipToFront() {
        flip(FlippableState.FRONT)
    }

    fun flipToBack() {
        flip(FlippableState.BACK)
    }

    fun flip(flippableState: FlippableState) {
        if (_flipEnabled.not())
            return

        currentSide = flippableState
    }

    internal fun setConfig(
        flipEnabled: Boolean = true,
    ) {
        _flipEnabled = flipEnabled
    }
}

@Composable
fun rememberFlipController(): FlippableController {
    return remember {
        FlippableController()
    }
}

enum class FlippableState {
    INITIALIZED,
    FRONT,
    BACK
}

enum class FlipAnimationType {
    HORIZONTAL_CLOCKWISE,
    HORIZONTAL_ANTI_CLOCKWISE,
    VERTICAL_CLOCKWISE,
    VERTICAL_ANTI_CLOCKWISE
}
