import androidx.compose.runtime.Stable
import androidx.compose.ui.unit.Density
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.DurationUnit
import kotlinx.browser.window
import kotlinx.datetime.Clock

fun Int.pxToDp(density: Density): Float = with(density) {
    return this@pxToDp / this@with.density
}

fun Int.dpToPx(density: Density): Float = with(density) {
    return this@dpToPx * this@with.density
}

/**
 * 타이머 계산
 */
fun Long.getRemainTime(): RemainTime {
    val currentTime = Clock.System.now().toEpochMilliseconds()
    val remainMillis = this - currentTime

    val duration = remainMillis.milliseconds

    val totalDays = duration.toLong(DurationUnit.DAYS)
    val totalHours = duration.toLong(DurationUnit.HOURS)
    val totalMinutes = duration.toLong(DurationUnit.MINUTES)
    val totalSeconds = duration.toLong(DurationUnit.SECONDS)

    val day = totalDays
    val hour = totalHours % 24
    val min = totalMinutes % 60
    val sec = totalSeconds % 60

    return RemainTime(
        day = day.toInt(),
        hour = hour.toInt().toString().padStart(2, '0'),
        min = min.toInt().toString().padStart(2, '0'),
        sec = sec.toInt().toString().padStart(2, '0')
    )
}
@Stable
data class RemainTime(
    val day: Int = 0,
    val hour: String = "00",
    val min: String = "00",
    val sec: String = "00"
)

fun isMobileDevice(): Boolean {
    val userAgent = window.navigator.userAgent.lowercase()
    return userAgent.contains("mobi") ||
        userAgent.contains("android") ||
        userAgent.contains("iphone") ||
        userAgent.contains("ipad")
}
