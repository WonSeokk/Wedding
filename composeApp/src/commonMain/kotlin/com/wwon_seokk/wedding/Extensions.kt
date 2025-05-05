import androidx.compose.ui.unit.Density

fun Int.pxToDp(density: Density): Float = with(density) {
    return this@pxToDp / this@with.density
}

fun Int.dpToPx(density: Density): Float = with(density) {
    return this@dpToPx * this@with.density
}
