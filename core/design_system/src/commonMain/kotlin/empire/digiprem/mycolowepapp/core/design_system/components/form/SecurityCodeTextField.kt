package empire.digiprem.mycolowepapp.core.design_system.components.form


import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.InputTransformation
import androidx.compose.foundation.text.input.TextFieldBuffer
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.foundation.text.input.placeCursorAtEnd
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import empire.digiprem.mycolowepapp.core.design_system.components.form.FieldBoxDecorator
import empire.digiprem.mycolowepapp.core.design_system.components.form.FieldHeader

@Composable
fun SecurityCodeTextField(
    modifier: Modifier = Modifier,
    title: String = "Code de sécurité",
    placeholder: String = "Entrez votre code",
    enabled: Boolean = true,
    maxLength: Int = 8,
    state: TextFieldState = rememberTextFieldState(),
    errorMessage: String? = null,
) {
    // Force majuscules, filtre et bloque au maxLength
    LaunchedEffect(Unit) {
        snapshotFlow { state.text }
            .collect { text ->
                val filtered = text
                    .toString()
                    .uppercase()
                    .filter { it.isLetterOrDigit() }
                    .take(maxLength)

                if (filtered != text.toString()) {
                    state.edit {
                        replace(0, length, filtered)
                        placeCursorAtEnd()
                    }
                }
            }
    }

    Column(modifier = Modifier.wrapContentSize()) {
        FieldHeader(title = title)

        FieldBoxDecorator(
            modifier = modifier.clip(RoundedCornerShape(8.dp)),
        ) {
            BasicTextField(
                state = state,
                enabled = enabled,
                inputTransformation = InputTransformation { // Bloque strictement toute saisie dépassant maxLength
                    val filtered = this.asCharSequence()//. asCharSequence()
                        .toString()
                        .uppercase()
                        .filter { it.isLetterOrDigit() }
                        .take(maxLength)

                    if (filtered != this.asCharSequence().toString()) {
                        this.replace(0, this.length, filtered)
                        this.placeCursorAtEnd()
                    }
                },
                keyboardOptions = KeyboardOptions(
                    keyboardType = KeyboardType.Text,
                    capitalization = KeyboardCapitalization.Characters,
                    imeAction = ImeAction.Done
                ),
                lineLimits = TextFieldLineLimits.SingleLine,
                textStyle = MaterialTheme.typography.bodyMedium.copy(
                    color = if (errorMessage != null)
                        MaterialTheme.colorScheme.error
                    else
                        MaterialTheme.colorScheme.onSurface,
                    letterSpacing = 4.sp
                ),
                decorator = { innerTextField ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 5.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Box(modifier = Modifier.weight(1f)) {
                            if (state.text.isEmpty()) {
                                Text(
                                    text = placeholder,
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                                    )
                                )
                            }
                            innerTextField()
                        }
                        Text(
                            text = "${state.text.length}/$maxLength",
                            style = MaterialTheme.typography.labelSmall.copy(
                                color = if (state.text.length == maxLength)
                                    MaterialTheme.colorScheme.primary
                                else
                                    MaterialTheme.colorScheme.onSurface.copy(alpha = 0.4f)
                            )
                        )
                    }
                }
            )
        }

        // Message d'erreur
        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn() + expandVertically(),
            exit = fadeOut() + shrinkVertically()
        ) {
            Row(
                modifier = Modifier.padding(start = 4.dp, top = 4.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(4.dp)
            ) {
                Icon(
                    imageVector = Icons.Filled.Warning,
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(12.dp)
                )
                Text(
                    text = errorMessage ?: "",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.error
                )
            }
        }
    }
}