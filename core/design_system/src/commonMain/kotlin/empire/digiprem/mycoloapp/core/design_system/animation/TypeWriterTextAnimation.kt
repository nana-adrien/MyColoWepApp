package empire.digiprem.mycoloapp.core.design_system.animation

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import empire.digiprem.mycoloapp.core.design_system.currentDeviceConfigure
import kotlinx.coroutines.delay


@Composable
fun TypeWriterTextAnimation(
    text: String,
    style: androidx.compose.ui.text.TextStyle = LocalTextStyle.current,
    textAnimateColor: Color = MaterialTheme.colorScheme.primary,
    additionalTexts: List<String> = listOf(),
) {
    val isMobileDevice = currentDeviceConfigure().isMobileDevice()
    var textIndex by remember { mutableStateOf(0) }
    var charIndex by remember { mutableStateOf(0) }
    var deleting by remember { mutableStateOf(false) }

    val currentText = additionalTexts[textIndex]

    if (charIndex > currentText.length) {
        charIndex = currentText.length
    }
    LaunchedEffect(charIndex, deleting, textIndex) {

        delay(if (deleting) 40 else 80)

        if (!deleting) {
            if (charIndex < currentText.length) {
                charIndex++
            } else {
                delay(1200)
                deleting = true
            }
        } else {
            if (charIndex > 0) {
                charIndex--
            } else {
                deleting = false
                textIndex = (textIndex + 1) % additionalTexts.size
            }
        }
    }

    if (isMobileDevice) {
        Column(
            verticalArrangement = Arrangement.spacedBy(5.dp,),
        ) {
            Text(
                text = text,
                style = style
            )
            Row {
                Text(
                    currentText.take(charIndex),// currentText.substring(0, charIndex),
                    style = style.copy(fontWeight = FontWeight.Bold),
                    color = textAnimateColor,
                )
                BlinkingCursorAnimation(style = style, cursorColor = textAnimateColor)
            }
        }
    }else{
        Row {
            Text(
                text = text,
                style = style
            )
            Text(
                currentText.substring(0, charIndex),
                style = style.copy(fontWeight = FontWeight.Bold),
                color = textAnimateColor,
            )
            BlinkingCursorAnimation(style = style, cursorColor = textAnimateColor)
        }
    }
}

@Composable
private fun BlinkingCursorAnimation(
    style: androidx.compose.ui.text.TextStyle,
    cursorColor: Color = MaterialTheme.colorScheme.primary,
) {

    val infiniteTransition = rememberInfiniteTransition()

    val alpha by infiniteTransition.animateFloat(
        initialValue = 1f,
        targetValue = 0f,
        animationSpec = infiniteRepeatable(
            animation = tween(500),
            repeatMode = RepeatMode.Reverse
        )
    )

    Text(
        "|",
        modifier = Modifier.alpha(alpha),
        style = style.copy(fontWeight = FontWeight.Bold),
        color = cursorColor,
    )
}
