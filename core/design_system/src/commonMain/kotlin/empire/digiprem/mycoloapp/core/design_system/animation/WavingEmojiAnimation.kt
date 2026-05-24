package empire.digiprem.mycoloapp.core.design_system.animation

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.TransformOrigin
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle


@Composable
fun WavingEmojiAnimation(
    emoji: String,
    style: TextStyle=MaterialTheme.typography.bodyLarge,
    modifier: Modifier = Modifier,
) {
    val infiniteTransition = rememberInfiniteTransition()

    val rotation by infiniteTransition.animateFloat(
        initialValue = -10f,
        targetValue = 30f,
        animationSpec = infiniteRepeatable(
            animation = tween(600, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )
    Text(
        text =emoji,
        modifier = Modifier.graphicsLayer {
            rotationZ = rotation
            // On fixe le point de pivot en bas à droite pour simuler le poignet
            transformOrigin = TransformOrigin(1f, 1f)
        }.then(modifier),
       style = style
    )
}
