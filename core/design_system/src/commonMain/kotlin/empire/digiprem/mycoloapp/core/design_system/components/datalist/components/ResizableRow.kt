package empire.digiprem.mycoloapp.core.design_system.components.datalist.components

import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerEventPass
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun ResizableRow(
    modifier: Modifier = Modifier,
    minWidth: Dp = 100.dp,
    dividerWidth: Dp = 6.dp,
    dividerColor: Color = Color.Black,
    content: List<@Composable () -> Unit>
) {
    if (content.isEmpty()) return

    val density = LocalDensity.current

    val weights = remember(content.size) {
        mutableStateListOf<Float>().apply {
            repeat(content.size) { add(1f) }
        }
    }

    val actualWidthsPx = remember(content.size) {
        FloatArray(content.size) { 0f }
    }

    Row(
        modifier = modifier.fillMaxSize(),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        content.forEachIndexed { index, childContent ->
            Box(
                modifier = Modifier
                    .weight(weights[index])
                    .fillMaxHeight()
                    .onGloballyPositioned { coordinates ->
                        actualWidthsPx[index] = coordinates.size.width.toFloat()
                    },
                contentAlignment = Alignment.CenterStart
            ) {
                childContent()
            }

            if (index < content.size - 1) {
                Box(
                    modifier = Modifier
                        .width(dividerWidth)
                        .fillMaxHeight()
                        .background(dividerColor)
                        .pointerHoverIcon(PointerIcon.Hand)
                        .pointerInput(index) {
                            awaitEachGesture {
                                // ✅ requireUnconsumed = false — pend l'événement même s'il est consommé
                                val down = awaitFirstDown(requireUnconsumed = false)
                                down.consume()

                                do {
                                    val event = awaitPointerEvent(pass = PointerEventPass.Main)
                                    val change = event.changes.firstOrNull { it.id == down.id } ?: break

                                    if (!change.pressed) break

                                    val dragX = change.positionChange().x
                                    change.consume()

                                    if (dragX == 0f) continue

                                    val prevIndex = index
                                    val nextIndex = index + 1

                                    if (nextIndex >= actualWidthsPx.size) break

                                    val prevWidthPx = actualWidthsPx[prevIndex]
                                    val nextWidthPx = actualWidthsPx[nextIndex]
                                    val totalWidthPx = prevWidthPx + nextWidthPx
                                    val minWidthPx = with(density) { minWidth.toPx() }

                                    val newPrevWidthPx = (prevWidthPx + dragX)
                                        .coerceAtLeast(minWidthPx)
                                        .coerceAtMost(totalWidthPx - minWidthPx)
                                    val newNextWidthPx = totalWidthPx - newPrevWidthPx

                                    if (newPrevWidthPx >= minWidthPx && newNextWidthPx >= minWidthPx) {
                                        val totalWeight = weights[prevIndex] + weights[nextIndex]
                                        weights[prevIndex] = (newPrevWidthPx / totalWidthPx) * totalWeight
                                        weights[nextIndex] = (newNextWidthPx / totalWidthPx) * totalWeight
                                    }

                                } while (true)
                            }
                        }
                )
            }
        }
    }
}
