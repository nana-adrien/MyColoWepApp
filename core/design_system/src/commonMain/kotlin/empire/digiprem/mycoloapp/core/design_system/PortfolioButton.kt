package empire.digiprem.mycoloapp.core.design_system

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.PointerIcon
import androidx.compose.ui.input.pointer.pointerHoverIcon
import androidx.compose.ui.unit.dp

enum class ButtonType {
    PRIMARY,
    SECONDARY,
}


@Composable
fun MyColoButton(
    text: String,
    model: Any? = null,
    isLoading: Boolean = false,
    enabled: Boolean = true,
    type: ButtonType = ButtonType.PRIMARY,
    modifier: Modifier = Modifier,
    onClick: () -> Unit,
) {

    Button(
        enabled = enabled,
        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 5.dp),
        shape = RoundedCornerShape(4.dp),

        elevation = ButtonDefaults.buttonElevation(
            defaultElevation = when (type) {
                ButtonType.PRIMARY -> 8.dp
                ButtonType.SECONDARY -> 4.dp
            }
        ),

        colors = ButtonDefaults.buttonColors(
            containerColor = when {
                isLoading -> MaterialTheme.colorScheme.primaryContainer
                !enabled -> MaterialTheme.colorScheme.surfaceVariant
                type == ButtonType.PRIMARY -> MaterialTheme.colorScheme.primary
                else -> MaterialTheme.colorScheme.background
            },

            contentColor = when {
                isLoading -> MaterialTheme.colorScheme.onPrimaryContainer
                !enabled -> MaterialTheme.colorScheme.onSurfaceVariant
                type == ButtonType.PRIMARY -> Color.White
                else -> MaterialTheme.colorScheme.primary
            }
        ),
        onClick = onClick,
        modifier = modifier.pointerHoverIcon(PointerIcon.Hand),
    ) {
        if (isLoading) {
            CircularProgressIndicator(
                modifier = Modifier.size(25.dp),
                color = MaterialTheme.colorScheme.primary,
            )
        }
        Spacer(Modifier.width(8.dp))
        Text(
            text = text,
            maxLines = 1,
            style = MaterialTheme
                .typography
                .labelSmall
                .let {
                    if (type == ButtonType.PRIMARY) {
                        it.copy(color = Color.White)
                    } else{
                        it
                    }
                }
        )
        model?.let { model ->
            PortfolioIcon(
                modifier = Modifier.size(25.dp),
                model = model,
                tint = if (type == ButtonType.PRIMARY) {
                    Color.White
                } else MaterialTheme.colorScheme.primary,
            )
        }

    }

}