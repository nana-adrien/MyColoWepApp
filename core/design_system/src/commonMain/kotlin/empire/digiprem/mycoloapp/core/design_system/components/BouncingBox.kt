package empire.digiprem.mycoloapp.core.design_system.components

import androidx.compose.animation.core.EaseIn
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.IntOffset

@Composable
fun BouncingBox(
    content: @Composable BoxScope.() -> Unit
) {
    val infiniteTransition = rememberInfiniteTransition()

    // Animation verticale infinie
    val offsetY by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 20f, // hauteur du rebond
        animationSpec = infiniteRepeatable(
            animation = tween(
                durationMillis = 700, // temps d’une chute
                easing = EaseIn // accélération vers le bas
            ),
            repeatMode = RepeatMode.Reverse // rebond automatique
        )
    )

    Box(
        modifier = Modifier.wrapContentSize()
            .offset { IntOffset(0, offsetY.toInt()) } // translation verticale

    ) { content() }
}