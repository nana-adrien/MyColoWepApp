package empire.digiprem.mycolowepapp.core.design_system.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun PortfolioTextField(
    state: TextFieldState,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    modifier: Modifier = Modifier,
) {

    Column {
        BasicTextField(
            state = state,
            modifier = modifier,
            lineLimits = lineLimits,
            //  interactionSource = interactionSource,
            enabled = enabled,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(
                keyboardType = keyboardType
            ),
            textStyle = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.onBackground),
            decorator = {
                Box(
                    Modifier.fillMaxWidth().fillMaxHeight()
                        .clip(RoundedCornerShape(8.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .padding(8.dp),
                    contentAlignment = if (lineLimits is TextFieldLineLimits.SingleLine) Alignment.CenterStart else Alignment.TopStart,
                ) {
                    if (placeholder != null && state.text.isEmpty()) {
                        Text(
                            text = placeholder,
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = MaterialTheme.colorScheme.onSurface.copy(
                                    alpha = 0.5f
                                )
                            ),
                        )
                    }
                    it()
                }
            }
        )
        if (isError && !errorMessage.isNullOrEmpty() && state.text.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall.copy(color = MaterialTheme.colorScheme.error),
            )
        }
    }


}