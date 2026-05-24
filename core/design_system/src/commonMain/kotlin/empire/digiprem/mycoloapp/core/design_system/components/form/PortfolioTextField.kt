package empire.digiprem.mycoloapp.core.design_system.components.form

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

@Composable
fun FieldHeader(
    title: String,
) {
    Text(
        text = title,
        style = MaterialTheme.typography.bodyMedium.copy(color = MaterialTheme.colorScheme.onBackground),
    )
    Spacer(modifier = Modifier.height(10.dp))
}

@Composable
fun FieldBoxDecorator(
    modifier: Modifier = Modifier,
    contentAlignment: Alignment = Alignment.CenterStart,
    content: @Composable BoxScope.() -> Unit
) {
    Box(
        modifier.heightIn(min = 40.dp).fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(MaterialTheme.colorScheme.background)
            .padding(8.dp),
        contentAlignment = contentAlignment,
        content = content
    )

}

@Composable
fun MyColoTextField(
    state: TextFieldState,
    title: String,
    placeholder: String? = null,
    enabled: Boolean = true,
    isError: Boolean = false,
    errorMessage: String? = null,
    keyboardType: KeyboardType = KeyboardType.Unspecified,
    lineLimits: TextFieldLineLimits = TextFieldLineLimits.SingleLine,
    leadingIcon: ImageVector? = null,
    modifier: Modifier = Modifier,
) {
    Column(modifier = Modifier.wrapContentSize()) {
        FieldHeader(title = title)
        BasicTextField(
            state = state,
            lineLimits = lineLimits,
            modifier = Modifier.wrapContentSize(),
            enabled = enabled,
            cursorBrush = SolidColor(MaterialTheme.colorScheme.primary),
            keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
            textStyle = MaterialTheme.typography.bodyMedium.copy(
                color = MaterialTheme.colorScheme.onBackground
            ),
            decorator = {
                FieldBoxDecorator(
                    modifier = modifier,
                    contentAlignment = if (lineLimits is TextFieldLineLimits.SingleLine)
                        Alignment.CenterStart else Alignment.TopStart,
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        if (leadingIcon != null) {
                            Icon(
                                imageVector = leadingIcon,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f),
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Box {
                            if (placeholder != null && state.text.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    ),
                                )
                            }
                            it()
                        }
                    }
                }
            }
        )
        if (isError && !errorMessage.isNullOrEmpty() && state.text.isNotBlank()) {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = errorMessage,
                style = MaterialTheme.typography.labelSmall.copy(
                    color = MaterialTheme.colorScheme.error
                ),
            )
        }
    }
}