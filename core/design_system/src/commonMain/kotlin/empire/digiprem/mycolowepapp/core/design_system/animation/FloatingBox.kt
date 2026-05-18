package empire.digiprem.mycolowepapp.core.design_system.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer

@Composable
fun FloatingBox(
    modifier: Modifier = Modifier,
    seed: Int = (0..10000).random(),
    content: @Composable () -> Unit
) {
    val random = remember(seed) { kotlin.random.Random(seed) }
    val transition = rememberInfiniteTransition(label = "floating")

    val offsetX by transition.animateFloat(
        initialValue = random.nextFloat() * -30f,
        targetValue = random.nextFloat() * 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(random.nextInt(3000, 6000), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "x"
    )
    val offsetY by transition.animateFloat(
        initialValue = random.nextFloat() * -25f,
        targetValue = random.nextFloat() * 25f,
        animationSpec = infiniteRepeatable(
            animation = tween(random.nextInt(3000, 6000), easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "y"
    )

    Box(
        modifier = modifier.wrapContentSize().graphicsLayer {
            translationX = offsetX
            translationY = offsetY
        },
        content = { content() }
    )
}
