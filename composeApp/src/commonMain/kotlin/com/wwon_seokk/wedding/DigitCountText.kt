import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.wwon_seokk.wedding.fontFamily

@Composable
fun DigitCountText(
    modifier: Modifier = Modifier,
    frontText: String = "",
    count: Int? = null,
    countFloat: Float? = null,
    textColor: Color = Color(0xFF4B3621),
    fontSize: Int,
    style: TextStyle = fontFamily.body1,
    textAlign: TextAlign = TextAlign.Center,
) {
    Row(
        modifier = modifier
            .animateContentSize(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Text(
            text = frontText,
            textAlign = textAlign,
            style = style,
            color = textColor,
            fontSize = fontSize.sp
        )
        val digits = count?.toString()?.mapIndexed { _, c -> Digit(c, count) } ?: countFloat.toString().mapIndexed { _, c -> DigitFloat(c, countFloat!!) }
        digits.forEach { digit ->
            AnimatedContent(
                targetState = digit,
                transitionSpec = {
                    if (targetState > initialState) {
                        slideInVertically { -it } togetherWith slideOutVertically { it }
                    } else {
                        slideInVertically { it } togetherWith slideOutVertically { -it }
                    }
                },
                label = ""
            ) { cnt ->
                Text(
                    text = "${cnt.digitChar}",
                    textAlign = textAlign,
                    style = style,
                    color = textColor,
                    fontSize = fontSize.sp
                )
            }
        }
    }
}


interface DigitCnt {
    val digitChar: Char
}


operator fun DigitCnt.compareTo(other: DigitCnt): Int {
    return when(this) {
        is Digit -> fullNumber.compareTo((other as Digit).fullNumber)
        is DigitFloat -> fullNumber.compareTo((other as DigitFloat).fullNumber)
        else -> 0
    }
}

data class Digit(
    override val digitChar: Char,
    val fullNumber: Int
): DigitCnt {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is Digit -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }
}

data class DigitFloat(
    override val digitChar: Char,
    val fullNumber: Float
): DigitCnt {
    override fun equals(other: Any?): Boolean {
        return when (other) {
            is DigitFloat -> digitChar == other.digitChar
            else -> super.equals(other)
        }
    }
}
